package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Article;

@Mapper
public interface ArticleMapper {
	//全件取得
	List<Article> selectAll(); 
	//特定日付で取得
	Article selectByDate(@Param("date") Date date);
	//特定期間（月）で取得
	List<Article> selectByMonth(@Param("startdate") Date startDate,@Param("enddate") Date endDate); 
	//登録
	void insert(Article article);
	//更新
	void update(Article artilce);
	//削除
	void delete(@Param("date") Date date);
}
