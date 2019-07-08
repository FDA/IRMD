/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.db.dao.ejb;


import java.util.List;

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

import com.fda.mdir.db.entities.MDIRPasswordResetRequest;



/**
 *
 * @author Muazzam
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRPasswordResetRequestDAO {

    private static final Logger logger = LoggerFactory.getLogger(MDIRPasswordResetRequestDAO.class);    
    
    @PersistenceContext
    private EntityManager em;
    
    public <T> void persist(T t) {

        em.persist(t);

    }
    
    
    public List<MDIRPasswordResetRequest> getAllRequests() {

        TypedQuery <MDIRPasswordResetRequest> query = em.createQuery("SELECT n FROM NarmsResetRequests n ORDER BY n.id", MDIRPasswordResetRequest.class);

        List<MDIRPasswordResetRequest> requests = query.getResultList();

        
        return requests;
    
    }
    
    public MDIRPasswordResetRequest getRequest(String email, String code) {

        TypedQuery <MDIRPasswordResetRequest> query = em.createQuery("SELECT n FROM NarmsResetRequest n "
        		+ "WHERE n.email='"+email+"' and n.code='"+code+"'" , MDIRPasswordResetRequest.class);

        MDIRPasswordResetRequest request = null;
        try {
             request = query.getSingleResult();
        } catch(NoResultException e){
            return null;
        }
        
        return request;
    }  
       
    
    @PreDestroy
    public void beforeGoing() {
        logger.debug("MDIRPasswordResetRequestDAO going good bye");
    }
    
}
