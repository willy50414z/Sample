package com.willy.sample.cacheable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPO implements Serializable {
  private int id;
  private String productName;
  private BigDecimal price;
  private Date createDate;
}
