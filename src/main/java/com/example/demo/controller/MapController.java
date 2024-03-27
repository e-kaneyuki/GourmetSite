package com.example.demo.controller;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//最終的にはコントローラー化せずDIとする？
//現状は動作確認のためコントローラー化
@Component
public class MapController {
//	YOLPで位置情報を取得
//　OpenStreatMapで位置情報を元に地図表記させる
	

	public String[] doGetLatLng(String serchWord) throws IOException {
		
		//APIKEYは環境変数に設定
		//https://qiita.com/oyaken0717/items/f040659bb77927e9e081
		String apiKey = System.getenv("OPEN_YOLP_API_KEY");
//		サンプルURL
//		String serchWord = "アーキテクチャcafe&bar 棲家";
		String url = "https://map.yahooapis.jp/search/local/V1/localSearch?appid="+apiKey+"&query="+ serchWord +"&output=json";

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		       .url(url)
		       .build();
		Response response = client.newCall(request).execute(); 
		String jsonStr = response.body().string();
//		System.out.println(jsonStr);
		ObjectMapper mapper = new ObjectMapper();
		// JSON文字列をJsonNodeオブジェクトにパース
		JsonNode rootNode = mapper.readTree(jsonStr);
//		System.out.println(rootNode);
		
		
		String[] geometryStr  = null;
		if (rootNode.path("Feature").get(0) != null) {
			JsonNode geometryNode = rootNode.path("Feature").get(0).path("Geometry").get("Coordinates");
			
			if (geometryNode != null) {
				geometryStr = geometryNode.toString().replace("\"","").split(",");
			} else {
				geometryStr = new String[] {"139.7576692","35.6802117"};
			}
		} else {
			geometryStr  = new String[] {"139.7576692","35.6802117"};
		}

		
//		System.out.println("mapNode:"+ geometryStr);
//		String geometryStr = geometryNode.toString();
		
		
//		model.addAttribute(, geometryNode)
		return geometryStr;
	}

}
