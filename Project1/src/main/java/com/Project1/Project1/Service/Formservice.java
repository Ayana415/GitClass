package com.Project1.Project1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project1.Project1.Model.Formmodel;
import com.Project1.Project1.Repo.Forminterface;

@Service
public class Formservice {
	@Autowired 
	Forminterface repo;
	
	public void savemethod(Formmodel model) {
		repo.save(model);
	}

}
