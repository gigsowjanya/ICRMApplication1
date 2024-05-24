package com.RecruitmentApplication.ServiceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.RecruitmentApplication.DTO.AuthRequestDTO;
import com.RecruitmentApplication.Entity.EmployeeDetailsTbl;
import com.RecruitmentApplication.Repository.EmployeeRepository;
import com.RecruitmentApplication.Security.JwtService;
import com.RecruitmentApplication.Service.EmploeeService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmployeeSeviceImpl implements EmploeeService {
	private final Logger LOGGER = LoggerFactory.getLogger(EmployeeSeviceImpl.class);
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Value("${spring.mail.username}")
	private String fromEmail;

	@Override
	public ResponseEntity<?> saveEmployee(EmployeeDetailsTbl dto) {
		LOGGER.info("employye save data {}:", dto);
		EmployeeDetailsTbl emp = employeeRepository.findByEmailId(dto.getEmailId());
		if (emp == null) {
			String empId = EmployeeId();
			dto.setPassword(passwordEncoder.encode(dto.getPassword()));
			dto.setEmployeeId(empId);
			employeeRepository.save(dto);
			return ResponseEntity.status(HttpStatus.OK).body("Employee created successfully!");
		}
		emp.setPassword(passwordEncoder.encode(dto.getPassword()));
		String empId = EmployeeId();
		emp.setEmployeeId(empId);
		emp.setManagerId(dto.getManagerId());
		employeeRepository.save(emp);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee already exist!");

	}

	private String EmployeeId() {
		Random r = new Random();
		int random = r.nextInt(9000) + 10000;
		StringBuilder sb = new StringBuilder();
		sb.append("GIG");
		sb.append(random);
		return sb.toString();
	}

	@Override
	public ResponseEntity<?> forrogotPassword(AuthRequestDTO dto) {
		EmployeeDetailsTbl emp = employeeRepository.findByEmailId(dto.getEmailId());
		if (emp != null) {
			String otp = generateOTP();
			String email =emailServiceImpl.sendEmailToVerifyOTP(emp.getEmailId(), otp);
			LOGGER.info("email send Status {}:", email);
			emp.setOtp(otp);
			emp.setOtpExpirationTime(LocalDateTime.now().plus(5, ChronoUnit.MINUTES));
			employeeRepository.save(emp);
			return ResponseEntity.status(HttpStatus.OK).body("Please check your email to verify the OTP.");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee details not registered!");
	}
	private String generateOTP() {
		Random r = new Random();
		int random = r.nextInt(9000) + 100000;
		StringBuilder sb = new StringBuilder();
		sb.append(random);
		return sb.toString();
	}

	@Override
	public ResponseEntity<?> employeeLogin(AuthRequestDTO dto) {
		org.springframework.security.core.Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
		if (authentication.isAuthenticated()) {
			String token = jwtService.generateToken(dto.getUsername());
			EmployeeDetailsTbl emp = employeeRepository.findByEmployeeId(dto.getUsername());
			if (emp != null) {
				dto.setRole(emp.getDepartment());
				dto.setEmailId(emp.getEmailId());
				dto.setEmployeeId(emp.getEmployeeId());
			}
			dto.setToken(token);
			dto.setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user request !");
		}
	}

	@Override
	public ResponseEntity<?> verifyOtp(AuthRequestDTO dto) {
		// TODO Auto-generated method stub
//		String loggedInEmail = "";
//
//		HttpSession session = request.getSession(false);
//
//		if (session != null) {
//			loggedInEmail = (String) session.getAttribute("loggedInEmail");
//			// Use the logged-in email in your further methods
//		}
//
//		EmployeeDetailsTbl emp = employeeRepository.findByEmailId(loggedInEmail);

		EmployeeDetailsTbl emp= employeeRepository.findByEmailId(dto.getEmailId());
		if (emp != null) {
			if (emp.getOtp().equals(dto.getOtp()) && LocalDateTime.now().isBefore(emp.getOtpExpirationTime())) {

				return ResponseEntity.status(HttpStatus.OK).body("OTP verified sucessfully");
			} 
			
			else if(!emp.getOtp().equals(dto.getOtp())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entered OTP is InCorrect!");
			}
			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP is expired!");	
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
	}
//	private boolean isOtpValid(LocalDateTime otpTimestamp) {
//	// Check if current time is within 5 minutes of OTP creation time
//	LocalDateTime currentTime = LocalDateTime.now();
//	LocalDateTime otpExpirationTime = otpTimestamp.plusMinutes(5);
//	return currentTime.isBefore(otpExpirationTime);
//}

	@Override
	public ResponseEntity<?> resetPassword(AuthRequestDTO dto) {
		// TODO Auto-generated method stub
//		String loggedInEmail="";
//		
//		HttpSession session = request.getSession(false);
//		
//		if (session != null) {
//            loggedInEmail = (String) session.getAttribute("loggedInEmail");
//            // Use the logged-in email in your further methods
//        }

		EmployeeDetailsTbl empOptional = employeeRepository.findByEmailId(dto.getEmailId());
		if (empOptional != null) {
			if (passwordEncoder.matches(dto.getPassword(), empOptional.getPassword())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("New password should not match the old password!");
			}
			empOptional.setPassword(passwordEncoder.encode(dto.getPassword()));
			employeeRepository.save(empOptional);
			return ResponseEntity.status(HttpStatus.OK).body("Password Changed Successfully");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
	}
}
