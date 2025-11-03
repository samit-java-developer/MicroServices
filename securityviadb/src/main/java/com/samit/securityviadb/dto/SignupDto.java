package com.samit.securityviadb.dto;

import lombok.Data;

@Data
public class SignupDto {
    private String name;
    private String email;
    private String password;
}
