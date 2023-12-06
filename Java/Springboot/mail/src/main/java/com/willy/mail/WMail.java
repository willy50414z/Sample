package com.willy.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class WMail {
	private String sender;
	private String sendFrom;
	@Autowired
	private JavaMailSender mailSender;
	private HashMap<String, Object> templateContextMap;
	private Date sendDate;// 寄件日期
	private List<String> toMailAddrList = new ArrayList<String>();
	private List<String> ccMailAddrList = new ArrayList<String>();
	private List<String> bccMailAddrList = new ArrayList<String>();

	public WMail() {
		super();
	}


	/**
	 * 发送简单文本邮件
	 *
	 * @param to
	 * @param subject
	 * @param content
	 */
	public void sendSimpleTextMail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		message.setFrom(sendFrom);
		mailSender.send(message);
		System.out.println("【文本邮件】成功发送！to={}" + to);
	}

	/**
	 * 发送 HTML 邮件
	 *
	 * @param to
	 * @param subject
	 * @param content
	 * @throws MessagingException
	 */
	public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
		messageHelper.setFrom(sendFrom);
		messageHelper.setTo(to);
		messageHelper.setSubject(subject);
		// true 为 HTML 邮件
		messageHelper.setText(content, true);
		mailSender.send(message);
		System.out.println("【HTML 邮件】成功发送！to={}" + to);
	}

	public void send(String subject, List<String> toMailAddrList, String content)
			throws MessagingException, UnsupportedEncodingException {
		clearRecver();

		this.toMailAddrList = toMailAddrList;

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		setMailHeader(helper);
		helper.setSubject(subject);
		addReceiver(helper);
		setMailContent(helper, content);
		mailSender.send(mimeMessage);
	}

	private void clearRecver() {
		if(this.toMailAddrList != null) {
			this.toMailAddrList.clear();
		}
		if(this.ccMailAddrList != null) {
			this.ccMailAddrList.clear();
		}
		if(this.toMailAddrList != null) {
			this.bccMailAddrList.clear();
		}
	}

	private void setMailHeader(MimeMessageHelper helper) throws MessagingException, UnsupportedEncodingException {
		if (StringUtils.isEmpty(sender)) {
			throw new IllegalArgumentException("未設定寄件人,spring.mail.sender");
		}
		if (StringUtils.isEmpty(sendFrom)) {
			throw new IllegalArgumentException("未設定寄件人郵件地址,spring.mail.sendfrom");
		}

		helper.setFrom(new InternetAddress(sendFrom, sender)); // 發件人

		helper.setSentDate(this.getSendDate() == null ? new Date() : this.getSendDate());
	}

	private MimeMessageHelper addReceiver(MimeMessageHelper helper) throws AddressException, MessagingException {
		for (String mailAddr : toMailAddrList) {
			helper.setTo(InternetAddress.parse(mailAddr));
		}
		for (String mailAddr : ccMailAddrList) {
			helper.setCc(InternetAddress.parse(mailAddr));
		}
		for (String mailAddr : bccMailAddrList) {
			helper.setBcc(InternetAddress.parse(mailAddr));
		}
		return helper;
	}

	private void setMailContent(MimeMessageHelper helper, String content) throws MessagingException {
		// 信件內容
		helper.setText(content, false);
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public void addTo(String mailAddr) {
		toMailAddrList.add(mailAddr);
	}

	public void addCC(String mailAddr) {
		ccMailAddrList.add(mailAddr);
	}

	public void addBCC(String mailAddr) {
		bccMailAddrList.add(mailAddr);
	}

	public HashMap<String, Object> getTemplateContextMap() {
		return templateContextMap;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSendFrom() {
		return sendFrom;
	}

	public void setSendFrom(String sendFrom) {
		this.sendFrom = sendFrom;
	}


	public ResourceBundleMessageSource emailMessageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("mail/MailMessages");
		return messageSource;
	}
}
