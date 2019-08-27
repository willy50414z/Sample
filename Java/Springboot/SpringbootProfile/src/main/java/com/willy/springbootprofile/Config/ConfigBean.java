package com.willy.springbootprofile.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
//@PropertySource({"classpath:application.yml"})//註冊來源設定檔
@ConfigurationProperties(prefix = "willy.var")
public class ConfigBean {
	@Override
	public String toString() {
		return "ConfigBean [key=" + key + ", profile=" + profile + ", profileFromValue=" + profileFromValue
				+ ", keyFromValue=" + keyFromValue + ", normal=" + normal + ", systemPropertiesName="
				+ systemPropertiesName + ", randomNumber=" + randomNumber 
				+ ", resourceFile=" + resourceFile + ", testUrl=" + testUrl + "]";
	}

	//自動注入屬性,需搭配@ConfigurationProperties及Setter
	private int key;
	private String profile;
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public void setKey(int key) {
		this.key = key;
	}

	// 注入指定路徑參數
	@Value("${willy.var.profile}")
	private String profileFromValue;
	@Value("${willy.var.key}")
	private int keyFromValue;

	// 注入普通字符串
	@Value("normal")
	private String normal;

	// 注入操作系统属性
	@Value("#{systemProperties['os.name']}")
	private String systemPropertiesName;

	// 注入表达式结果
	@Value("#{ T(java.lang.Math).random() * 100.0 }")
	private double randomNumber;

	// 注入文件资源
	@Value("classpath:config.txt")
	private Resource resourceFile; 

	// 注入URL资源
	@Value("http://www.baidu.com")
	private Resource testUrl; 

}
