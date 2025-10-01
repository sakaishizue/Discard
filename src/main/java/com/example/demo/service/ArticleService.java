package com.example.demo.service;

import java.util.Date;
import java.util.List;

import com.example.demo.entity.Article;
import com.example.demo.entity.Effort;
import com.example.demo.entity.TrashType;

public interface ArticleService {
	//記事全件検索
	List<Article> findAllArticle();
	//記事範囲検索
	List<Article> findByMonthArticle(Date startDate,Date endDate);
	//記事1件検索
	Article findByDateArticle(Date date);
	//ゴミ種別全検索
	List<TrashType> findAllTrashtype();
	//ゴミ種別1件検索
	TrashType findByIdTrashtype(Integer id);
	//労力全検索
	List<Effort> findAllEffort();
	//労力1件検索
	Effort findByIdEffort(Integer id);
	//記事登録
	void insertArticle(Article article);
	//記事更新
	void updateArticle(Article article);
	//記事更新(画像ファイル名以外)
	void updateArticleWithoutImageFileName(Article article);
	//記事削除
	void deleteArticle(Date date);
}
