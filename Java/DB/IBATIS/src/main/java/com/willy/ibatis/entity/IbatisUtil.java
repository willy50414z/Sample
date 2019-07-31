package com.willy.ibatis.entity;

import java.io.IOException;
import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class IbatisUtil {
	private static SqlMapClient client = null;  
    static{  
        try {  
            Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");  
            client = SqlMapClientBuilder.buildSqlMapClient(reader);  
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    public static SqlMapClient getClient() {  
        return client;  
    }  
}
