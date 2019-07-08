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

import com.fda.mdir.db.entities.Patient;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRPatientDAO {
	private static final Logger logger = LoggerFactory.getLogger(MDIRPatientDAO.class);
	
	@PersistenceContext
    private EntityManager em;
    
    public <T> void persist(T t) {

        em.persist(t);

    }
    
    public List<Patient> getAllPatients() {

        TypedQuery <Patient> query = em.createQuery("SELECT n FROM Patient n ORDER BY n.id", Patient.class);

        List<Patient> patients = query.getResultList();

        return patients;
    
    }
    
    public Patient getPatientByPatientID(String patientID) {

        TypedQuery <Patient> query = em.createQuery("SELECT n FROM Patient n WHERE n.id like :patientID", Patient.class);

        List<Patient> rows = query.setParameter("patientID", patientID).getResultList();

        Patient patient = null;
        
        if(rows != null && rows.size() > 0) {
        	patient = rows.get(0);
        }
        
        return patient;
    
    }
    
    @PreDestroy
    private void beforeGoing() {
        logger.debug("MDIRPatientDAO going good bye");
    } 
}
