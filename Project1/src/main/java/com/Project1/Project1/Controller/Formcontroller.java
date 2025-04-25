	package com.Project1.Project1.Controller;
	
	import java.time.Duration;

	import java.time.LocalDateTime;
	import java.time.temporal.ChronoUnit;
	import java.util.List;
	import java.util.UUID;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.ModelAttribute;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.RestController;
	import org.springframework.web.bind.annotation.SessionAttributes;
	
	import com.Project1.Project1.Mail.Mailsender;
	import com.Project1.Project1.Model.Formmodel;
	import com.Project1.Project1.Model.OtpModel;
	import com.Project1.Project1.Repo.Forminterface;
	import com.Project1.Project1.Repo.OtpRepository;
	import com.Project1.Project1.Service.Formservice;
	
	import jakarta.mail.MessagingException;
	import jakarta.servlet.http.HttpSession;
	
	@Controller
	@SessionAttributes("user")
	@RequestMapping("/spr")
	
	public class Formcontroller {
	
		@Autowired
		Formservice service;
	
		@Autowired
		Mailsender mailSender;
	
		@Autowired
		OtpRepository otprepo;
	
		@Autowired
		private HttpSession session;
		
		@Autowired 
		Forminterface repo;
		

		@GetMapping("/form")
		public String Form(Model model) {
	
			model.addAttribute("user", new Formmodel());
			return "Formscope";
	
		}
	
	
		@PostMapping("/send")
	
		public String mail( @ModelAttribute Formmodel User,Model model) throws MessagingException {
	

			
			String token = UUID.randomUUID().toString();
		    User.setToken(token); // ✅ set the token

		   
			service.savemethod( User);
			mailSender.simplemail( User);
			System.out.println(User.getMail()); // pass it till here
     		model.addAttribute("user", User); // store in a model
			session.setAttribute("user",  User);//store in session
			return "redirect:/spr/form";
	
		}
	
		@GetMapping("/sigin")
		public String showVerificationPage1(@RequestParam("token") String token,Model model,HttpSession session) {
//			
//			session.setAttribute("user", User);
//			 Formmodel user = (Formmodel) session.getAttribute("user");
			Formmodel user = service.getUserByToken(token);

			    if (user == null) {
			        return "redirect:/spr/form"; // Handle missing user session
			    }

			    System.out.println(user.getMail());
			    model.addAttribute("inp", new OtpModel());
			    model.addAttribute("token", token); 
			    
			    return "Signin2";
	
		}
	
		// Handle the form submission and send OTP
	
		@PostMapping("/submit-verification")
		public String sendOtp(@ModelAttribute OtpModel otp, Model model, @RequestParam("token") String token) throws MessagingException {
	
			System.out.println("Received token from form: " + token);
			Formmodel user = service.getUserByToken(token);
			
			 if (user == null) {
			        // Redirect or show error
			        model.addAttribute("error", "Session expired or user not found. Please fill the form again.");
			        System.out.println("no user got in this method");
			        return "redirect:/spr/form"; // or return an error page
			    }
	
			String enteredEmail = user.getMail();
	
			System.out.println(enteredEmail + "000000");
	
			OtpModel otpModel = new OtpModel();
	
			String Verificationmail = otp.getEmail();
	
			// LocalDateTime createdtime=otp.getCreatedAt() ;
			// long OTP_EXPIRATION_TIME = 5 * 60 * 1000;
	
			System.out.println(Verificationmail + "##########");
			otpModel.setEmail(Verificationmail); // otpmodel
	
			String generatedOtp = mailSender.generateOtp();
			System.out.println(generatedOtp);
			otpModel.setOtp(generatedOtp);  // Generate the OTP (you will need a method to generate it)//otpmodel
			otpModel.setCreatedAt(LocalDateTime.now());
	
			List<OtpModel> otpList = otprepo.findByEmail(enteredEmail);
	
			if (otpList != null && !otpList.isEmpty()) {
				OtpModel otpModel1 = otpList.get(0);
	
				if (Verificationmail.equals(enteredEmail)) {
	
					service.Otpsave(otpModel); // otpModel
					mailSender.sendOtpEmail(enteredEmail, generatedOtp);
	
					session.setAttribute("generatedOtp", generatedOtp);
					session.setAttribute("otpSentTime", System.currentTimeMillis());
					return "otpverification";
	
				}
	
			}
	
			// return new ResponseEntity<>("No record found for the given email!",
			// HttpStatus.BAD_REQUEST);
			return "redirect:/spr/sigin";
		}
	
		// Import necessary classes
	
		@PostMapping("/verifyOtp")
		public String verifyOtp(@RequestParam("otp") String enteredOtp, @ModelAttribute OtpModel otp, Model model,
		HttpSession session) throws MessagingException {
	
			Formmodel user = (Formmodel) session.getAttribute("user");
	
			String email = user.getMail();
			String generatedOtp = (String) session.getAttribute("generatedOtp");
			Long otpSentTime = (Long) session.getAttribute("otpSentTime");
	
			System.out.println(otpSentTime + " 0000000 ");
			System.out.println(System.currentTimeMillis() - otpSentTime);
	
			if (otpSentTime != null && System.currentTimeMillis() - otpSentTime > 60000) {
				model.addAttribute("otpExpired", "Your OTP has expired. Please resend it.");
				
				session.removeAttribute("generatedOtp");
		        session.removeAttribute("otpSentTime");
		        
				return "otpexpired";
			}
	
			List<OtpModel> otpList = otprepo.findByEmail(email);
			if (otpList != null && !otpList.isEmpty()) {
	
				
				
				
				
				OtpModel otpModel = otpList.get(0);
				System.out.println("Entered OTP: " + enteredOtp);
				System.out.println("Stored OTP: " + generatedOtp);
	
				if (enteredOtp.trim().equals(generatedOtp.trim())) {
	
					return "passwordverify";
	
				} else {
	
					System.out.println("WRONG OTP!");
					model.addAttribute("otpError", "WRONG OTP TRY AGAIN.");
					return "otpverification";
				}
				
	
			}
	
			return "redirect:/spr/sigin";
		}
	
		@GetMapping("/resendOtp")
		public String resendOtp(HttpSession session, Model model) throws MessagingException {
	
			Formmodel user = (Formmodel) session.getAttribute("user");
			String email = user.getMail();
	
			String newOtp = mailSender.generateOtp();
			mailSender.sendOtpEmail(email, newOtp);
	
			session.setAttribute("generatedOtp", newOtp);
			session.setAttribute("otpSentTime", System.currentTimeMillis());
	
			model.addAttribute("otpSent", "New OTP has been sent to your email.");
			return "otpverification";
		}
		
		
		
		
		
	
//	@PostMapping("/passwordverify")
//	public String passverification(@RequestParam("pass")String password,@RequestParam("confirmpass") String confirmpassword,@ModelAttribute OtpModel otp,Model model, HttpSession session) {
//		
//		System.out.println(password);
//		System.out.println(confirmpassword);
//		if(password.equals(confirmpassword)) {
//			
//			Formmodel user = (Formmodel) session.getAttribute("user");
//		    String email = user.getMail();
//
//		    // Fetch the actual user from DB
//		    OtpModel existingUser = (OtpModel) otprepo.findByEmail(email); // You need this method in your Formservice
//
//		    if (existingUser != null) {
//		    	
//		        existingUser.setPassword(password); // assuming Formmodel has setPassword method
//		        
//		        service.Otpsave(existingUser); // Save the updated user back to the database
//
//		        return "otpsuccess";
//		        
//		        
//		    } else {
//		        model.addAttribute("otpError", "User not found for the given email.");
//		        return "passwordverify";
//		    }}}
//	
//	
		@PostMapping("/passwordverify")
		public String passverification(@RequestParam("pass") String password,
		                               @RequestParam("confirmpass") String confirmpassword,
		                               Model model, HttpSession session) {

		    // Step 1: Check if passwords match
		    if (!password.equals(confirmpassword)) {
		        model.addAttribute("error", "Passwords do not match.");
		        return "passwordverify";
		    }

		    // Step 2: Get the user from session
		    Formmodel userInSession = (Formmodel) session.getAttribute("user");
		    if (userInSession == null) {
		        model.addAttribute("error", "Session expired or user not found.");
		        return "passwordverify";
		    }

		    // Step 3: Fetch user from database using email
		    List<Formmodel> usersFromDb = service.findAllByMail(userInSession.getMail());
		    if (usersFromDb == null || usersFromDb.isEmpty()) {
		        model.addAttribute("error", "User not found in the system.");
		        return "passwordverify";
		    }

		    // Step 4: Pick the first matching user and update password
		    Formmodel userToUpdate = usersFromDb.get(0);
		    userToUpdate.setPassword(new String(password)); // You can also hash it here
		    Formmodel updatedUser = repo.saveAndFlush(userToUpdate);
		    System.out.println("Saving user: " + userToUpdate.getMail() + " | ID: " + userToUpdate.getId());

		    
		    service.savemethod(userToUpdate);
		    session.setAttribute("user", userToUpdate);
//		    session.removeAttribute("user");
	        session.removeAttribute("generatedOtp");
	        session.removeAttribute("otpSentTime");
		    return "otpsuccess"; // Password successfully updated
		}
}