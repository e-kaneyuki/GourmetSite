package com.example.demo.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	@Column(name = "storename")
    private String storeName;
	
    @Column(name = "storenamekana")
    private String storeNameKana;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "businesshours")
    private String businessHours;
    
    @Column(name = "catchphrase")
    private String catchphrase;
    
    @Column(name = "imageurl")
    private String imageURL;
    
    @Column(name = "storehpurl")
    private String storeHpURL;
    
    @Column(name = "areacode")
    private String areaCode;
    
    @Column(name = "areaname")
    private String areaName;
    
    //経度
    @Column(name = "longitude")
    private String longitude;
    
    //緯度
    @Column(name = "latitude")
    private String latitude;

}
