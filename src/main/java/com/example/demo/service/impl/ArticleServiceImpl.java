package com.example.demo.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Article;
import com.example.demo.entity.Effort;
import com.example.demo.entity.TrashType;
import com.example.demo.repository.ArticleMapper;
import com.example.demo.repository.EffortMapper;
import com.example.demo.repository.TrashTypeMapper;
import com.example.demo.service.ArticleService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

	private final ArticleMapper articleMapper;
	private final TrashTypeMapper trashTypeMapper;
	private final EffortMapper effortMapper;
	
	@Override
	public List<Article> findAllArticle() {
		return articleMapper.selectAll();
	}

	@Override
	public List<Article> findByMonthArticle(Date startDate, Date endDate) {
		return articleMapper.selectByMonth(startDate, endDate);
	}

	@Override
	public Article findByDateArticle(Date date) {
		return articleMapper.selectByDate(date);
	}

	@Override
	public List<TrashType> findAllTrashtype() {
		return trashTypeMapper.selectAll();
	}

	@Override
	public List<Effort> findAllEffort() {
		return effortMapper.selectAll();
	}

	@Override
	public void insertArticle(Article article) {
		articleMapper.insert(article);
	}

	@Override
	public void updateArticle(Article article) {
		articleMapper.update(article);
	}

	@Override
	public void updateArticleWithoutImageFileName(Article article) {
		articleMapper.updateWithoutImageFileName(article);
	}

	@Override
	public void deleteArticle(Date date) {
		articleMapper.delete(date);
	}

	@Override
	public TrashType findByIdTrashtype(Integer id) {
		return trashTypeMapper.selectById(id);
	}

	@Override
	public Effort findByIdEffort(Integer id) {
		return effortMapper.selectById(id);
	}

}
