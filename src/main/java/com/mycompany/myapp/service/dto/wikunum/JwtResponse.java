package com.mycompany.myapp.service.dto.wikunum;

public class JwtResponse {

    private String id_token;

    public JwtResponse() {}

    public JwtResponse(String id_token) {
        this.id_token = id_token;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }
}
