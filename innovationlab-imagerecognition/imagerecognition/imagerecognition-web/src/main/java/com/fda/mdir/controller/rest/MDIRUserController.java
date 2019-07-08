/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.controller.rest;



import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fda.mdir.data.dto.RestResponseDTO;
import com.fda.mdir.data.dto.UserDTO;
import com.fda.mdir.db.dao.ejb.EJBFacade;
import com.fda.mdir.db.dao.ejb.MDIRPasswordResetRequestDAO;
import com.fda.mdir.db.dao.ejb.MDIRRoleDAO;
import com.fda.mdir.db.dao.ejb.MDIRUserDAO;
import com.fda.mdir.db.dao.ejb.MDIRUserSessionDAO;
import com.fda.mdir.db.entities.MDIRPasswordResetRequest;
import com.fda.mdir.db.entities.MDIRRole;
import com.fda.mdir.db.entities.MDIRUser;
import com.fda.mdir.db.entities.MDIRUserSession;
import com.fda.mdir.init.AppContextListener;

/**
 *
 * @author Muazzam
 */
@CrossOrigin
@RestController
public class MDIRUserController {
    
    private static final Logger logger = LoggerFactory.getLogger(MDIRUserController.class);
    
    private MDIRUserDAO idelabUserDAO;
    
    private MDIRRoleDAO idelabRoleDAO;
    
    private MDIRUserSessionDAO idealabSessionDAO;
    
