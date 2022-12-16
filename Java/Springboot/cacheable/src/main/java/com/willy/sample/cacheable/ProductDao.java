package com.willy.sample.cacheable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {
  private List<ProductPO> productList = new ArrayList<>(
      Arrays.asList(new ProductPO(0, "pencil", new BigDecimal("10"), new Date())
      ,new ProductPO(1, "perfume", new BigDecimal("20"), new Date())
      ,new ProductPO(2, "pepper", new BigDecimal("30"), new Date())
      ,new ProductPO(3, "glasses", new BigDecimal("40"), new Date())
      ,new ProductPO(4, "coke", new BigDecimal("50"), new Date())
      ,new ProductPO(5, "sprite", new BigDecimal("60"), new Date())
      ,new ProductPO(6, "computer", new BigDecimal("70"), new Date())
      ,new ProductPO(7, "plugin", new BigDecimal("80"), new Date())
      )
  );
  public List<ProductPO> findAll(){
    return productList;
  }

  @Cacheable(value="product", key="{#id, #date}")
  public List<ProductPO> findByIdCache(int id, Date date) {
    System.out.println("findById get value without cache");
    return productList.stream()
        .filter(p -> p.getId() == id)
        .collect(Collectors.toList());
  }

  @Cacheable(value="product", key="#root.methodName + '[' + #id + ']'")
  public List<ProductPO> findByIdCacheSpel(int id) {
    //cache key will like 'findByIdCacheSpel[1]'
    System.out.println("findById get value without cache");
      return productList.stream()
          .filter(p -> p.getId() == id)
          .collect(Collectors.toList());
  }

  @Cacheable(value="product", key="'findByIdCacheSpel[' + #id + ']'")
  public List<ProductPO> findByIdCacheSpel(int id, Date date) {
    //try get value from findByIdCacheSpel(int id)
    System.out.println("findById get value without cache");
    return productList.stream()
        .filter(p -> p.getId() == id)
        .collect(Collectors.toList());
  }

  @Cacheable(value="product", keyGenerator ="cacheKeyGenerator")
  public List<ProductPO> findByIdCacheSelfDef(int id) {
    //cache key will like 'findByIdCacheSpel[1]'
    System.out.println("findById get value without cache");
    return productList.stream()
        .filter(p -> p.getId() == id)
        .collect(Collectors.toList());
  }

  @CacheEvict(value = "product", allEntries=true)
  public void clearAll() {
    System.out.println("clear all product caches");
  }
}
