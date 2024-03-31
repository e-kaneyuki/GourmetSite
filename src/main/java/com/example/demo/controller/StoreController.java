package com.example.demo.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Store;
import com.example.demo.repository.StoreCrudRepository;

@RestController
public class StoreController {


	@Autowired
	private StoreCrudRepository storeCrudRepository;
	
	@PostMapping("/saveStore")
//	https://qiita.com/yuji38kwmt/items/516d00fb7f0b360bd7c9#%E3%83%A1%E3%82%BD%E3%83%83%E3%83%89%E5%BC%95%E6%95%B0%E3%81%AE%E5%9E%8B%E3%81%8Cmapstring-string
	public Store saveStore(@RequestParam Map<String, String> allParams) throws IOException {
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
		store.setLatitude(allParams.get("緯度"));
		store.setLongitude(allParams.get("経度"));
		;
        // allParamsから店舗情報をセット
        storeCrudRepository.save(store);
        return store;
	}
	
	@PostMapping("/deleteStore")
	public Integer deleteStore(@RequestParam(value="shopName" , required = false) String param) throws IOException{
		Integer results = storeCrudRepository.deleteStore(param);
		return results;
	}

}