package com.example.demo.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleForm {
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
	private LocalDate date;
	@NotNull(message = "タイトルを入力してください。")
	@Size(min = 1,max = 30,message = "タイトルは{min}～{max}文字で入力してください。")
	private String title;
	@NotNull(message = "画像を選択してください。")
	private MultipartFile imageFile;
	private String storedImageFileName;
	@NotNull(message = "ゴミ種別を選択してください。")
	private Integer trashTypeId;
	@NotNull(message = "ゴミ出し労力を選択してください。")
	private Integer effortId;
	private boolean isNew;
}
