/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.db.dao.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author Muazzam
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRUserSessionDAO {

    private static final Logger logger = LoggerFactory.getLogger(MDIRUserSessionDAO.class); 
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void ejbPostConstruct() {
        logger.debug("SessionDAO EJB Started");
    }    
    
    public <T> void persist(T t) {
        em.persist(t);
    }
    
    public Long getClientID(String tokenId) {
        Query query = em.createNamedQuery( "findUserIDByToken").setParameter("tokenid", tokenId);
        Long userID = null;
        try {
             userID = (Long)query.getSingleResult();
        } catch(NoResultException e){
            return null;
        }
        return userID;
    }    
    
    public Long getClient(String tokenId) {
        Query query = em.createNamedQuery( "findUserIDByToken").setParameter("tokenid", tokenId);
        Long userID = null;
        try {
             userID = (Long)query.getSingleResult();
        } catch(NoResultException e){
            return null;
        }
        return userID;
    }    
    
    @PreDestroy
    public void ejbPreDestroy() {
        logger.debug("MDIRUserSessionDAO going good bye");
    }
    
}
