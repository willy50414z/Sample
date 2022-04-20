package com.willy.model;

public class People {
String name;
int weight;
int height;

public People() {
	super();
}
public People(String name, int weight, int height) {
	super();
	this.name = name;
	this.weight = weight;
	this.height = height;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getWeight() {
	return weight;
}
public void setWeight(int weight) {
	this.weight = weight;
}
public int getHeight() {
	return height;
}
public void setHeight(int height) {
	this.height = height;
}

}
