/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.db.dao.ejb;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fda.mdir.db.entities.MDIRRole;
import com.fda.mdir.db.entities.MDIRUser;



/**
 *
 * @author Muazzam
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRUserDAO {

    private static final Logger logger = LoggerFactory.getLogger(MDIRUserDAO.class);    
    
    @PersistenceContext
    private EntityManager em;
    
    public <T> void persist(T t) {

        em.persist(t);

    }
    
    
    public List<MDIRUser> getAllUsers() {

        TypedQuery <MDIRUser> query = em.createQuery("SELECT n FROM MDIRUser n ORDER BY n.id", MDIRUser.class);

        List<MDIRUser> users = query.getResultList();

        return users;
    
    }   
    
    public MDIRUser getActiveUserBySessionToken(String userSessionToken) {

        TypedQuery <MDIRUser> query = em.createNamedQuery("findUserBySessionToken" , MDIRUser.class).setParameter("tokenid", userSessionToken);

        
        MDIRUser user = null;
        try {
             user = query.getSingleResult();
             Set<MDIRRole> userRole = user.getMdirRole();
           logger.debug("userRole size " + userRole.size());
           Iterator<MDIRRole> itr = userRole.iterator();
           while(itr.hasNext()){
           	MDIRRole role = (MDIRRole)itr.next();
           }  
        } catch(NoResultException e){
        }

        
        return user;
    
    }
    
    public MDIRUser getUser(String email, String password) {
 
        logger.debug("Inside Get User DAO");
        
        TypedQuery <MDIRUser> query = em.createQuery("SELECT n FROM MDIRUser n WHERE n.email='"+email+"' "
        		+ "and n.password='"+password+"'" , MDIRUser.class);
        
        //em.getTransaction().commit();
        MDIRUser user = null;
        try {
            user = query.getSingleResult();
       } catch(NoResultException e){
    	   logger.debug("Inside Get User DAO: NoResultException");
       	return null;
       }
        return user;
    }  
       
    public MDIRUser getUser(String email) {
    	logger.debug("Inside Get User DAO");
        TypedQuery <MDIRUser> query = em.createQuery("SELECT n FROM MDIRUser n WHERE n.email='"+email+"'" , MDIRUser.class);

        MDIRUser user = null;
        try {
             user = query.getSingleResult();
        } catch(NoResultException e){
        	 logger.debug("Inside Get User DAO: NoResultException");
        	return null;
        }

        return user;
    } 
    
    
    @PreDestroy
    private void beforeGoing() {
        logger.debug("MDIRUserDAO going good bye");
    }    
    
}
