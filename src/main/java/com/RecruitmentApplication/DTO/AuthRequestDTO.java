package com.RecruitmentApplication.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequestDTO {

    private String username ;
    private String password;
    private String emailId;
    private String token;
    private String otp;
    private String employeeId;
    private String confirmPass;
    private String role;
}
