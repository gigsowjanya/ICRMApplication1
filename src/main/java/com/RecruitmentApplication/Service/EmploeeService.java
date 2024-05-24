package com.RecruitmentApplication.Service;

import org.springframework.http.ResponseEntity;

import com.RecruitmentApplication.DTO.AuthRequestDTO;
import com.RecruitmentApplication.Entity.EmployeeDetailsTbl;

public interface EmploeeService {

	ResponseEntity<?> saveEmployee(EmployeeDetailsTbl dto);

	ResponseEntity<?> forrogotPassword(AuthRequestDTO dto);

	ResponseEntity<?> employeeLogin(AuthRequestDTO dto);

	ResponseEntity<?> verifyOtp(AuthRequestDTO dto);

	ResponseEntity<?> resetPassword(AuthRequestDTO dto);

}
