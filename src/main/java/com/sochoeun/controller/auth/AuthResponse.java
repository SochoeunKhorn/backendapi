package com.sochoeun.controller.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String profile;
    private List<String> roles;

    @JsonProperty(value = "access_token")
    private String token;
    @JsonProperty(value = "refresh_token")
    private String refreshToken;
}
