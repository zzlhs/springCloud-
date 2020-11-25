package com.zzlhs.springcloud.entities;

import java.io.Serializable;

public class Account implements Serializable{
	
	private Integer id;
	private Double money;
	
	public Account() {
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
	
	
}
