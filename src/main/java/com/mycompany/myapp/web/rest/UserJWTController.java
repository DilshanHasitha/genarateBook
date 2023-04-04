package com.mycompany.myapp.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.domain.OrgProperty;
import com.mycompany.myapp.security.jwt.JWTFilter;
import com.mycompany.myapp.security.jwt.TokenProvider;
import com.mycompany.myapp.service.OrgPropertyService;
import com.mycompany.myapp.service.dto.wikunum.JwtResponse;
import com.mycompany.myapp.web.rest.vm.LoginVM;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final OrgPropertyService orgPropertyService;

    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        OrgPropertyService orgPropertyService
    ) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.orgPropertyService = orgPropertyService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    public JwtResponse getJWTAuth() {
        Optional<OrgProperty> token = orgPropertyService.findOneByName("id_token");
        OrgProperty orgToken = new OrgProperty();
        JwtResponse newToken = new JwtResponse();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (token.isPresent() && token.get().getDescription() != null) {
            orgToken = token.get();
            String url = "http://demo.wikunum.lk/api/account";
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + orgToken.getDescription());
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                if (200 == response.getStatusCodeValue()) {
                    orgToken.getDescription();
                    newToken.setId_token("Bearer " + orgToken.getDescription());
                    return newToken;
                } else {
                    JwtResponse jwtResponse = createNewJWT();
                    orgToken.setDescription(jwtResponse.getId_token());
                    orgPropertyService.save(orgToken);
                    newToken.setId_token("Bearer " + jwtResponse.getId_token());
                    return newToken;
                }
            } catch (Exception e) {
                JwtResponse jwtResponse = createNewJWT();
                orgToken.setDescription(jwtResponse.getId_token());
                orgPropertyService.save(orgToken);
                newToken.setId_token("Bearer " + jwtResponse.getId_token());
                return newToken;
            }
        } else {
            JwtResponse jwtResponse = createNewJWT();
            orgToken.setName("id_token");
            orgToken.setDescription(jwtResponse.getId_token());
            orgToken.isActive(true);
            orgPropertyService.save(orgToken);
            newToken.setId_token("Bearer " + jwtResponse.getId_token());
            return newToken;
        }
    }

    public JwtResponse createNewJWT() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String url = "http://demo.wikunum.lk/api/authenticate";
        String requestJson = "{\"username\":\"talemein\",\"password\":\"123456\",\"rememberMe\":false}";
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        JwtResponse stringResponseEntity = restTemplate.postForEntity(url, entity, JwtResponse.class).getBody();
        return stringResponseEntity;
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
