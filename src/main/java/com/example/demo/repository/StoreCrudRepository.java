package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Store;

public interface StoreCrudRepository extends CrudRepository<Store, Integer> {

}
