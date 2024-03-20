package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Store;
import com.example.demo.form.NameForm;
import com.example.demo.repository.StoreCrudRepository;

@Controller
@RequestMapping("favorites")
public class FavoritesController {

	@Autowired
	StoreCrudRepository repository;
	
	@GetMapping("list")
	public String getFavoritesList(Model model) {
		Iterable<Store> storeList = repository.findAll();
		model.addAttribute("store", storeList);
		return "favorites-list";
	}
	
//	セーブした時は表示ページを変えないが、現状は値が渡せているのか判定するために画面遷移させている。
	@PostMapping("save")
//	https://qiita.com/yuji38kwmt/items/516d00fb7f0b360bd7c9#%E3%83%A1%E3%82%BD%E3%83%83%E3%83%89%E5%BC%95%E6%95%B0%E3%81%AE%E5%9E%8B%E3%81%8Cmapstring-string
	public String saveFavoritesList(NameForm nameForm, @RequestParam Map<String,String> allParams, Model model) {
				model.addAttribute("store", allParams);
				Store store = new Store();
				store.setStoreName(allParams.get("shopName"));
				store.setStoreNameKana(allParams.get("店舗名かな"));
				store.setAddress(allParams.get("店舗住所"));
				store.setBusinessHours(allParams.get("営業時間"));
				store.setCatchphrase(allParams.get("店舗紹介"));
				store.setImageURL(allParams.get("店舗画像"));
				store.setStoreHpURL(allParams.get("店舗URL"));
				store.setAreaCode(allParams.get("中エリアコード"));
				store.setAreaName(allParams.get("中エリア名"));
				repository.save(store);
				System.out.println(allParams);
		return "favorites-list";
	}
}
