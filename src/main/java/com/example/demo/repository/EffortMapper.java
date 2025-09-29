package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Effort;

@Mapper
public interface EffortMapper {
	//全件取得
	List<Effort> selectAll(); 
	//IDで取得
	Effort selectById(@Param("id") Integer id);
}
