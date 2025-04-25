package com.Project1.Project1.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity

//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email"),  @UniqueConstraint(columnNames = "mail")})

public class OtpModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    private String email;  // Email to which OTP was sent
    private String mail;
    private String otp;    // OTP value
    private LocalDateTime createdAt;  // Time when the OTP was generated
    
//    private String password;
//    private String confirm_Password;
//    
//    public String getConfirm_Password() {
//    	
//		return confirm_Password;
//	}
//
//	public void setConfirm_Password(String confirm_Password) {
//		
//		this.confirm_Password = confirm_Password;
//	}

//	public String getPassword() {
//		
//		return password;
//	}
//
//	public void setPassword(String password) {
//		
//		this.password = password;
//	}

	
    // Default constructor
   

    // Getters and Setters
    public Long getId() {
    	
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

   public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;		
	}

   public String getOtp() {
        return otp;
   }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
