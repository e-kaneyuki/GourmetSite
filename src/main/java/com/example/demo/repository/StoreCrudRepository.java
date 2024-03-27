package com.example.demo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Store;

import jakarta.transaction.Transactional;

public interface StoreCrudRepository extends CrudRepository<Store, Integer> {

	@Query("SELECT s FROM Store s ORDER BY s.storeNameKana ASC")
	Iterable<Store> getAllNameAsc();
	
	@Query("SELECT s FROM Store s ORDER BY s.areaCode ASC")
	Iterable<Store> getAllAreCodeAsc();
	
	
	//アノテーションについては下記記事参照
	//https://qiita.com/ist-a-ku/items/c20d67140402634cd5db
	@Transactional
	@Modifying
	@Query("DELETE FROM Store WHERE storeName = :storeName ")
	Integer deleteStore(@Param("storeName") String params);
}
