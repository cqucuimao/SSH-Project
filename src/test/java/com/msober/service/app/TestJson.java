package com.msober.service.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.yuqincar.domain.order.Address;
import com.yuqincar.domain.order.ChargeModeEnum;

class Dog {
	private Long id;
	private String name;
	private int age;
	private String address = "";
	private BigDecimal num = null;
	private BigDecimal num2 = new BigDecimal(10);

	private boolean flag;
	private float money;
	private Date now = null;
	
	public ChargeModeEnum chargeMode = ChargeModeEnum.MILE;

	
	private List<Dog> dogs = new ArrayList();
	
	private Address to = new Address();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public BigDecimal getNum() {
		return num;
	}
	public void setNum(BigDecimal num) {
		this.num = num;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getNow() {
		return now;
	}
	public void setNow(Date now) {
		this.now = now;
	}
	public BigDecimal getNum2() {
		return num2;
	}
	public void setNum2(BigDecimal num2) {
		this.num2 = num2;
	}
	public Address getTo() {
		return to;
	}
	public void setTo(Address to) {
		this.to = to;
	}
	public List<Dog> getDogs() {
		return dogs;
	}
	public void setDogs(List<Dog> dogs) {
		this.dogs = dogs;
	}
	public ChargeModeEnum getChargeMode() {
		return chargeMode;
	}
	public void setChargeMode(ChargeModeEnum chargeMode) {
		this.chargeMode = chargeMode;
	}
	
	
	
}

public class TestJson {
	public static void main(String[] args) {
		Dog dog = new Dog();
		dog.setName("代码狗");
		String json = JSON.toJSONString(dog);
		System.out.println(json);
		
		Gson gson = new Gson();
		String json2 = gson.toJson(dog);
		System.out.println(json2);
	}
}
