package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Authentication;

@Mapper
public interface AuthenticationMapper {
	Authentication selectByUsername(String username);
}
