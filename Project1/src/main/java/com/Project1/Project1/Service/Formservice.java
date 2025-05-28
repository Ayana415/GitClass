package com.Project1.Project1.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project1.Project1.Model.Course;
import com.Project1.Project1.Model.CourseSignup;
import com.Project1.Project1.Model.Formmodel;
import com.Project1.Project1.Model.OtpModel;
import com.Project1.Project1.Repo.CourseRepo;
import com.Project1.Project1.Repo.CourseRepository;
import com.Project1.Project1.Repo.CourseSignupRepository;
//import com.Project1.Project1.Model.OtpverificationModel;
import com.Project1.Project1.Repo.Forminterface;
import com.Project1.Project1.Repo.OtpRepository;

import jakarta.transaction.Transactional;

//import com.Project1.Project1.Repo.OtpverifierRepo;

@Service
public class Formservice {
	@Autowired 
	Forminterface repo;
	
	@Autowired 
	OtpRepository otprepo;
	
	@Autowired
	CourseRepository courseRepo;

//	@Autowired
//	CourseSignupRepository courseSignupRepo;

	@Transactional
	public void savemethod(Formmodel model) {
		
		repo.save(model);
		  
	}
	
	public List<Formmodel> findByMailAndPassword(String mail, String password) {
	    return repo.findByMailAndPassword(mail, password);
	}
	public Formmodel getUserByToken(String token) {
		
	    if (token == null || token.trim().isEmpty()) return null;

	    System.out.println("Finding by token: [" + token.trim() + "]");
	    return repo.findByToken(token.trim());
	}
	
	public void Otpsave(OtpModel otp) {
			
		otprepo.save(otp);
		
	}
	
	public List<Formmodel> findAllByMail(String email) {
	    return repo.findAllByMail(email);
	}
	
	public List<Course> getAllCourses() {
	    return courseRepo.findAll();
	}
	


//	public void signupCourse(Formmodel user, Long courseId) {
//	    Course course = courseRepo.findById(courseId).orElseThrow();
//	    CourseSignup signup = new CourseSignup();
//	    signup.setUser(user);
//	    signup.setCourse(course);
//	    courseSignupRepo.save(signup);
//	}
//	public List<CourseSignup> getUserCourses(Formmodel user) {
//	    return courseSignupRepo.findByUser(user);
//	}

}
