//package com.Project1.Project1.Model;
//
//import jakarta.persistence.*;

//@Entity
//public class CourseSignup {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id") // foreign key to Formmodel
//    private Formmodel user;
//
//    @ManyToOne
//    @JoinColumn(name = "course_id") // foreign key to Course
//    private Course course;
//
//    // === Getters and Setters ===
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Formmodel getUser() {
//        return user;
//    }
//
//    public void setUser(Formmodel user) {
//        this.user = user;
//    }
//
//    public Course getCourse() {
//        return course;
//    }
//
//    public void setCourse(Course course) {
//        this.course = course;
//    }
//}

package com.Project1.Project1.Model;

import jakarta.persistence.*;

@Entity
public class CourseSignup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    private String userName;
    private String userEmail;
    private double fee;
    private String duration;
  
    public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	private String courseName;

    // === Getters and Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
