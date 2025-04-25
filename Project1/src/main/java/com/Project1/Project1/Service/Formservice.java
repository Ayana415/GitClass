package com.Project1.Project1.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project1.Project1.Model.Formmodel;
import com.Project1.Project1.Model.OtpModel;

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
	
//	Autowired
//    private PasswordEncoder passwordEncoder;

	@Transactional
	public void savemethod(Formmodel model) {
		
		repo.save(model);
		  
	}
	
	public Formmodel getUserByToken(String token) {
	    return repo.findByToken(token); // Assuming you have a repository
	}
	
	public void Otpsave(OtpModel otp) {
			
		otprepo.save(otp);
		
	}
	
	public List<Formmodel> findAllByMail(String email) {
	    return repo.findAllByMail(email);
	}
	
//	public void savePassword(String password) {
//        String encodedPassword = passwordEncoder.encode(password);  // Encode password
//        // Now save the encoded password to the database
//    }

}
