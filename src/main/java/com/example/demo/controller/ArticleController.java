package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Article;
import com.example.demo.form.ArticleForm;
import com.example.demo.helper.ArticleControllerHelper;
import com.example.demo.service.ArticleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/discard")
@RequiredArgsConstructor
public class ArticleController {
	
	private final ArticleService articleService;
	
	@GetMapping
	public String initialView(Model model) {
        // 現在日付を取得
        LocalDate today = LocalDate.now();
        //　article取得
        List<Article> articles = articleService.findByMonthArticle(ArticleControllerHelper.getMonthFirstDate(today), ArticleControllerHelper.getMonthLastDate(today));
        model.addAttribute("articles",articles);
        //カレンダーリンク用データ取得
        model.addAttribute("LinksJson",ArticleControllerHelper.getLinksJson(articleService,today));
        model.addAttribute("today",today);
        return "discard/main";
	}
	
	//指定日付の記事表示（１件と１か月分）
	@GetMapping("/search/{dateString}")
	public String searchView(@PathVariable String dateString, Model model) {
		if (dateString.length() == 7) {
			ArticleControllerHelper.monthArticleView(articleService,dateString,model);
		} else {
			ArticleControllerHelper.singleArticleView(articleService,dateString,model);
		}
        return "discard/main";
	}

	@GetMapping("/toSave")
	public String toSave(Model model,RedirectAttributes attributes) {
		//本日分の記事の存在チェック
		LocalDate date = LocalDate.now();
       if (ArticleControllerHelper.isExist(articleService,date)) {
    	   	attributes.addFlashAttribute("message","本日は既に記事が登録されています。");
    	   	return "redirect:/discard";
       }else {
        	//helperクラスでform情報を編集する
     		model.addAttribute("articleForm",ArticleControllerHelper.editSaveForm(date));
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
    		return "discard/form";
       }
	}

	@PostMapping("/saveArticle")
//	@Transactional
	public String saveArticle(@Validated ArticleForm form,BindingResult result,RedirectAttributes attributes,Model model) {
		if (result.hasErrors()) {
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
			return "discard/form";
		}
		//イメージファイル未指定の場合
		if (form.getImageFile().isEmpty() && form.isNew()) {
			result.rejectValue("imageFile", "error.imageFile", "ファイルが選択されていません。");
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
			return "discard/form";
		}
		//画像ファイル以外の場合
		String contentType = form.getImageFile().getContentType();
		if (contentType != null && !contentType.startsWith("image/")) {
			result.rejectValue("imageFile", "error.imageFile", "画像ファイル以外が選択されています。");
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
			return "discard/form";
		}
		try {
            if (form.isNew()) {
                String storedFileName = ArticleControllerHelper.storeFile(form.getImageFile());
            	articleService.insertArticle(ArticleControllerHelper.convertArticle(articleService,form,storedFileName));
            } else {
            	if (form.getImageFile().isEmpty()) {
                	articleService.updateArticleWithoutImageFileName(ArticleControllerHelper.convertArticle(articleService, form));
            	} else {
                    String storedFileName = ArticleControllerHelper.storeFile(form.getImageFile());
            		articleService.updateArticle(ArticleControllerHelper.convertArticle(articleService,form,storedFileName));
            	}
            }
            if (form.isNew()) {
            	attributes.addFlashAttribute("successMessage", "記事登録に成功しました。");
            } else {
            	attributes.addFlashAttribute("successMessage", "記事更新に成功しました。");
            }
        	return "redirect:/discard";
        } catch (RuntimeException e) {
        	e.printStackTrace();
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
			result.rejectValue("imageFile", "error.imageFile", "登録または更新に失敗しました。");
            return "discard/form";
        }
	}

	@GetMapping("/delete/{dateString}")
	public String deleteArticle(@PathVariable String dateString,RedirectAttributes attributes) {
		try {
			ArticleControllerHelper.deleteArticle(articleService,dateString);
	        attributes.addFlashAttribute("successMessage", "記事削除に成功しました。");			
		} catch (RuntimeException e) {
	       	e.printStackTrace();
	        attributes.addFlashAttribute("errorMessage", "記事削除に失敗しました。");			
		}
		return "redirect:/discard";
	}

	@GetMapping("/update/{dateString}")
	public String toUpdate(@PathVariable String dateString,Model model,RedirectAttributes attributes) {
		try {
			model.addAttribute("articleForm", ArticleControllerHelper.editUpdateForm(articleService,dateString));
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
     		return "discard/form";
		} catch (RuntimeException e) {
	       	e.printStackTrace();
	        attributes.addFlashAttribute("errorMessage", "更新フォーム編集に失敗しました。");			
			return "redirect:/discard";
		}
	}
}
