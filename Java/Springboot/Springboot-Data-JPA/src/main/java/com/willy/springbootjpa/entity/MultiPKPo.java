package com.willy.springbootjpa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class MultiPKPo {
	@EmbeddedId
	private MultiPKPoPK userFundPK;
	
	//本金
	@Column(precision=18, scale=5)
	private BigDecimal principal;
	
	//利率（5%傳0.05）
	@Column(precision=18, scale=5)
	private BigDecimal rate;
	
	@Column(precision=18, scale=2)
	private BigDecimal interest;
}
