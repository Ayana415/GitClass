package com.Project1.Project1.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Project1.Project1.Mail.Mailsender;
import com.Project1.Project1.Model.Formmodel;
import com.Project1.Project1.Service.Formservice;

import jakarta.mail.MessagingException;


@Controller
@RequestMapping("/spr")
	
	

public class Formcontroller {
	
	@Autowired
	Formservice service;
	
	@Autowired
	Mailsender mailSender;

	@GetMapping("/form")
	public String Form(Model model) {
		
		model.addAttribute("user",new Formmodel());
		return "Formscope";
		
		}
	
//	@PostMapping("/send")
//	public String Fomdetails(@ModelAttribute Formmodel form) {
//		service.savemethod(form);
//		return "ttt" ;
//	}
	
	
   @PostMapping("/sendmail")
	
	public ResponseEntity<String> sendmail(){
//		mailSender.simplemail();
		return new ResponseEntity<String>("Mail send",HttpStatus.OK);
	}
	
	
	@PostMapping("/send")
	
	public ResponseEntity<String> mail(@ModelAttribute Formmodel User) throws MessagingException{
		
		service.savemethod(User);
		mailSender.simplemail(User);
		return new ResponseEntity<String>("Mail send",HttpStatus.OK);
	}
	

}


