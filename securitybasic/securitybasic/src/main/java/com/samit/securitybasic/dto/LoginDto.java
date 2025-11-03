package com.samit.securitybasic.dto;

import lombok.Data;

@Data
public class LoginDto {
    // you can add validation here
    private String email;
    private String password;
}