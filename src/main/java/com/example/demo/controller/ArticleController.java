package com.example.demo.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
        // 月初、月末を取得
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date firstDate = Date.from(firstDayOfMonth.atStartOfDay(defaultZoneId).toInstant());
        Date lastDate = Date.from(lastDayOfMonth.atStartOfDay(defaultZoneId).toInstant());
        
        //　article取得
        List<Article> articles = articleService.findByMonthArticle(firstDate, lastDate);
        model.addAttribute("articles",articles);
		return "discard/main";
	}
	
	@GetMapping("/toSave")
//	public String toSave(@ModelAttribute ArticleForm form,RedirectAttributes attributes) {
	public String toSave(Model model,RedirectAttributes attributes) {
		//本日分の記事の存在チェック
       if (ArticleControllerHelper.isExist(articleService)) {
    	   	attributes.addFlashAttribute("message","本日は既に記事が登録されています。");
    	   	return "redirect:/discard";
       }else {
        	//helperクラスでform情報を編集する
     		model.addAttribute("articleForm",ArticleControllerHelper.editSaveForm());
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
    		return "discard/form";
       }
	}

	@PostMapping("/saveArticle")
	@Transactional
	public String saveArticle(@Validated ArticleForm form,BindingResult result,RedirectAttributes attributes,Model model) {
		if (result.hasErrors()) {
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
			return "discard/form";
		}
		
		if (form.getImageFile().isEmpty()) {
			result.rejectValue("imageFile", "error.imageFile", "ファイルが選択されていません。");
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
			return "discard/form";
		}
        try {
            String storedFileName = ArticleControllerHelper.storeFile(form.getImageFile());
            articleService.insertArticle(ArticleControllerHelper.convertArticle(articleService,form,storedFileName));
            attributes.addFlashAttribute("successMessage", "登録に成功しました。");
    		return "redirect:/discard";
        } catch (RuntimeException e) {
        	e.printStackTrace();
     		model.addAttribute("trashTypes",articleService.findAllTrashtype());
     		model.addAttribute("efforts",articleService.findAllEffort());
			result.rejectValue("imageFile", "error.imageFile", "登録に失敗しました。");
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
