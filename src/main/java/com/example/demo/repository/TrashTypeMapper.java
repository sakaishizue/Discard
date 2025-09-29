package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.TrashType;

@Mapper
public interface TrashTypeMapper {
	//全件取得
	List<TrashType> selectAll(); 
	//IDで取得
	TrashType selectById(@Param("id") Integer id);
}
