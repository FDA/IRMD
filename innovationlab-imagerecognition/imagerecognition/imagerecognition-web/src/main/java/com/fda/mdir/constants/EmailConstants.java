/**
 * 
 */
package com.fda.mdir.constants;

import java.util.List;

/**
 * @author kbdayma
 *
 */
public class EmailConstants {

	List<String> recipientEmailList;
	String senderMail;
	String subjectForRegistrationEvent;
	String subjectForScheduleAppointment;
	String registrationMailTemplateName;
	public String getRegistrationMailTemplateName() {
		return registrationMailTemplateName;
	}

	public void setRegistrationMailTemplateName(String registrationMailTemplateName) {
		this.registrationMailTemplateName = registrationMailTemplateName;
	}

	public String getScheduleAppointmentMailTemplateName() {
		return scheduleAppointmentMailTemplateName;
	}

	public void setScheduleAppointmentMailTemplateName(
			String scheduleAppointmentMailTemplateName) {
		this.scheduleAppointmentMailTemplateName = scheduleAppointmentMailTemplateName;
	}

	String scheduleAppointmentMailTemplateName;
	
	public List<String> getRecipientEmailList() {
		return recipientEmailList;
	}

	public void setRecipientEmailList(List<String> recipientEmailList) {
		this.recipientEmailList = recipientEmailList;
	}

	public String getSubjectForRegistrationEvent() {
		return subjectForRegistrationEvent;
	}

	public void setSubjectForRegistrationEvent(String subjectForRegistrationEvent) {
		this.subjectForRegistrationEvent = subjectForRegistrationEvent;
	}

	public String getSubjectForScheduleAppointment() {
		return subjectForScheduleAppointment;
	}

	public void setSubjectForScheduleAppointment(String subjectForScheduleAppointment) {
		this.subjectForScheduleAppointment = subjectForScheduleAppointment;
	}

	public String getSenderMail() {
		return senderMail;
	}

	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}

}
