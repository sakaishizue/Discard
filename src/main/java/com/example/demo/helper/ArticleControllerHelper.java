package com.example.demo.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Article;
import com.example.demo.form.ArticleForm;
import com.example.demo.service.ArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArticleControllerHelper {
	//本日で時刻ゼロのDateを取得
//	public static Date getDate() {
//       Date originalDate = new Date();
//       LocalDate localDate = originalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//       Date dateWithZeroTime = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//       return dateWithZeroTime;
//	}
	//dateStringをLocalDateに変換
	public static LocalDate parseLocalDate(String dateString) {
       	try {
    	   	LocalDate date = LocalDate.parse(dateString);
    	   	return date;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
	}

	//月初を取得
	public static LocalDate getMonthFirstDate(LocalDate date) {
        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
//        ZoneId defaultZoneId = ZoneId.systemDefault();
//        Date firstDate = Date.from(firstDayOfMonth.atStartOfDay(defaultZoneId).toInstant());
//        return firstDate;
        return firstDayOfMonth;
	}
	
	//月末を取得
	public static LocalDate getMonthLastDate(LocalDate date) {
        LocalDate lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
//        ZoneId defaultZoneId = ZoneId.systemDefault();
//        Date lastDate = Date.from(lastDayOfMonth.atStartOfDay(defaultZoneId).toInstant());
//        return lastDate;
        return lastDayOfMonth;
	}
	
	//同日記事の存在確認
	public static boolean isExist(ArticleService articleService,LocalDate date) {
		if (articleService.findByDateArticle(date) == null) {
			return false;
		}else {
			return true;
		}
	}
	
	//新規登録時の登録フォームを編集
	public static ArticleForm editSaveForm(LocalDate date) {
		ArticleForm form = new ArticleForm();
		form.setDate(date);
		form.setEffortId(0);
		form.setTrashTypeId(0);
		form.setNew(true);
		return form;
	}

	//画像ファイルを格納
	public static String storeFile(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedFilename = UUID.randomUUID().toString() + extension;
        Path targetLocation = Paths.get(System.getenv("IMAGE_UPLOAD_DIR")).toAbsolutePath().normalize().resolve(storedFilename);
        try {
            Files.copy(file.getInputStream(), targetLocation);
        } catch (IOException ex) {
            throw new RuntimeException("ファイルを保存できませんでした。" + originalFilename, ex);
        }
        return storedFilename;
	}

	//フォームからArticleエンティティに変換
	public static Article convertArticle(ArticleService service,ArticleForm form,String storedFileName) {
		Article article = new Article();
		article.setDate(form.getDate());
		article.setTitle(form.getTitle());
		article.setImageFileName(storedFileName);
		article.setTrashType(service.findByIdTrashtype(form.getTrashTypeId()));
		article.setEffort(service.findByIdEffort(form.getEffortId()));
		
		return article;
	}
	public static Article convertArticle(ArticleService service,ArticleForm form) {
		return convertArticle(service, form, "");
	}

	//記事&画像削除
	public static void deleteArticle(ArticleService service,String dateString) {
       try {
    	   	LocalDate date = LocalDate.parse(dateString);
    	   	String fileName = service.findByDateArticle(date).getImageFileName();
    	    Path fileStorageLocation = Paths.get(System.getenv("IMAGE_UPLOAD_DIR")).toAbsolutePath().normalize();
    	    Path targetPath = fileStorageLocation.resolve(fileName).normalize();
    	    Files.deleteIfExists(targetPath);
           	service.deleteArticle(date);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
	
	//更新時の登録フォームを編集
	public static ArticleForm editUpdateForm(ArticleService service,String dateString) {
		ArticleForm form = new ArticleForm();
        try {
    	    Article article = service.findByDateArticle(parseLocalDate(dateString));
        	form.setDate(article.getDate());
        	form.setTitle(article.getTitle());
        	form.setStoredImageFileName(article.getImageFileName());
        	form.setEffortId(article.getEffort().getId());
        	form.setTrashTypeId(article.getTrashType().getId());
        	form.setNew(false);
        	return form;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
	}

	//カレンダーリンク用JSON取得
	public static String getLinksJson(ArticleService service,LocalDate date) {
		List<Article> articles = service.findByMonthArticle(getMonthFirstDate(date), getMonthLastDate(date));
		Map<String,String> articleLinks = new HashMap<>();
		for (Article article : articles) {
			LocalDate localDate = article.getDate();
			articleLinks.put(localDate.toString(),"/discard/search/" + localDate.toString());
		}
        ObjectMapper objectMapper = new ObjectMapper();
        try {
			String linksJson = objectMapper.writeValueAsString(articleLinks);
//			System.out.println(linksJson);
			return linksJson;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	//記事１件の画面編集
	public static void singleArticleView(ArticleService service,String dateString,Model model) {
		LocalDate date = ArticleControllerHelper.parseLocalDate(dateString);
        //　article取得
        Article article = service.findByDateArticle(date);
        List<Article> articles = new ArrayList<Article>();
        articles.add(article);
        model.addAttribute("articles",articles);
        //カレンダーリンク用データ取得
        model.addAttribute("LinksJson",ArticleControllerHelper.getLinksJson(service,date));
        model.addAttribute("today",date);
	}

	//記事１月分の画面編集
	public static void monthArticleView(ArticleService service,String dateString,Model model) {
		//月初を取得
		YearMonth yearMonth = YearMonth.parse(dateString);
		LocalDate date = yearMonth.atDay(1);
        //　article取得
        List<Article> articles = service.findByMonthArticle(ArticleControllerHelper.getMonthFirstDate(date), ArticleControllerHelper.getMonthLastDate(date));
        model.addAttribute("articles",articles);
        //カレンダーリンク用データ取得
        model.addAttribute("LinksJson",ArticleControllerHelper.getLinksJson(service,date));
        model.addAttribute("today",date);
	}
	//	public static void main(String[] args) {
//		System.out.println(addImagePath("image.jpg"));
//	}
}