    private MDIRPasswordResetRequestDAO idealabResetRequestDAO;
    
    
    @PostConstruct
    public void setup(){

    	logger.debug("SETUP METHOD STARTED");
    	
        try {
        	
			EJBFacade ejbFacade = (EJBFacade)AppContextListener.getSpringContext().getBean("ejbFacade");
			String nameMDIRUserDAO = (String)ejbFacade.getEjb().get("MDIRUserDAO");
			String nameSessionDAO = (String)ejbFacade.getEjb().get("MDIRUserSessionDAO");
			String namePasswordResetRequestDAO = (String)ejbFacade.getEjb().get("MDIRPasswordResetRequestDAO");
			String nameMDIRRoleDAO = (String)ejbFacade.getEjb().get("MDIRRoleDAO");
			
			logger.debug("nameMDIRUserDAO := " + nameMDIRUserDAO );
			logger.debug("nameSessionDAO := " + nameSessionDAO );
			logger.debug("namePasswordResetRequestDAO := " + namePasswordResetRequestDAO );
			
			Context context = new InitialContext();
        	
			logger.debug("EJB Load Start");
			idelabUserDAO = (MDIRUserDAO) context.lookup(nameMDIRUserDAO);
			idealabSessionDAO = (MDIRUserSessionDAO) context.lookup(nameSessionDAO);
			idealabResetRequestDAO = (MDIRPasswordResetRequestDAO) context.lookup(namePasswordResetRequestDAO);
			idelabRoleDAO = (MDIRRoleDAO)context.lookup(nameMDIRRoleDAO);
			
			logger.debug("EJB Load End");
        } catch (NamingException e) {
            e.printStackTrace();
            
            logger.debug("SETUP METHOD EXCEPTION END");
        }
    	
        logger.debug("SETUP METHOD END");
        
    }
   
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/getallusers")
    public List<MDIRUser> getAllMDIRUsers() {
        // Handle a new guest (if any):
        logger.debug("getallnarmusers called");
        
        logger.debug("labuserdao := " + idelabUserDAO);
        
        List<MDIRUser> names =  idelabUserDAO.getAllUsers(); 
        
        return names;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/getalluserRole")
    public List<MDIRRole> getAllMDIRRole() {
        // Handle a new guest (if any):
        logger.debug("getalluserRole called");
        
        logger.debug("labuserroledao := " + idelabRoleDAO);
        
        List<MDIRRole> roleList =  idelabRoleDAO.getAllRole(); 
        
        return roleList;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/getactiveuserbyid/{tokenid}")
    public String getActiveUserID( @PathVariable("tokenid") String tokenid) {
        // Handle a new guest (if any):
        logger.debug("getActiveUserID called");
        logger.debug("getActiveUserID called token: " + tokenid);
        
        try{
        
            Long MDIRUserID =  idealabSessionDAO.getClientID(tokenid);
            if( MDIRUserID != null ){

                logger.debug("getActiveUserID sucess {\"userid\":\""+MDIRUserID+"\"}");
                return "{\"userid\":\""+MDIRUserID+"\"}";


            }
        }catch(javax.persistence.NoResultException excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("getActiveUserID not sucess");
            return  "{\"message\":\"No Record Found\"}";
        }
        
        logger.debug("getActiveUserID not sucess return null");
        return null;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/getactiveuserbytoken/{tokenid}")
    public RestResponseDTO getActiveUserByToken(@PathVariable("tokenid") String tokenid) {
        // Handle a new guest (if any):
        logger.debug("getActiveUserByToken called");
        logger.debug("getActiveUserByToken token: " + tokenid);
        RestResponseDTO dto = new RestResponseDTO();
        try{
            
            MDIRUser MDIRUser =  idelabUserDAO.getActiveUserBySessionToken(tokenid);
            if( MDIRUser != null ){
                //logger.debug("MDIRUser.getidealabCenter().getId() " + MDIRUser.getidealabCenter().getId());
                
                dto.setData(MDIRUser);
                logger.debug("getActiveUserByToken sucess");
                return dto;
            }
        }catch(javax.persistence.NoResultException excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("getActiveUserByToken not sucess");
            dto.setError("Exception occured please check logs");
            return  dto;
        }
        
        dto.setMessage("No Record Found");
        logger.debug("getActiveUserByToken not sucess return null");
        return dto;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/login/email/{email}/password/{password}")
    public String login(@PathVariable("email") String email, @PathVariable("password") String password) {
        // Handle a new guest (if any):
        logger.debug("login called");
        logger.debug("login called email: " + email);
        logger.debug("login called password: " + password);
        
        try{
        
            MDIRUser MDIRUser = idelabUserDAO.getUser(email, password);
            if( MDIRUser != null ){

                String uniqueID = UUID.randomUUID().toString();
                
                MDIRUserSession session = new MDIRUserSession(MDIRUser.getId() , uniqueID);
                session.setLabUser(MDIRUser);
                
                idealabSessionDAO.persist(session);

                logger.debug("login sucess {\"token\":\""+uniqueID+"\"}");
                return "{\"token\":\""+uniqueID+"\"}";


            }else{
                logger.debug("login not sucess user null");
                return  "{\"message\":\"Login Failed. Try Again\"}";
            }
        }catch(Exception excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("login not sucess");
            return  "{\"message\":\"Login Failed. Try Again\"}";
        }
        
        //logger.debug("login not sucess return null");
        //return null;
    }
        
    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/getuserrolebytoken/{tokenid}")
    public UserDTO getUserRoleByToken(@PathVariable("tokenid") String tokenid) {
        // Handle a new guest (if any):
        logger.debug("getUserRoleByToken called");
        logger.debug("getUserRoleByToken token: " + tokenid);
        UserDTO userDTO = new UserDTO();
        try{
            
            MDIRUser mDIRUser =  idelabUserDAO.getActiveUserBySessionToken(tokenid);
            if( mDIRUser != null ){
                //logger.debug("MDIRUser.getidealabCenter().getId() " + MDIRUser.getidealabCenter().getId());
                
            	userDTO.setEmail(mDIRUser.getEmail());
                userDTO.setFirstName(mDIRUser.getFirstName());
                userDTO.setLastName(mDIRUser.getLastName());
                userDTO.setId(mDIRUser.getId());
                userDTO.setToken(tokenid);
                userDTO.setUserRoleId(mDIRUser.getMdirRole());
                
                logger.debug("getUserRoleByToken sucess");
                return userDTO;
            }
        }catch(javax.persistence.NoResultException excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("getUserRoleByToken not sucess");
            userDTO.setMessages("Exception occured please check logs");
            return  userDTO;
        }
        
        userDTO.setMessages("No Record Found");
        logger.debug("getUserRoleByToken not sucess return null");
        return userDTO;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/register/email/{email}/password/{password}/firstname/{firstname}/lastname/{lastname}/role/{role}")
    public String register(
    		@PathVariable("email") String email,
    		@PathVariable("password") String password, @PathVariable("firstname") String firstname, 
    		@PathVariable("lastname") String lastname, @PathVariable("role") String[] role) {
    	
        // Handle a new guest (if any):
        logger.debug("register called"); 
        
        Set<MDIRRole> userRole = new HashSet<>();
        MDIRUser labUser =  this.idelabUserDAO.getUser(email);
      
        
        if( labUser != null ){
            return "{\"error\":\"This email is already registered\"}";
        }else{
        	 if( role == null ){
             	return "{\"error\":\"Invalid role\"}";
             } else if(role != null && role.length > 0) {
            	 if(role.length == 1) {
            		 if ("0".equals(role[0])) {
            			 role  = new String[] {"1","2"};
            		 }
            	 }
            	 
             	for(int i=0; i<role.length; i++) {
             		 MDIRRole mdirRole = this.idelabRoleDAO.getRoleByRoleID(role[i]);
                 	 if(mdirRole == null) {
                      	return "{\"error\":\"Invalid role\"}";
                 	 } else {
                 		 userRole.add(mdirRole);
                 	 }
             	}
             }
        	MDIRUser user = new MDIRUser(email, password, firstname, lastname);
            user.setMdirRole(userRole);        
            idelabUserDAO.persist(user);
            return "{\"message\":\"Ok\"}";
        }
    }    

    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/request_reset/{email}")
    public String request_reset(@PathVariable("email") String email ) {
        // create a request for new password
        logger.debug("request reset called with email : " + email);
        
        
        MDIRUser MDIRUser =  idelabUserDAO.getUser(email);
        if( MDIRUser != null ){
            String uniqueID = UUID.randomUUID().toString();
            Long userID = MDIRUser.getId();
            idealabResetRequestDAO.persist(new MDIRPasswordResetRequest(email, userID, uniqueID));

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
           
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Password reset code");
            msg.setText("A password request for your account has just been requested. To reset it, go to http://52.26.209.16:8000/#/reset_code?"+uniqueID);
            mailSender.send(msg);

           return "{\"message\":\"Ok\"}";
        }else{
             return "{\"error\":\"Invalid email to send request to\"}";
            
        }

    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/mdiruser/update_login/{new_password}/code/{reset_code}/email/{email}")
    public String update_login(@PathVariable("new_password") String new_password, @PathVariable("reset_code") String reset_code, @PathVariable("email") String email ) {
        // create a request for new password
        logger.debug("update login called");
        
        
        MDIRPasswordResetRequest idealabRequest = idealabResetRequestDAO.getRequest(email, reset_code);
        if( idealabRequest != null ){
            MDIRUser MDIRUser =  idelabUserDAO.getUser(email);
            MDIRUser.setPassword(new_password);
            idelabUserDAO.persist(MDIRUser);
            

           return "{\"message\":\"Ok\"}";
        }else{
             return "{\"error\":\"Invalid email or reset code to update\"}";
            
        }
        
    }
}
