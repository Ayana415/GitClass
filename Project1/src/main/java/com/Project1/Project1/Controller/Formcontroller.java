	package com.Project1.Project1.Controller;
	
	import java.time.Duration;

	import java.time.LocalDateTime;
	import java.time.temporal.ChronoUnit;
	import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.Project1.Project1.Model.Course;
import com.Project1.Project1.Model.CourseSignup;
import com.Project1.Project1.Model.Formmodel;
	import com.Project1.Project1.Model.OtpModel;
import com.Project1.Project1.Repo.CourseRepo;
import com.Project1.Project1.Repo.CourseSignupRepository;
import com.Project1.Project1.Repo.Forminterface;
	import com.Project1.Project1.Repo.OtpRepository;
	import com.Project1.Project1.Service.Formservice;
	
	import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
	
	@Controller
	@SessionAttributes("user")
	@RequestMapping("/spr")
	
	public class Formcontroller {
	
		@Autowired
		Formservice service;
		
		@Autowired
		CourseSignupRepository courseSignupRepo;
		
		@Autowired
		CourseRepo courserepo;
		
		@Autowired
		Mailsender mailSender;
	
		@Autowired
		OtpRepository otprepo;
	
		@Autowired
		private HttpSession session;
		
		@Autowired 
		Forminterface repo;
		

		
		@GetMapping("")
			
			public String Index() {
			return "Index";
		}
		
		@GetMapping("/register")
		public String register() {
			return "Register";
		}
		
		@GetMapping("/login")
		public String showLoginForm() {
			
		    return "Login"; 
		    
		}
		

		
//		
//		@PostMapping("/login")
//		public String loginUser(@RequestParam("email") String email,
//		                        @RequestParam("password") String password,
//		                        Model model,
//		                        HttpSession session) {
//
//		    List<Formmodel> users = service.findByMailAndPassword(email, password);
//
//		    if (users.size() != 1) {
//		        model.addAttribute("error", "Invalid email or password.");
//		        return "Login";
//		    }
//
//		    Formmodel user = users.get(0);
//		    session.setAttribute("user", user); 
//		    model.addAttribute("user", user); 
//		    return "BackendDashboard";
//		}
//         

		
		@PostMapping("/login")
		public String loginUser(@RequestParam("email") String email,
		                        @RequestParam("password") String password,
		                        Model model,
		                        HttpSession session) {

		    List<Formmodel> users = service.findByMailAndPassword(email, password);

		    if (users.size() != 1) {
		        model.addAttribute("error", "Invalid email or password.");
		        return "Login";
		    }

		    Formmodel user = users.get(0);
		    session.setAttribute("user", user);

		    // ✅ Redirect to dashboard controller, which prepares course data
		    return "redirect:/spr/dashboard";
		}

		
//		@GetMapping("/dashboard")
//		public String dashboard(Model model, HttpSession session) {
//		    Formmodel user = (Formmodel) session.getAttribute("user");
//		    if (user == null) {
//		        return "redirect:/login";
//		    }
//
//		    List<Course> courses = courserepo.findAll();
//
//		    List<CourseSignup> signedCourses= courseSignupRepo.findByUserEmail(user.getMail());
//
//		    model.addAttribute("courses", courses);
//		    model.addAttribute("signedCourses", signedCourses);
//		    model.addAttribute("user", user);
//
//		    return "BackendDashboard"; // this loads your dashboard.html
//		}

		@GetMapping("/dashboard")
		public String dashboard(Model model, HttpSession session) {
		    Formmodel user = (Formmodel) session.getAttribute("user");
		    if (user == null) {
		        return "redirect:/login";
		    }

		  
		    List<Course> allCourses = courserepo.findAll();
		    List<CourseSignup> signedCourses = courseSignupRepo.findByUserEmail(user.getMail());

		   
		    Set<String> signedCourseNames = signedCourses.stream()
		            .map(CourseSignup::getCourseName)
		            .collect(Collectors.toSet());

		  
		    List<Course> unsignedCourses = allCourses.stream()
		            .filter(course -> !signedCourseNames.contains(course.getCoursename()))
		            .collect(Collectors.toList());

		    
		    model.addAttribute("courses", unsignedCourses);      
		    model.addAttribute("signedCourses", signedCourses);  // Already signed courses
		    model.addAttribute("user", user);

		    return "BackendDashboard"; 
		}

	    

		
		@PostMapping("/signupcourse")
		public String signupCourse(@RequestParam("courseId") Long courseId, HttpSession session) {
		    
		    Formmodel user = (Formmodel) session.getAttribute("user");
		    if (user == null) return "redirect:/login";

		    Course course = courserepo.findById(courseId).orElse(null);

		    if (course != null) {
		        CourseSignup signup = new CourseSignup();
		        signup.setUserName(user.getName());
		        signup.setUserEmail(user.getMail());
		        signup.setCourseName(course.getCoursename());
		        signup.setFee(course.getFee());
		        signup. setDuration(course.getDuration());
		        courseSignupRepo.save(signup);
		    }

		    return "redirect:/spr/dashboard";
		}

		
		@GetMapping("/editprofile")
		public String editprofile(Model model, HttpSession session) {
		    Formmodel user = (Formmodel) session.getAttribute("user");
		    if (user == null) {
		        return "redirect:/spr/login"; // No user in session, redirect to login
		    }
		    model.addAttribute("user", user);
		    return "Editprofile";
		}
		
		@PostMapping("/editprofile")
		public String updateProfile(@ModelAttribute("user") Formmodel updatedUser, HttpSession session, Model model) {
		    Formmodel sessionUser = (Formmodel) session.getAttribute("user");

		    if (sessionUser == null) {
		        return "redirect:/spr/login"; // No user logged in
		    }

		    // Update only the name (keep email and password unchanged)
		    sessionUser.setName(updatedUser.getName());

		    // Save updated user to database
		    service.savemethod(sessionUser);
            session.setAttribute("user", sessionUser);
            model.addAttribute("user", sessionUser);
		    model.addAttribute("success", "Profile updated successfully.");
		    return "Editprofile"; // reloads the page with updated info
		}


		
		@GetMapping("/changepass")
		public String changepass() {
			
		    return "Changepassword"; 
		    
		}
		
		
		@PostMapping("/changepass")
		public String changePassword(@RequestParam("currentPassword") String currentPassword,
		                             @RequestParam("newPassword") String newPassword,
		                             @RequestParam("confirmPassword") String confirmPassword,
		                             HttpSession session,
		                             Model model) {
		    Formmodel user = (Formmodel) session.getAttribute("user");

		    if (user == null) {
		        return "redirect:/spr/login";
		    }

		    if (!user.getPassword().equals(currentPassword)) {
		        model.addAttribute("error", "Current password is incorrect.");
		        return "Changepassword";
		    }

		    if (!newPassword.equals(confirmPassword)) {
		        model.addAttribute("error", "New passwords do not match.");
		        return "Changepassword";
		    }

		    user.setPassword(newPassword);
		    service.savemethod(user);

		    session.invalidate(); // Auto logout

		    return "redirect:/spr/login?msg=Password changed successfully. Please login again.";
		}

		
		@GetMapping("/logout")
		public String logout(HttpServletRequest request, HttpServletResponse response) {
		    // Invalidate the session
		    HttpSession session = request.getSession(false);
		    if (session != null) {
		        session.invalidate();
		    }

		    // Remove cookies
		    Cookie[] cookies = request.getCookies();
		    if (cookies != null) {
		        for (Cookie cookie : cookies) {
		            cookie.setValue("");
		            cookie.setPath("/");
		            cookie.setMaxAge(0); // Expire the cookie
		            response.addCookie(cookie);
		        }
		    }

		    return "redirect:/spr/login";
		}

		
			
		
		@GetMapping("/signinform")
		public String Form(Model model) {
	
			model.addAttribute("user", new Formmodel());
			return "Formscope";
			
	
		}
	
	
		@PostMapping("/send")
	
		public  ResponseEntity<String>  mail( @ModelAttribute Formmodel User,Model model) throws MessagingException {
	

			
//			String token = UUID.randomUUID().toString();
//
//			User.setToken(UUID.randomUUID().toString());

			String token = UUID.randomUUID().toString();
			User.setToken(token);           // ✅ Set token first
			service.savemethod(User); 
			mailSender.simplemail( User);
			System.out.println(User.getMail()); // pass it till here
     		model.addAttribute("user", User); // store in a model
			session.setAttribute("user",  User);//store in session
			return new ResponseEntity<>("Mail sent", HttpStatus.OK);
	
		}
	
		@GetMapping("/sigin")
		public String showVerificationPage1(@RequestParam("token") String token,Model model,HttpSession session) {

			Formmodel user = service.getUserByToken(token);

			    if (user == null) {
			        return "redirect:/spr/signinform"; // Handle missing user session
			    }

			    System.out.println(user.getMail());
			    model.addAttribute("inp", new OtpModel());
			    model.addAttribute("token", token); 
			    
			    return "Signin2";
	
		}
	

		
		@PostMapping("/submit-verification")
		public String sendOtp(@ModelAttribute OtpModel otp, Model model, @RequestParam("token") String token) throws MessagingException {

		    System.out.println("Received token from form: " + token);
		    Formmodel user = service.getUserByToken(token);

//		    if (user == null) {
//		        model.addAttribute("error", "Session expired or user not found. Please fill the form again.");
//		        return "redirect:/spr/signinform";
//		    }

		    String enteredEmail = user.getMail();
		    String verificationEmail = otp.getEmail();

		    System.out.println("Entered Email: " + enteredEmail);
		    System.out.println("Verification Email: " + verificationEmail);

		    if (!verificationEmail.trim().equalsIgnoreCase(enteredEmail.trim())) {
		    	model.addAttribute("error", "Entered email does not match registered email.");
		        model.addAttribute("inp", new OtpModel());
		        model.addAttribute("token", token);
		        return "Signin2";
		        
		    }

		    // Emails match – proceed to generate and send OTP
		    String generatedOtp = mailSender.generateOtp();
		    OtpModel otpModel = new OtpModel();
		    otpModel.setEmail(verificationEmail.trim());
		    otpModel.setOtp(generatedOtp);
		    otpModel.setCreatedAt(LocalDateTime.now());

		    // Save to DB
		    service.Otpsave(otpModel);

		    // Send OTP Email
		    mailSender.sendOtpEmail(enteredEmail, generatedOtp);

		    // Store in session
		    session.setAttribute("generatedOtp", generatedOtp);
		    session.setAttribute("otpSentTime", System.currentTimeMillis());
		    model.addAttribute("token", token);
		    return "otpverification";
		}

	
		
		@PostMapping("/verifyOtp")
		public String verifyOtp(@RequestParam("otp") String enteredOtp, @ModelAttribute OtpModel otp, @RequestParam("token") String token, Model model,
		HttpSession session) throws MessagingException {
	

			
			Formmodel user = service.getUserByToken(token);
			
			System.out.println("User from token: " + user);

			if (user == null) {
			    model.addAttribute("error", "Invalid or expired token. Please try again.");
			    return "redirect:/spr/signinform";
			}
			System.out.println(user);
			
			String email = user.getMail();
			System.out.println(email);
			
			
			String generatedOtp = (String) session.getAttribute("generatedOtp");
			Long otpSentTime = (Long) session.getAttribute("otpSentTime");
	
			System.out.println(otpSentTime + " 0000000 ");
			System.out.println(System.currentTimeMillis() - otpSentTime);
	
			if (otpSentTime != null && System.currentTimeMillis() - otpSentTime > 60000) {
				model.addAttribute("otpExpired", "Your OTP has expired. Please resend it.");
				
				session.removeAttribute("generatedOtp");
		        session.removeAttribute("otpSentTime");
		        model.addAttribute("token", token); 
				return "otpexpired";
			}
	
			List<OtpModel> otpList = otprepo.findByEmail(email);
			if (otpList != null && !otpList.isEmpty()) {
	
				
				
				
				
				OtpModel otpModel = otpList.get(0);
				System.out.println("Entered OTP: " + enteredOtp);
				System.out.println("Stored OTP: " + generatedOtp);
	
				if (enteredOtp.trim().equals(generatedOtp.trim())) {
					 model.addAttribute("token", token);
					return "passwordverify";
	
				} else {
	
					System.out.println("WRONG OTP!");
					model.addAttribute("otpError", "WRONG OTP TRY AGAIN.");
					model.addAttribute("token", token); 
					return "otpverification";
				}
				
	
			}
	
			return "redirect:/spr/sigin?token=" + token;
		}
	
		@GetMapping("/resendOtp")
		public String resendOtp(HttpSession session, Model model, @RequestParam("token") String token) throws MessagingException {
	
			Formmodel user = service.getUserByToken(token);
			String email = user.getMail();
	
			String newOtp = mailSender.generateOtp();
			mailSender.sendOtpEmail(email, newOtp);
	
			session.setAttribute("generatedOtp", newOtp);
			session.setAttribute("otpSentTime", System.currentTimeMillis());
	
			model.addAttribute("otpSent", "New OTP has been sent to your email.");
			model.addAttribute("token", token); 
			return "otpverification";
		}
		
		
		
		
		
	
		@PostMapping("/passwordverify")
		public String passverification(@RequestParam("pass") String password,
		                               @RequestParam("confirmpass") String confirmpassword,
		                               @RequestParam("token") String token,
		                               Model model, HttpSession session) {

		    // Step 1: Check if passwords match
		    if (password == null || confirmpassword == null || !password.equals(confirmpassword)) {
		        model.addAttribute("error", "Passwords do not match.");
		        model.addAttribute("token", token);
		        return "passwordverify";
		    }

		    // Step 2: Get the user using token
		    Formmodel user = service.getUserByToken(token);
		    if (user == null) {
		        model.addAttribute("error", "Invalid session or token. Please start over.");
		        return "redirect:/spr/signinform";
		    }

		    // Step 3: Update password and save
		    user.setPassword(password); // You can hash the password here if needed
		    service.savemethod(user);   // Save using your service

		    // Step 4: Clean session
		    session.removeAttribute("generatedOtp");
		    session.removeAttribute("otpSentTime");
		    session.setAttribute("user", user);
		    model.addAttribute("token", token);
		    return "Login"; // Password successfully updated
		}
}