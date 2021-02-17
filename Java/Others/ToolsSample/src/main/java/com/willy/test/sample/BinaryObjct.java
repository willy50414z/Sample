package com.willy.test.sample;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class BinaryObjct implements Serializable {
	private static final long serialVersionUID = 1L;
	private int byteArLength;
	private byte[] byteAr;

	public void setByteAr(byte[] byteAr) {
		byte[] newByteAr = new byte[byteAr.length];
		for (int i = 0; i < byteAr.length; i++) {
			newByteAr[i] = byteAr[i];
		}
		this.byteAr = newByteAr;
	}

	public BinaryObjct(int byteArLength, byte[] byteAr) {
		super();
		this.byteArLength = byteArLength;
		setByteAr(byteAr);
	}
}
