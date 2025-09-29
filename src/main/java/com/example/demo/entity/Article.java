package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
	private Date date;
	private String title;
	private String imageFileName;
	private TrashType trashType;
	private Effort effort;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
