package com.Project1.Project1.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Project1.Project1.Model.Formmodel;

public interface Forminterface extends JpaRepository<Formmodel,Integer> {
	
	List<Formmodel> findAllByMail(String mail);
	
	Formmodel findByToken(String token);
	
	List<Formmodel> findByMailAndPassword(String mail, String password);

}

