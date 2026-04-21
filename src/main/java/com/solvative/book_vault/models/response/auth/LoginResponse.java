package com.solvative.book_vault.models.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class LoginResponse {

    private String token;
    private String type = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }
}
