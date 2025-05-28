package com.Project1.Project1.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Project1.Project1.Model.CourseSignup;
import com.Project1.Project1.Model.Formmodel;

public interface CourseSignupRepository extends JpaRepository<CourseSignup, Long> {
	
//    List<CourseSignup> findByUser(Formmodel user);
	List<CourseSignup> findByUserEmail(String userEmail);

}
