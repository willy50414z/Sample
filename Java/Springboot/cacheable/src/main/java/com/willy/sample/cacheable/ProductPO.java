package com.willy.sample.cacheable;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPO {
  private int id;
  private String productName;
  private BigDecimal price;
  private Date createDate;
}
