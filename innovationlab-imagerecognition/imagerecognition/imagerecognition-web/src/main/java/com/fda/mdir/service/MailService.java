/**
 * 
 */
package com.fda.mdir.service;

import java.util.List;

/**
 * @author kbdayma
 *
 */
public interface MailService {

	String sendEmail(String from, List<String> to, String subject, String msg);
	
}
