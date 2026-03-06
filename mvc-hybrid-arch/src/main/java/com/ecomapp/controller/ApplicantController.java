@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class ApplicantController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CatalogueValidator catalogueValidator;

    @Resource
    private ApplicantService applicantService;

    @GetMapping("/{applicationId}")
    public CompletableFuture<Applicant> getApplication(@PathVariable int applicationId) {
        return applicantService.getByID(applicationId);
    }

    @GetMapping("/synods/{synodId}/users/{userId}")
    public CompletableFuture<List<Applicant>> getApplications(
            @PathVariable int userId,
            @PathVariable int synodId) {

        return applicantService.getByUserAndSynod(synodId, userId);
    }

    @PostMapping("/{applicationId}/finish")
    public CompletableFuture<ResponseEntity<ResponseDTO>> finishApplication(
            @PathVariable int applicationId) {

        return applicantService.finishApplication(applicationId)
                .thenApply(result ->
                        ResponseEntity.ok(new ResponseDTO(result, "")))
                .exceptionally(ex ->
                        ResponseEntity.badRequest()
                                .body(new ResponseDTO(null, ex.getMessage())));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<ResponseDTO>> createApplication(
            @RequestPart("catalogue") String jsonDocument,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                Catalogue catalogue = objectMapper.readValue(jsonDocument, Catalogue.class);

                Errors errors = new BeanPropertyBindingResult(catalogue, "Catalogue");
                catalogueValidator.validate(catalogue, errors);

                if (errors.hasErrors() && Objects.nonNull(errors.getFieldError())) {
                    return ResponseEntity.badRequest()
                            .body(new ResponseDTO(null,
                                    errors.getFieldError().getDefaultMessage()));
                }

                boolean notExist =
                        applicantService.activeApplicationNotExist(catalogue).join();

                if (!notExist) {
                    return ResponseEntity.badRequest()
                            .body(new ResponseDTO(null, "Application already exist"));
                }

                if (file != null) {
                    catalogue.setFile(file.getBytes());
                    catalogue.setFileName(file.getOriginalFilename());
                }

                Applicant created =
                        applicantService.createApplication(catalogue).join();

                return ResponseEntity.ok(new ResponseDTO(created, ""));

            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO(null, e.getMessage()));
            }
        });
    }
}