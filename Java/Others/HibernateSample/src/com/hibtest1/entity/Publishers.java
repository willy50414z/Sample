package com.hibtest1.entity;

public class Publishers {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPublisherId() {
		return PublisherId;
	}
	public void setPublisherId(int publisherId) {
		PublisherId = publisherId;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	private int id;
	private int PublisherId;
	private String bookname;
	private int price;
	private String desc;
	
}
