package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Article;

@Mapper
public interface ArticleMapper {
	//全件取得
	List<Article> selectAll(); 
	//特定日付で取得
	Article selectByDate(@Param("date") LocalDate date);
	//特定期間（月）で取得
	List<Article> selectByMonth(@Param("startdate") LocalDate startDate,@Param("enddate") LocalDate endDate); 
	//登録
	void insert(Article article);
	//更新
	void update(Article artilce);
	//更新2
	void updateWithoutImageFileName(Article artilce);
	//削除
	void delete(@Param("date") LocalDate date);
}
