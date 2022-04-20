package org.zttc.service;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.willy.server.IMyService;

public class TestClient {
public static void main(String[] args) {
	try {
        //創建訪問wsdl服務地址的url
        URL url = new URL("http://localhost:8888/ns?wsdl");
        //通過Qname指明服務的具體信息
        QName sname = new QName("http://server.willy.com/", "MyServiceImplService");
        //創建服務
        Service service = Service.create(url,sname);
        //實現接口
        IMyService ms = service.getPort(IMyService.class);
        System.out.println(ms.add(12,33));
        //以上服務有問題，依然依賴於IMyServie接口
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
