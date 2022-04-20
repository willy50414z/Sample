package com.learnWeb.Bean;

public class Employee {
	
	public Employee() {
		super();
	}
	public String getName() {
		if(this.name==null)
			this.name="nullName";
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
	public String getGender() {
		if(this.gender==null)
			this.gender="nullGender";
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	private String name;
	private int age;
	private String gender;
	public void show() {
		System.out.println("名字: "+this.name+"\r\n性別 : "+this.gender+"\r\n 年齡 : "+this.age);
	}
}
