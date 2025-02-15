package com.Project1.Project1.Mail;


	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.mail.SimpleMailMessage;
	import org.springframework.mail.javamail.JavaMailSender;
	import org.springframework.stereotype.Service;

import com.Project1.Project1.Model.Formmodel;

import jakarta.mail.MessagingException;

	@Service
	public class Mailsender {
		
		@Autowired
		JavaMailSender mailSender;
		
		
		public void simplemail(Formmodel form) throws MessagingException{
			
			
			SimpleMailMessage simplemailmessage=new SimpleMailMessage();
			
			String to= form.getMail();
			String from="scopeindia@gmail.org";
			String bcc="ayanamohan25@gmail.com";
//			String cc="gsaraswathi335@gmail.com";
			String Subject="Testing Mail";
			String text= "HELLO ITS ME " + form.getName()+" "+ form.getMessage();
			
			simplemailmessage.setTo(to);
			simplemailmessage.setFrom(from);
			simplemailmessage.setBcc(bcc);
//			simplemailmessage.setCc(cc);
			simplemailmessage.setSubject(Subject);
			simplemailmessage.setText(text);
			
			mailSender.send(simplemailmessage);
		}
		

	}



