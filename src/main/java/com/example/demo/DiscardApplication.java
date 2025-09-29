package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class DiscardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscardApplication.class, args);
//		SpringApplication.run(DiscardApplication.class, args)
//		.getBean(DiscardApplication.class).exe();
	}
	
//	private final ArticleMapper mapper;
//	private final TrashTypeMapper tmapper;
//	private final EffortMapper emapper;
//	
//	public void exe() {
//		System.out.println("==全件検索==");
//		for (Article row : mapper.selectAll()) {
//			System.out.println(row);
//		};
//		System.out.println("==1件検索==");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 日付フォーマットを定義
//		Date date;
//		try {
//			date = sdf.parse("2025-09-11");
//			System.out.println(mapper.selectByDate(date));
//		} catch (ParseException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		};
//		System.out.println("==範囲検索==");
//		try {
//			Date startdate = sdf.parse("2025-09-01");
//			Date enddate = sdf.parse("2025-09-30");
//			for (Article row : mapper.selectByMonth(startdate,enddate)) {
//				System.out.println(row);
//			};
//		} catch (ParseException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		};
//		System.out.println("==ゴミタイプ全件検索==");
//		for (TrashType row : tmapper.selectAll()) {
//			System.out.println(row);
//		};
//		System.out.println("==労力全件検索==");
//		for (Effort row : emapper.selectAll()) {
//			System.out.println(row);
//		};
//	}
}
