package com.willy.mail;

import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailApplication implements CommandLineRunner {

	public static void main(String[] args) {
//		SpringApplication.run(MailApplication.class, args);
		backPressureTest();
	}

	public static void backPressureTest() {

		SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

		Flow.Subscriber<String> subscriber = new Flow.Subscriber<String>() {
			private Flow.Subscription subscription;

			@Override
			public void onSubscribe(Flow.Subscription subscription) {
				this.subscription = subscription;
				//向数据发布者请求一个数据
				this.subscription.request(5);
			}

			@Override
			public void onNext(String item) {
				System.out.println("接收到 publisher 发来的消息了：" + item);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.subscription.request(1);
			}

			@Override
			public void onError(Throwable throwable) {
				//出现异常，就会来到这个方法，此时直接取消订阅即可
				this.subscription.cancel();
			}

			@Override
			public void onComplete() {
				//发布者的所有数据都被接收，并且发布者已经关闭
				System.out.println("数据接收完毕");
			}
		};
		publisher.subscribe(subscriber);
		for (int i = 0; i < 500; i++) {
			System.out.println("i--------->" + i);
			publisher.submit("hello:" + i);
		}
		//关闭发布者
		publisher.close();
	}

	@Autowired WMail mail;
	@Override
	public void run(String... args) throws Exception {
//		aa();

		mail.setSender("sender");
		mail.setSendFrom("hank@gmail.com");
		mail.send("subject", Arrays.asList("willy50414z@gmail.com"), "content");

		mail.sendHtmlMail("willy50414z@gmail.com", "subject", "content");

		mail.sendSimpleTextMail("willy50414z@gmail.com", "subject", "content");
		System.exit(2);
	}

//	public void aa() {
//
//		final String username = "willy50414z@gmail.com";
//		final String password = "Google50213!";
//
//		Properties prop = new Properties();
//		prop.put("mail.smtp.host", "smtp.gmail.com");
//		prop.put("mail.smtp.port", "587");
//		prop.put("mail.smtp.auth", "true");
//		prop.put("mail.smtp.starttls.enable", "true"); //TLS
//
//		Session session = Session.getInstance(prop,
//				new javax.mail.Authenticator() {
//					protected PasswordAuthentication getPasswordAuthentication() {
//						return new PasswordAuthentication(username, password);
//					}
//				});
//
//		try {
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("hank@gmail.com"));
//			message.setRecipients(
//					Message.RecipientType.TO,
//					InternetAddress.parse("willy50414z@gmail.com")
//			);
//			message.setSubject("Gmail TLS 测试");
//			message.setText("邮件内容,"
//					+ "\n\n 测试邮件内容");
//
//			Transport.send(message);
//
//			System.out.println("发送成功");
//
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
//	}
}
