package com.Project1.Project1.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Project1.Project1.Model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
