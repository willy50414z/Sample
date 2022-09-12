package com.willy.sample.cacheable;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class CacheKeyGenerator implements KeyGenerator {

  @Override
  public Object generate(Object target, Method method, Object... params) {
    StringBuffer sb = new StringBuffer(method.getName());
    for(Object param : params) {
      sb.append(param == null ? "null" : param.toString()).append("-");
    }
    sb.setLength(sb.length()-1);
    return sb.toString();
  }
}
