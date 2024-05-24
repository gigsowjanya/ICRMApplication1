package com.RecruitmentApplication.Entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="employee_details_tbl")
public class EmployeeDetailsTbl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="employee_Id")
	private String employeeId;
	
	@Column(name="first_Name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="email_Id")
	private String emailId;
	
	@Column(name="phone_Number")
	private String phoneNumber;
	
	@Column(name="password")
	private String password;
	private String department;
	private String status;
	private String otp;
	private LocalDateTime otpExpirationTime;
	// added by Vinayak for Storing manager_Id in table
	@Column(name = "manager_Id")
	private String managerId;

}
