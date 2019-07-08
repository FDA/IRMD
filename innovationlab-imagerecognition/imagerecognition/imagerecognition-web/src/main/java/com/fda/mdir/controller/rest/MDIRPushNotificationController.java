/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.controller.rest;



import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fda.mdir.data.dto.RestResponseDTO;
import com.fda.mdir.db.dao.ejb.EJBFacade;
import com.fda.mdir.db.dao.ejb.MDIRUserDAO;
import com.fda.mdir.db.entities.MDIRUser;
import com.fda.mdir.init.AppContextListener;
import com.fda.mdir.service.PushNotificationService;

/**
 *
 * @author Muazzam
 */
@CrossOrigin
@RestController
public class MDIRPushNotificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(MDIRPushNotificationController.class);
    
    private MDIRUserDAO ideaLabUserDAO;
    
    @Autowired
    private PushNotificationService pushNotificationService;
    
    @PostConstruct
    public void setup(){

    	logger.debug("SETUP METHOD STARTED");
    	
        try {
        	
			EJBFacade ejbFacade = (EJBFacade)AppContextListener.getSpringContext().getBean("ejbFacade");
			String nameIDEALABUserDAO = (String)ejbFacade.getEjb().get("MDIRUserDAO");
			
			logger.debug("nameIDEALABUserDAO := " + nameIDEALABUserDAO );
			
			Context context = new InitialContext();
        	
			logger.debug("EJB Load Start");
			ideaLabUserDAO = (MDIRUserDAO) context.lookup(nameIDEALABUserDAO);
			logger.debug("EJB Load End");
        } catch (NamingException e) {
            e.printStackTrace();
            
            logger.debug("SETUP METHOD EXCEPTION END");
        }
    	
        logger.debug("SETUP METHOD END");
        
    }
   
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/notification/register", method=RequestMethod.POST)
    public RestResponseDTO updateDeviceToken(@RequestParam ("registrationToken") String registrationToken,
    		@RequestParam("userName") String userName, @RequestParam("devicePlatform") String devicePlatform) {
        logger.debug("updateDeviceToken(): START");
        RestResponseDTO result = new RestResponseDTO();
       
        
        try {
        	
        	if(StringUtils.isEmpty(registrationToken)) {
        		result.setError("Invalid device token.");
        		return result;
        	}
        	
        	MDIRUser user = ideaLabUserDAO.getUser(userName);
        	
        	if(user == null) {
        		result.setError("Invalid User.");
        		return result;
        	}
        	
        	pushNotificationService.register(userName, registrationToken, devicePlatform);
        	
        	
        	result.setMessage("SUCCESS");
        	
        } catch(Exception e) {
        	result.setError("Unknown error occured. Cannot store device token.");
    		return result;
        }	
        
        logger.debug("updateDeviceToken(): END");
        return result;
    }
    
   
}
