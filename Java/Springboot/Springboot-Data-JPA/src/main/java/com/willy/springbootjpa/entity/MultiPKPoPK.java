package com.willy.springbootjpa.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class MultiPKPoPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer userId;

	/**
	 * 日期，格式yyyy-MM-dd
	 */
	private String date;

	public MultiPKPoPK() {
	}

	public MultiPKPoPK(Integer userId, String date) {
		this.userId = userId;
		this.date = date;
	}
}
