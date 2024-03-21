package com.example.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Store;

public interface StoreCrudRepository extends CrudRepository<Store, Integer> {

	@Query("SELECT s FROM Store s ORDER BY s.storeNameKana ASC")
	Iterable<Store> getAllNameAsc();
	
	@Query("SELECT s FROM Store s ORDER BY s.areaCode ASC")
	Iterable<Store> getAllAreCodeAsc();
	
}
