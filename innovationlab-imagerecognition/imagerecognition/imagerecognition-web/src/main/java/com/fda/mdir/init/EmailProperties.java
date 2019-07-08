package com.fda.mdir.init;

import java.util.Date;

public interface EmailProperties {
	
	public String getPersonName();

	public void setPersonName(String personName);

	public String getCenterName() ;

	public void setCenterName(String centerName) ;

	public String getEmailAddress() ;

	public void setEmailAddress(String emailAddress) ;

	public String getPhoneNumber() ;

	public void setPhoneNumber(String phoneNumber) ;
	
	public String getTitle() ;

	public void setTitle(String title) ;
	
	public Date getDate();
	
	public Date setDate();

}
