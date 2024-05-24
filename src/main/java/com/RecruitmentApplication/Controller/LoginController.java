package com.RecruitmentApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RecruitmentApplication.DTO.AuthRequestDTO;
import com.RecruitmentApplication.Entity.EmployeeDetailsTbl;
import com.RecruitmentApplication.Service.EmploeeService;

@RestController
@RequestMapping("/api/v1.0")
@CrossOrigin(origins = "*")
public class LoginController {
	@Autowired
	private EmploeeService emploeeService;

	@PostMapping("/employee")
	public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDetailsTbl dto) {
		try {
			ResponseEntity<?> data = emploeeService.saveEmployee(dto);
			return data;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please try after some times!");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> employeeLogin(@RequestBody AuthRequestDTO dto) {
		try {
			ResponseEntity<?> data = emploeeService.employeeLogin(dto);
			return data;
		}

		catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid employeeId or  password!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please try after some times!!");
		}
	}

	@PutMapping("/forgotPwd")
	public ResponseEntity<?> forrogotPassword(@RequestBody AuthRequestDTO dto) {
		try {
			ResponseEntity<?> data = emploeeService.forrogotPassword(dto);
			return data;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please try after some times!");
		}
	}

	@PutMapping("/verify")
	public ResponseEntity<?> verifyOtp(@RequestBody AuthRequestDTO dto) {
		try {
			ResponseEntity<?> data = emploeeService.verifyOtp(dto);
			return data;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please try after some times!");
		}

	}

	@PutMapping("/reset")
	public ResponseEntity<?> resetPassword(@RequestBody AuthRequestDTO dto) {
		if (dto.getConfirmPass().equals(dto.getPassword())) {
			try {
				ResponseEntity<?> data = emploeeService.resetPassword(dto);
				return data;
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please try after some times!");
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match!");
		}
	}
}
