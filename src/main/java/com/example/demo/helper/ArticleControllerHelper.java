package com.example.demo.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Article;
import com.example.demo.form.ArticleForm;
import com.example.demo.service.ArticleService;

public class ArticleControllerHelper {
	//本日で時刻ゼロのDateを取得
	public static Date getDate() {
       Date originalDate = new Date();
       LocalDate localDate = originalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
       Date dateWithZeroTime = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
       return dateWithZeroTime;
	}
	
	//同日記事の存在確認
	public static boolean isExist(ArticleService articleService) {
		if (articleService.findByDateArticle(getDate()) == null) {
			return false;
		}else {
			return true;
		}
	}
	
	//新規登録時の登録フォームを編集
	public static ArticleForm editSaveForm() {
		ArticleForm form = new ArticleForm();
		form.setDate(getDate());
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       try {
    	   	Date date = sdf.parse(dateString);
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
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ArticleForm form = new ArticleForm();
        try {
    	    Article article = service.findByDateArticle(sdf.parse(dateString));
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
//	public static void main(String[] args) {
//		System.out.println(addImagePath("image.jpg"));
//	}
}

