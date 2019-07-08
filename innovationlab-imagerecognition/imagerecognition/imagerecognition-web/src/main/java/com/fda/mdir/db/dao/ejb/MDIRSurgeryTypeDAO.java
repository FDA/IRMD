package com.fda.mdir.db.dao.ejb;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fda.mdir.db.entities.SurgeryType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRSurgeryTypeDAO {
private static final Logger logger = LoggerFactory.getLogger(MDIRSurgeryTypeDAO.class);
	
	@PersistenceContext
    private EntityManager em;
    
    public <T> void persist(T t) {

        em.persist(t);

    }
    
    public List<SurgeryType> getAllSurgeryTypes() {

        TypedQuery <SurgeryType> query = em.createQuery("SELECT n FROM SurgeryType n ORDER BY n.id", SurgeryType.class);

        List<SurgeryType> surgeryTypes = query.getResultList();

        return surgeryTypes;
    
    }
    
    public SurgeryType getSurgeryTypeBySurgeryID(String surgeryTypeID) {

        TypedQuery <SurgeryType> query = em.createQuery("SELECT n FROM SurgeryType n WHERE n.surgeryTypeId like :surgeryTypeID ORDER BY n.id", SurgeryType.class);

        List<SurgeryType> rows = query.setParameter("surgeryTypeID", surgeryTypeID).getResultList();
        
        SurgeryType surgeryType = null;
        
        if(rows != null && rows.size() > 0) {
        	surgeryType = rows.get(0);
        }

        return surgeryType;
    
    }
    
    @PreDestroy
    private void beforeGoing() {
        logger.debug("MDIRSurgeryTypeDAO going good bye");
    } 
}
