package com.fda.mdir.controller.rest;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fda.mdir.db.dao.ejb.EJBFacade;
import com.fda.mdir.db.dao.ejb.MDIRPatientDAO;
import com.fda.mdir.db.entities.Patient;
import com.fda.mdir.init.AppContextListener;

@CrossOrigin
@RestController
public class MDIRPatientController {
	private static final Logger logger = LoggerFactory.getLogger(MDIRPatientController.class);

	private MDIRPatientDAO mdirPatientDAO;

	@PostConstruct
	public void setup() {
		logger.debug("SETUP METHOD STARTED");
		try {

			EJBFacade ejbFacade = (EJBFacade) AppContextListener.getSpringContext().getBean("ejbFacade");
			String nameMDIRPatientDAO = (String) ejbFacade.getEjb().get("MDIRPatientDAO");

			logger.debug("nameMDIRPatientDAO := " + nameMDIRPatientDAO);

			Context context = new InitialContext();

			logger.debug("EJB Load Start");
			mdirPatientDAO = (MDIRPatientDAO) context.lookup(nameMDIRPatientDAO);
			logger.debug("EJB Load End");
		} catch (NamingException e) {
			e.printStackTrace();

			logger.debug("SETUP METHOD EXCEPTION END");
		}

		logger.debug("SETUP METHOD END");
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/patient/getpatients")
    public List<Patient> getAllPatients() {
        // Handle a new guest (if any):
        logger.debug("getall Patient called");
        
        logger.debug("mdirPatientDAO := " + mdirPatientDAO);
        
        List<Patient> names =  mdirPatientDAO.getAllPatients(); 
        
        return names;
    }
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/patient/getpatients/{patientID}")
    public Patient getPatientByPatientID(@PathVariable("patientID") String patientID) {
        logger.debug("getPatientByPatientID called");
        
        logger.debug("mdirPatientDAO := " + mdirPatientDAO);
        
        Patient patientInstance =  mdirPatientDAO.getPatientByPatientID(patientID);
        
        return patientInstance;
    }
}
