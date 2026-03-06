package com.ecomapp.controller.User;

import com.ecomapp.config.security.JwtAuthorizationFilter;
import com.ecomapp.config.security.JwtTokenProvider;
import com.ecomapp.dto.LoginDTO;
import com.ecomapp.dto.ResponseDTO;
import com.ecomapp.dto.request.AuthenticationRequestDTO;
import com.ecomapp.models.User;
import com.ecomapp.services.UserService;
import com.ecomapp.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Resource
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @PostMapping("/token")
    public ResponseEntity<LoginDTO> login(@RequestBody AuthenticationRequestDTO authRequest) {

        try {
            AbstractAuthenticationToken authToken = getAuthenticationToken(authRequest);
            var authentication = authenticationManager.authenticate(authToken);

            String token = tokenProvider.createToken(authentication);

            Optional<User> userOptional = userService.getByUsername(authentication.getName());

            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new LoginDTO(true, "User not found"));
            }

            return new ResponseEntity<>(
                    new LoginDTO(token, userOptional.get()),
                    getHeaders(token),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginDTO(true, e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody User user) {

        Errors errors = new BeanPropertyBindingResult(user, "User");
        userValidator.validate(user, errors);

        if (errors.hasErrors() && Objects.nonNull(errors.getFieldError())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(null, errors.getFieldError().getDefaultMessage()));
        }

        try {
            User registeredUser = userService.register(user);
            return ResponseEntity.ok(new ResponseDTO(registeredUser, ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(null, e.getMessage()));
        }
    }

    private HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, JwtAuthorizationFilter.HEADER_PREFIX + token);
        return headers;
    }

    private AbstractAuthenticationToken getAuthenticationToken(AuthenticationRequestDTO request) {
        return new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                new String(Base64.getDecoder().decode(request.getPassword()))
        );
    }
}