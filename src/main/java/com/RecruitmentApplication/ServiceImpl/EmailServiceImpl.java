package com.RecruitmentApplication.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl {
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String fromEmail;
	public String sendEmailToVerifyOTP(String emailId, String otp) {
		try {

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setTo(emailId);
			mimeMessageHelper.setSubject("Admin-Gigleaz");
			mimeMessageHelper.setText("<html><body> Dear User,<br/><br/>"
					+ "Thank you for choosing <strong>GIGLEAZ PVT LTD!</strong> To ensure the security of your account, we need to verify your identity.<br/><br/>"
					+ "Your One-Time Password (OTP) for verification is: <strong>" + otp + "</strong><br/><br/>"
					+ "Please use this code within the next 5 minutes to complete the verification process.<br/><br/>"
					+ "If you did not request this OTP or need any assistance, please contact our support team immediately.<br/><br/>"
					+ "<br/><br/>" + "Thank you for your cooperation.</body></html>", true);
			javaMailSender.send(mimeMessage);
			System.out.println("Email sent successfully!");
			return "email send sucussfully";
		} catch (Exception e) {
			return "email not send";
		}
	}

}
