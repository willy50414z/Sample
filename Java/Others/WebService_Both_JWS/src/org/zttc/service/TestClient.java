package org.zttc.service;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.willy.server.IMyService;

public class TestClient {
public static void main(String[] args) {
	try {
        //�ЫسX��wsdl�A�Ȧa�}��url
        URL url = new URL("http://localhost:8888/ns?wsdl");
        //�q�LQname�����A�Ȫ�����H��
        QName sname = new QName("http://server.willy.com/", "MyServiceImplService");
        //�ЫتA��
        Service service = Service.create(url,sname);
        //��{���f
        IMyService ms = service.getPort(IMyService.class);
        System.out.println(ms.add(12,33));
        //�H�W�A�Ȧ����D�A�̵M�̿��IMyServie���f
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
