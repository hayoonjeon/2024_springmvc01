package com.ict.edu05.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EMailhandler {

	private JavaMailSender javaMailSender;
	private MimeMessage message;
	private MimeMessageHelper messageHelper;

	public EMailhandler(JavaMailSender javaMailSender) throws Exception{
		this.javaMailSender = javaMailSender;
		
		message = this.javaMailSender.createMimeMessage();
		//true   : 멀티파트 메세지 사용가능
		messageHelper = new MimeMessageHelper(message,true,"UTF-8");
		
		
		//단순한 텍스트 메세지만 사용
		//messageHelper = new MimeMessageHelper(message,"UTF-8");
		
		
	}
	
	//제목
	public void setSubject(String subject) throws Exception{
		messageHelper.setSubject(subject);
	}
	//내용
	public void setText(String text) throws Exception{
		//true :  태그 사용가능 
		messageHelper.setText(text, true);
	}
	
	//보낸사람의 이메일과 제목
	public void setForm(String email, String name) throws Exception{
		messageHelper.setFrom(email, name);
		
	}
	
	//받는 이메일
	public void setTo(String email) throws Exception{
		messageHelper.setTo(email);
		
	}
	
	//보내기
	  public void send() throws Exception {
	        // JavaMailSender를 사용하여 메시지를 보냄
	        javaMailSender.send(message);
	    }
}
