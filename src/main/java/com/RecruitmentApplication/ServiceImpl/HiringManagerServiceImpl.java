package com.RecruitmentApplication.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.RecruitmentApplication.Entity.EmployeeDetailsTbl;
import com.RecruitmentApplication.Entity.JobStatusTbl;
import com.RecruitmentApplication.Entity.NewJobRequestTbl;
import com.RecruitmentApplication.Repository.EmployeeRepository;
import com.RecruitmentApplication.Repository.HiringManagerRepository;
import com.RecruitmentApplication.Repository.JobStatusRepository;
import com.RecruitmentApplication.Service.HiringManagerService;

import jakarta.mail.internet.MimeMessage;

@Service
public class HiringManagerServiceImpl implements HiringManagerService {

	@Value("${spring.mail.username}")
	private String fromEmail;

	private final Logger LOGGER = LoggerFactory.getLogger(HiringManagerServiceImpl.class);

	@Autowired
	private HiringManagerRepository jobRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	private
	JobStatusRepository jobStatusRepository;

	@Override
	public ResponseEntity<String> createNewJobRequest(NewJobRequestTbl newJobRequest) {
		// Create a new JobDetails entity
		String jobId = generateJobId();
		newJobRequest.setJobId(jobId);
		// Get job details from newJobRequestTbl
		String jobTitle = newJobRequest.getJobTitle();
		String department = newJobRequest.getDepartment();
		String jobDescription = newJobRequest.getJobDescription();
		String createdBy = newJobRequest.getCreatedBy();
		LocalDateTime createdDate = LocalDateTime.now();
		String status = "Pending"; // Set default status as "Pending"
		int numberOfPositions = newJobRequest.getNumberOfPositions();
		String experience = newJobRequest.getExperience();
		newJobRequest.setCreatedDate(createdDate);
		// added by Vinayak for Sending Mail for Multiple Users
		EmployeeDetailsTbl hemp = employeeRepository.findByEmployeeId(newJobRequest.getCreatedBy());
		EmployeeDetailsTbl memp = employeeRepository.findByEmployeeId(hemp.getManagerId());
		List<String> emailIds = Arrays.asList(hemp.getEmailId(), memp.getEmailId());
		String email = sendMail(emailIds, jobId, jobTitle, department,numberOfPositions, experience,jobDescription,createdBy,
				createdDate);
		LOGGER.info("email send Status {}:", email);

		jobRepository.save(newJobRequest);
		// added by vinayak for job status entity
				JobStatusTbl jobStatus = new JobStatusTbl();
				jobStatus.setEmpId(newJobRequest.getCreatedBy());
				jobStatus.setRole(hemp.getDepartment());
				jobStatus.setJobId(jobId);
				jobStatus.setStatus(newJobRequest.getStatus());
				jobStatus.setTime(LocalTime.now());
				jobStatus.setDate(LocalDate.now());
				jobStatus.setComments(newJobRequest.getComments());
				jobStatusRepository.save(jobStatus);
		return ResponseEntity.status(HttpStatus.OK)
				.body("Your request is sucessfully submited Here is your JobId : " + jobId);
	}

	// added by Sowjanya for Generating JobId
	private String generateJobId() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append("DES");
		// Generate a random number of specified length
		for (int i = 0; i < 12; i++) {
			int digit = random.nextInt(10);
			sb.append(digit);
		}
		return sb.toString();
	}

	// added by Vinayak for Sending Mail for Multiple Users
	private String sendMail(List<String> emailIds, String jobId, String jobTitle,String department,int numberOfPositions,
			 String experience,String JobDescription,
			String createdBy, LocalDateTime createdDate) {
		try {

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setTo(emailIds.toArray(new String[0]));
			mimeMessageHelper.setSubject("Job-Openings");
			// Constructing the email content in HTML format
			StringBuilder emailContent = new StringBuilder();
			emailContent.append("<html><body>");
			emailContent.append("Dear Supervisor Manager,<br/><br/>");
			emailContent.append("We have a new job Request to you:<br/><br/>");
			emailContent.append("<table border='1'>");
			emailContent.append(
					"<tr><th>Job ID</th><th>Job Title</th><th>Department</th><th>Number of Positions</th>"
					+ "<th>Experience</th><th>jobDescription</th><th>createdBy</th><th>createdDate</th></tr>");
			emailContent.append("<tr>");
			emailContent.append("<td>").append(jobId).append("</td>");
			emailContent.append("<td>").append(jobTitle).append("</td>");
			emailContent.append("<td>").append(department).append("</td>");
			emailContent.append("<td>").append(numberOfPositions).append("</td>");
			emailContent.append("<td>").append(experience).append("</td>");
			emailContent.append("<td>").append(JobDescription).append("</td>");
			emailContent.append("<td>").append(createdBy).append("</td>");
			emailContent.append("<td>").append(createdDate).append("</td>");
			emailContent.append("</tr>");
			emailContent.append("</table><br/><br/>");
			emailContent.append(
					"Please review the job details and let us know if you have any questions or if you would like to proceed.<br/><br/>");
			emailContent.append("Best regards,<br/>");
			emailContent.append("GIGLEAZ</body></html>");

			mimeMessageHelper.setText(emailContent.toString(), true);
			javaMailSender.send(mimeMessage);
			System.out.println("Email sent successfully!");
			return "Email sent successfully!";
		} catch (Exception e) {
			return "email not send";
		}

	}
	@Override
	public ResponseEntity<?> updateJobRequest(NewJobRequestTbl newJobRequest) {
		NewJobRequestTbl data=jobRepository.findByJobId(newJobRequest.getJobId());
		if(data!=null ) {
			if(!data.getStatus().equalsIgnoreCase("Pending")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You do not have access to update the jobRequest!");
			}
			data.setDepartment(newJobRequest.getDepartment());
			data.setExpectedDateOfJoining(newJobRequest.getExpectedDateOfJoining());
			data.setAvailableBudget(newJobRequest.getAvailableBudget());
			data.setJobDescription(newJobRequest.getJobDescription());
			data.setNumberOfPositions(newJobRequest.getNumberOfPositions());
			data.setJobTitle(newJobRequest.getJobTitle());
			data.setStatus(newJobRequest.getStatus());
			data.setExperience(newJobRequest.getExperience());
			jobRepository.save(data);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("job Request updated successfully");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("job Id does not exist!");
	}
	// code by sowjanya for search/filter operation
	@Override
	public List<NewJobRequestTbl> searchAndFilter(String jobId, String jobTitle, String department, String status,
			int numberOfPositions, String experience) {
		
		return jobRepository.searchAndFilter(jobId, jobTitle, department, status, numberOfPositions,experience);

	}

	// added by Sowjanya for retreving all jobrequests
	@Override
	public List<NewJobRequestTbl> getAllJobRequests() {
		// TODO Auto-generated method stub

		return jobRepository.findAll();

	}

	@Override
	public NewJobRequestTbl getById(String jobId) {
		return jobRepository.findByJobId(jobId);
	}
	
	@Override
	public List<JobStatusTbl> getJobStatus() {
		// TODO Auto-generated method stub

		return jobStatusRepository.findAll();

	}
}
