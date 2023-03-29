package com.willy.springboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "data")
public class ConfigData {
  private String env;
  private UserInfo user;
}
