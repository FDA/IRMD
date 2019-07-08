/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fda.mdir.constants.LABConstants;
import com.fda.mdir.init.AppContextListener;

/**
 *
 * @author Muazzam
 */
public class DateUtils {
    
		static LABConstants narmsConstants = (LABConstants)AppContextListener.getSpringContext().getBean("labConstants");
	    static String DATE_FORMAT = (String)narmsConstants.getCONSTANTS().get("DATE_FORMAT");  
	    DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        static SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
	    
    	public static Date getFormatedDate(String dateText) throws ParseException{
            return format.parse(dateText);
    	}
    	
    	public static String getFormatedString(Date date) throws ParseException{
            return format.format(date);
    	}
    	
    	public static Date removeTime(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
    
}
