package com.test.willy;

public class calledClass implements calledClassInter{
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	private Car car;
	
	public void test() {
		this.car.test();
	}
}
