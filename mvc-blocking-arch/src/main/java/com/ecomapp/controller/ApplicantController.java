package com.ecomapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecomapp.dto.ResponseDTO;
import com.ecomapp.models.*;
import com.ecomapp.services.ApplicantService;
import com.ecomapp.validators.CatalogueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    public Applicant getApplication(@PathVariable int applicationId) {
        return applicantService.getByID(applicationId);
    }

    @GetMapping("/{applicationId}/synods/{synodId}")
    public Applicant getApplication(@PathVariable int applicationId, @PathVariable int synodId) {
        return applicantService.getApplication(applicationId, synodId);
    }

    @GetMapping("/synods/{synodId}/users/{userId}")
    public List<Applicant> getApplications(@PathVariable int userId, @PathVariable int synodId) {
        return applicantService.getByUserAndSynod(synodId, userId);
    }

    @GetMapping("/synods/{synodId}/registrar/{registrarId}")
    public ResponseEntity<ResponseDTO> getApplicationsForReview(@PathVariable int synodId,
                                                                @PathVariable int registrarId) {
        try {
            List<Applicant> applications =
                    applicantService.getApplicationsForRegistrar(synodId, registrarId);
            return ResponseEntity.ok(new ResponseDTO(applications, ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(null, e.getMessage()));
        }
    }

    @GetMapping("/{applicationId}/reports")
    public List<ApplicationReport> getReportsForApplication(@PathVariable int applicationId) {
        return applicantService.getReportForApplication(applicationId);
    }

    @PostMapping("/{applicationId}/reports")
    public ResponseEntity<ApplicationReport> addReportForApplication(@RequestBody ApplicationReport report) {
        return ResponseEntity.ok(applicantService.createReport(report));
    }

    @DeleteMapping("/reports/{reportId}/users/{userId}")
    public ResponseEntity<Void> deleteReport(@PathVariable int reportId,
                                             @PathVariable int userId) {
        applicantService.deleteReport(reportId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicationId}/close")
    public ResponseEntity<Void> closeApplication(@PathVariable int applicationId) {
        applicantService.closeApplication(applicationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{applicationId}/delete")
    public ResponseEntity<Void> deleteApplication(@PathVariable int applicationId) {
        applicantService.deleteApplication(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicationId}/finish")
    public ResponseEntity<ResponseDTO> finishApplication(@PathVariable int applicationId) {
        try {
            Applicant finished = applicantService.finishApplication(applicationId);
            return ResponseEntity.ok(new ResponseDTO(finished, ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(null, e.getMessage()));
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> createApplication(
            @RequestPart("catalogue") String jsonDocument,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws Exception {

        Catalogue catalogue = objectMapper.readValue(jsonDocument, Catalogue.class);

        Errors errors = new BeanPropertyBindingResult(catalogue, "Catalogue");
        catalogueValidator.validate(catalogue, errors);

        if (errors.hasErrors() && Objects.nonNull(errors.getFieldError())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(null, errors.getFieldError().getDefaultMessage()));
        }

        boolean notExist = applicantService.activeApplicationNotExist(catalogue);

        if (!notExist) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(null, "Application already exist"));
        }

        if (file != null) {
            catalogue.setFile(file.getBytes());
            catalogue.setFileName(file.getOriginalFilename());
        }

        Applicant created = applicantService.createApplication(catalogue);
        return ResponseEntity.ok(new ResponseDTO(created, ""));
    }
}