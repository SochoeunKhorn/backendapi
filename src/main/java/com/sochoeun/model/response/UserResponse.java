package com.sochoeun.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String profile;
    private String status;
    private List<String> roles;
}
