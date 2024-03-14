package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.NameForm;
import com.example.demo.model.ApiResponse;
import com.example.demo.validator.CheckValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Controller
public class SampleController {

	//Formの初期化による入力フォームの初期値セット
	@ModelAttribute
	public NameForm createForm() {
		return new NameForm(); 
	}
	
	@Autowired
	CheckValidator checkValidator;
	
	@InitBinder("nameForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(checkValidator);
	}
	
	@GetMapping("name")
	public String getName(Model model,NameForm nameForm) throws IOException {
		return "home";
	}
	
	//RequestParamをForm化したい
	@PostMapping("search")
	public String postName(@Validated NameForm nameForm, BindingResult bindingResult, Model model) throws IOException {
		System.out.println(bindingResult);

		if(bindingResult.hasErrors()) {
			nameForm = setForm(nameForm);
			model.addAttribute("zero","入力内容を確認してください。");			
			return getName(model,nameForm);
		}
		
		//APIKEYは環境変数に設定
		//https://qiita.com/oyaken0717/items/f040659bb77927e9e081
		String apiKey = System.getenv("OPEN_API_KEY");
	
		//Formで入力された内容とapiキーを渡すことで検索条件を含むリクエストurlとサービスエリアを返すためのリクエストurl作成
		HashMap<String, String> reqestUrlMap = createRequestUrl(nameForm, apiKey);  
		
		//サービスエリアを返すためのurlを取得
		String areaUrlStr = reqestUrlMap.get("areaUrl");
		//検索条件を含むリクエストurlを取得
		String formUrlStr = reqestUrlMap.get("formUrl");
		
		//サービスエリア取得するため、リクエストを実行。
		//サービスエリアをJSON文字列化
		String areaJsonStr = doRequest(areaUrlStr);
		
		//自作のオブジェクト(ApiResponse)にマッピング
		ObjectMapper mapper = new ObjectMapper();
		try {
            ApiResponse response = mapper.readValue(areaJsonStr, ApiResponse.class);
            System.out.println("API Version: " + response);
            model.addAttribute("apiResponse", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		//検索結果取得するため、リクエストを実行。
	    //検索結果をJSON文字列化
		String formJsonStr = doRequest(formUrlStr);
        //Formに入力され得られたJson文字列をJsonNod化
        JsonNode shopsNode = changeJsonNode(formJsonStr);
        
        if(shopsNode.size()!=0) {
        	LinkedHashMap<String, LinkedHashMap<String, String>> shopMap = createMap(shopsNode);
        	model.addAttribute("shopMap",shopMap);
        } else {
        	model.addAttribute("zero", "取得結果はゼロです。");
        }
        nameForm = setForm(nameForm);
		return "list";
	}
	
	//空白の検索条件になしをsetする
	//ただし、検索する時点でなしを入れてはならない
	public NameForm setForm(NameForm nameForm) {
		NameForm form = new NameForm();
		form =nameForm;
		if(nameForm.getAddress().isBlank()) {
			nameForm.setAddress("なし");
		}
		if(nameForm.getKeyword().isBlank()) {
			nameForm.setKeyword("なし");
		}
		return form;
	}

	//urlを渡せばそれを実行し、JSON文字列で返す
	public String doRequest(String doUrl) throws IOException  {
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		       .url(doUrl)
		       .build();
		Response response = client.newCall(request).execute(); 
		String jsonStr = response.body().string();
		return jsonStr;
	}
	
	//Request送ってResponseけ取るまでの処理
	//code値で順序や取得数を設定したい	
	public HashMap<String, String> createRequestUrl(NameForm form, String apiKey) throws IOException {
		String address = form.getAddress();
		String keyword = form.getKeyword();
		String order = form.getOrder();
		OkHttpClient client = new OkHttpClient();

        // URLに検索条件を含める
        String formUrl = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key=" + apiKey +
                     "&address=" + address +
                     "&keyword=" + keyword +
                     "&order=" + order + 
                     "&count=" + 20 +
                     "&format=json";
        
		String areaUrl = "https://webservice.recruit.co.jp/hotpepper/large_area/v1/?key=" + apiKey +
				 "&format=json";
        
		HashMap<String, String> requestUrlMap = new HashMap<String, String>(); 
		requestUrlMap.put("formUrl",formUrl);
		requestUrlMap.put("areaUrl",areaUrl);
		
		return requestUrlMap;
		
	}
	
	// Json文字列をJsonNodeへ変換する処理
	public JsonNode changeJsonNode(String jsonStr) throws JsonMappingException, JsonProcessingException {
    	// ObjectMapperインスタンスの生成
        ObjectMapper mapper = new ObjectMapper();
        // JSON文字列をJsonNodeオブジェクトにパース
        JsonNode rootNode = mapper.readTree(jsonStr);
        // 必要なデータの取得
        JsonNode shopsNode = rootNode.get("results").get("shop");
        System.out.println("取得件数"+shopsNode.size());
        return shopsNode;
	}
	
	//店名：店舗情報のMapを作成
	public LinkedHashMap<String, LinkedHashMap<String, String>> createMap(JsonNode shopsNode) {
		LinkedHashMap<String, LinkedHashMap<String, String>> shopMap = new LinkedHashMap<String,LinkedHashMap<String, String>>();
		 for (int i = 0; i < shopsNode.size(); i++) {	
             JsonNode shopNode = shopsNode.get(i);
             String photoUrl = shopNode.get("photo").get("pc").get("l").asText();
             String name = shopNode.get("name").asText();
             String shopCatch = shopNode.get("catch").asText();
             String shopOpen = shopNode.get("open").asText();
             String shopAddress = shopNode.get("address").asText();
             String shopUrls = shopNode.get("urls").get("pc").asText();

             LinkedHashMap<String, String> shopList = new LinkedHashMap<String, String>();
             
             shopList.put("店舗画像",photoUrl);
             shopList.put("店舗住所",shopAddress);
             shopList.put("店舗紹介",shopCatch);
             shopList.put("営業時間",shopOpen);
             shopList.put("店舗URL",shopUrls);
             
             shopMap.put(name,shopList);
		 }
		return shopMap;
	}
	
}
