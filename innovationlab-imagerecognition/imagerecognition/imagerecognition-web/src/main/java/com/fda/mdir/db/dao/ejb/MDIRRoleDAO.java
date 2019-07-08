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

import com.fda.mdir.db.entities.MDIRRole;
import com.fda.mdir.db.entities.MDIRUser;
import com.fda.mdir.db.entities.SurgeryType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRRoleDAO {

    private static final Logger logger = LoggerFactory.getLogger(MDIRRoleDAO.class);    
    
    @PersistenceContext
    private EntityManager em;
    
    public <T> void persist(T t) {

        em.persist(t);

    }
     
    
    public List<MDIRRole> getAllRole() {

        TypedQuery <MDIRRole> query = em.createQuery("SELECT n FROM MDIRRole n ORDER BY n.id", MDIRRole.class);

        List<MDIRRole> mdirrole = query.getResultList();

        return mdirrole;
    
    }
    
    
    public MDIRRole getRoleByRoleID(String roleId) {
        TypedQuery <MDIRRole> query = em.createQuery("SELECT n FROM MDIRRole n WHERE n.id like :roleId "
        		+ "ORDER BY n.id", MDIRRole.class);
        query.setParameter("roleId", roleId).getResultList();
        MDIRRole roleObj = null;
       
        try {
        	roleObj = query.getSingleResult();
            
        } catch(NoResultException e){
        
        }
        
        return roleObj; 
    }
    
   
    @PreDestroy
    public void beforeGoing() {
        logger.debug("MDIRRoleDAO going good bye");
    }
    
}
