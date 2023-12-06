package com.willy.sample.cacheable;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class CacheableApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(CacheableApplication.class, args);
  }

  @Autowired
  private ProductDao productDao;

  @Override
  public void run(String... args) throws Exception {
    //功能測試
    productDao.findAll().forEach(System.out::println);

    System.out.println("---單測試cache---");
    //單測試cache - 第一次撈，執行方法並放入cache
    productDao.findByIdCache(1, null).forEach(System.out::println);
    //單測試cache - 第二次撈，不執行方法並從cache取值
    productDao.findByIdCache(1, null).forEach(System.out::println);

//    System.out.println("---測試cache with spel---");
//    productDao.findByIdCacheSpel(1).forEach(System.out::println);
//    //蹭上一行方法執行時put進cache的value，證明上一個方法自動產生的key符合預期
//    productDao.findByIdCacheSpel(1, new Date()).forEach(System.out::println);

//    System.out.println("---測試cache with self def cache generator---");
//    productDao.findByIdCacheSelfDef(1).forEach(System.out::println);
//    productDao.findByIdCacheSelfDef(1).forEach(System.out::println);

//    System.out.println("---測試清除cache---");
//    //應該還留有cache
//    productDao.findByIdCache(1, null).forEach(System.out::println);
//    //清除cache
//    productDao.clearAll();
//    //需要重撈
//    productDao.findByIdCache(1, null).forEach(System.out::println);
  }
}
