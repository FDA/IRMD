/**
 * 
 */
package com.fda.mdir.utils;

import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.fda.mdir.init.EmailProperties;

/**
 * @author kbdayma
 *
 */
public class EmailUtility {
	
	static StringBuilder builder;
	static String lineSpearator = System.lineSeparator();
	

	
	public static String buildMessageForRegistrationEvent(VelocityEngine velocityEngine, String templateName, EmailProperties labEventRegistration) {
		Template template = velocityEngine.getTemplate("./templates/" + templateName);

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("personName", labEventRegistration.getPersonName());
		velocityContext.put("centerName", labEventRegistration.getCenterName());
		velocityContext.put("eventTitle", labEventRegistration.getTitle());
		velocityContext.put("eventDate", formatter.format(labEventRegistration.getDate()));
		velocityContext.put("emailAddress", labEventRegistration.getEmailAddress());
		velocityContext.put("phoneNumber", labEventRegistration.getPhoneNumber());
		
		
		StringWriter stringWriter = new StringWriter();
		  
		template.merge(velocityContext, stringWriter);
		  
		return stringWriter.toString();
	}
	

}
