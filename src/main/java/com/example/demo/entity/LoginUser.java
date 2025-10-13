package com.example.demo.entity;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;

public class LoginUser extends User {
	
	private String displayname;
	
	public LoginUser(String username,String password,String displayname) {
		super(username,password,Collections.emptyList());
		this.displayname = displayname;
	}
	
	public String getDisplayname() {
		return displayname;
	}
}
