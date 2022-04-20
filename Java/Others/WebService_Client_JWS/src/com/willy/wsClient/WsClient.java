package com.willy.wsClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class WsClient {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			java.net.InetAddress i = java.net.InetAddress.getLocalHost();
			System.out.println(i); // name and IP address
			System.out.println(i.getHostName()); // name
			System.out.println(i.getHostAddress()); // IP address only

			 //�ЫسX��wsdl�A�Ȧa�}��url
			 URL url = new URL("http://10.32.85.100:8888/ns?wsdl");
			 //�q�LQname�����A�Ȫ�����H��
			 QName sname = new QName("http://server.willy.com/", "MyServiceImplService");
			// QName port_name = new QName("http://server.willy.com","MyServiceImplPort");
//			 //�ЫتA��
			 Service service = Service.create(url,sname);
//			 
//			 //��{���f
//			 IMyService ms = service.getPort(IMyService.class);
//			 System.out.println(ms.add(12,33));
//			 �H�W�A�Ȧ����D�A�̵M�̿��IMyServie���f
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class bb {
	public String aa() {
		String aa = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println(aa);
		String codeing = "UTF-8";
		BufferedWriter fw = null;
		try {
			File file = new File(aa + "bb.txt");
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), codeing)); // ���I�s�X�榡�A�H�KŪ���ɤ���r�Ų��`
			fw.append("UTF-8").append("UTF-8").append("UTF-8").append("UTF-8");
			fw.flush(); // �����g�J�w�s�������e
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return aa;
		}
	}
}