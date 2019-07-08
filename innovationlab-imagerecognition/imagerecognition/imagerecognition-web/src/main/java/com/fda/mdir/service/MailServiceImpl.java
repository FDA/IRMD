/**
 * 
 */
package com.fda.mdir.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author kbdayma
 *
 */
@Service
public class MailServiceImpl implements MailService{

	@Autowired
    private MailSender mailSender;
	
	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	
	/**
	 * @param
	 *
	 */
	@Override
	public String sendEmail(String from, List<String> to, String subject, String msg) {
		
		logger.debug("MailServiceImpl:sendEmail");		
		logger.debug("To::"+to);
		
		//creating message  
        SimpleMailMessage message = new SimpleMailMessage();  
        message.setTo(to.toArray(new String[to.size()]));
        message.setSubject(subject);  
        message.setText(msg);  
        //sending message  
        mailSender.send(message);
		return msg;  
	}

}