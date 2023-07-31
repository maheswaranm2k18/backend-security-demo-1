package com.example.demo.service;

import com.sun.mail.smtp.SMTPTransport;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Value;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;
import java.util.Random;


import static com.example.demo.constant.EmailConstant.*;

@Service
public class EmailService {
	
//	@Value("${spring.mail.password}")
//    private final String PASSWORD;
	
	private static String HOST = "smtp.gmail.com";
	private static String PORT = "587";
	private static String USERNAME = "maheswaranm2k18@gmail.com";
	private static String PWD = "wyvnwoeargiapxym";

    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
//        Message message = createEmail(firstName, password, email);
//        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
//        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD.replace("\"", ""));
//        smtpTransport.sendMessage(message, message.getAllRecipients());
//        smtpTransport.close();
    	
    	Properties properties = new Properties();
		properties.put("mail.smtp.host", HOST);
		properties.put("mail.smtp.port", PORT);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.trust", HOST);
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PWD);
			}
		};
		Session sess = Session.getInstance(properties, auth);
		Random random = new Random();
//		int otp = 100000 + random.nextInt(900000);
		String subject = "Here's the new password From Naduppalayam cable operator";
//		String content = "<p>Hello,</p>"
//				+ "<p>You have requested to reset your password.</p>"
//				+ "<p>OTP number for validation :"+otp+"</p>"
//				+ "<p>This is valid for 5 minutes</p>"
//				+ "<br>"
//				+ "<p>Ignore this email if you do remember your password, "
//				+ "or you have not made the request.</p>";
		Message msg = new MimeMessage(sess);
		msg.setFrom(new InternetAddress(USERNAME));
		msg.setSubject(subject);
		msg.setSentDate(new Date());
//		msg.setContent(content,"text/html");
		msg.setText("Hello " + firstName + ", \n \n Your new account password is: " + password + "\n \n The Support Team");
//		msg.setSentDate(new Date());
		msg.saveChanges();
		String emailId = email.replace("%40", "@").replace("=", "");
		System.out.println(emailId);
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailId, false));
		try{
			Transport.send(msg);
			System.out.println("---------------------------------------MAIL SENT----------------------------------------------------------");
		}
		catch(Exception ex) {
			System.err.println("----------------------------------MAIL SENDING FAILED--------------------------------------------");
			ex.printStackTrace();
		}
    }

//    private Message createEmail(String firstName, String password, String email) throws MessagingException {
//        Message message = new MimeMessage(getEmailSession());
//        message.setFrom(new InternetAddress(FROM_EMAIL));
//        message.setRecipients(RecipientType.TO, InternetAddress.parse(email, false));
//        message.setRecipients(RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
//        message.setSubject(EMAIL_SUBJECT);
//        message.setText("Hello " + firstName + ", \n \n Your new account password is: " + password + "\n \n The Support Team");
//        message.setSentDate(new Date());
//        message.saveChanges();
//        return message;
//    }
//
//    private Session getEmailSession() {
//        Properties properties = System.getProperties();
//        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
//        properties.put(SMTP_AUTH, true);
//        properties.put(SMTP_PORT, DEFAULT_PORT);
//        properties.put(SMTP_STARTTLS_ENABLE, true);
//        properties.put(SMTP_STARTTLS_REQUIRED, true);
//        return Session.getInstance(properties, null);
//    }
}
