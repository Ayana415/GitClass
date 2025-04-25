package com.Project1.Project1.Mail;


	
	import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.mail.SimpleMailMessage;
	import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Project1.Project1.Model.Formmodel;
import com.Project1.Project1.Model.OtpModel;
import com.Project1.Project1.Repo.OtpRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

	@Service
	
	public class Mailsender {
		
		@Autowired
		JavaMailSender mailSender;
		
		
		public void simplemail(Formmodel form) throws MessagingException{
			
			  MimeMessage mimeMessage = mailSender.createMimeMessage();

		      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);

			
			

			String to= form.getMail();
			String from="scopeindia@gmail.org";
			String bcc="ayanamohan25@gmail.com";
			String cc="gsaraswathi335@gmail.com";
			String Subject="Testing Mail";
			String baseURL=generateDynamicURL(form);
			String htmlContent= "<p> Dear  "+form.getName() +"</p>"+"</br>"+"<p> Thankyou for Contacting SCOPE INDIA regarding the course "+form.getMessage()+"</p>"+
					"</br>"+"<a href=' "+ baseURL +" '> Click this link for Verification"+"</a>";
					
			messageHelper .setTo(to);
			messageHelper .setFrom(from);
			messageHelper .setBcc(bcc);
			messageHelper .setCc(cc);
			messageHelper .setSubject(Subject);
			messageHelper.setText(htmlContent, true);
			
			mailSender.send(mimeMessage );
		}
		
		private String generateDynamicURL(Formmodel form) {
//			String baseURL = "http://localhost:8080/spr/sigin?email=" + form.getMail() ;
			String baseURL = "http://localhost:8080/spr/sigin?token=" + form.getToken() ;
	        return baseURL;
			}
		

		public String generateOtp() {
		        SecureRandom random = new SecureRandom();
		        int otp = random.nextInt(999999);
		       
		        return String.valueOf( otp);  // Format OTP to be 6 digits
		       // String.format("%06d", otp);  
		    }
		 
		 
		 public void sendOtpEmail(String email,String otp) throws MessagingException {
			 
			 if (email == null || email.isEmpty()) {
			        throw new IllegalArgumentException("Email address must not be null or empty");
			    }
		       
		        
		        // Save OTP to database with expiration (e.g., 10 minutes)
		        
		
		        MimeMessage mimeMessage = mailSender.createMimeMessage();
		        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

		        String subject = "Your OTP for Verification";
		        String messageContent = "<p>Your OTP is: <strong>" + otp + "</strong></p><p>It will expire in 1 minute.</p>";
		        

		        messageHelper.setTo(email);
		        messageHelper.setSubject(subject);
		        messageHelper.setText(messageContent, true);

		        mailSender.send(mimeMessage);
	}

	}

