package com.assignment.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AuthRequest {
    @Id
    @JsonProperty("login_id")
    private String loginId;

    @JsonProperty("password")
    private String password;

    public AuthRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
