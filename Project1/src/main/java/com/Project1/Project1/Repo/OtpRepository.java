package com.Project1.Project1.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Project1.Project1.Model.OtpModel;

public interface OtpRepository extends JpaRepository<OtpModel, Long> {
	
	
	List<OtpModel>findByEmail(String email );
	
	}


