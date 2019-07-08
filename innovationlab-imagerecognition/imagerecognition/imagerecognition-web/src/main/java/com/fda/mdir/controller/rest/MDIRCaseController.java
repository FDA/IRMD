package com.fda.mdir.controller.rest;

import java.util.ArrayList;
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
import com.fda.mdir.db.dao.ejb.MDIRAssemblyDAO;
import com.fda.mdir.db.dao.ejb.MDIRCaseDAO;
import com.fda.mdir.db.entities.Assembly;
import com.fda.mdir.db.entities.CaseDetails;
import com.fda.mdir.db.entities.SurgeryType;
import com.fda.mdir.init.AppContextListener;

@CrossOrigin
@RestController
public class MDIRCaseController {
	private static final Logger logger = LoggerFactory.getLogger(MDIRCaseController.class);
	
	private MDIRCaseDAO mdirCaseDAO;
	
	@PostConstruct
	public void setup() {
		logger.debug("SETUP METHOD STARTED");
		try {
			EJBFacade ejbFacade = (EJBFacade) AppContextListener.getSpringContext().getBean("ejbFacade");
			String nameMDIRCaseDAO = (String) ejbFacade.getEjb().get("MDIRCaseDAO");

			//logger.debug("nameMDIRAssemblyDAO := " + nameMDIRAssemblyDAO);
			logger.debug("nameMDIRCaseDAO := " + nameMDIRCaseDAO);

			Context context = new InitialContext();

			logger.debug("EJB Load Start");
			mdirCaseDAO = (MDIRCaseDAO) context.lookup(nameMDIRCaseDAO);
			logger.debug("EJB Load End");
		} catch (NamingException e) {
			e.printStackTrace();

			logger.debug("SETUP METHOD EXCEPTION END");
		}

		logger.debug("SETUP METHOD END");
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/getSurgeons")
    public List<String> getSurgeons() {
        // Handle a new guest (if any):
        logger.debug("getSurgeons called");
        
        logger.debug("mdirCaseDAO := " + mdirCaseDAO);
        
        List<String> names =  mdirCaseDAO.getAllSurgeons(); 
        
        return names;
    }
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/getSurgeons/{patientID}")
    public List<String> getSurgeonsByPatientID(@PathVariable("patientID") String patientID) {
        // Handle a new guest (if any):
        logger.debug("getSurgeonsByPatientID called");
        
        logger.debug("mdirCaseDAO := " + mdirCaseDAO);
        
        List<String> names =  mdirCaseDAO.getAllSurgeonsByPatientID(patientID); 
        
        return names;
    }
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/getcasedates/{patientID}/{surgeonName}")
    public List<String> getCaseDateByPatientIDBySurgeonName(@PathVariable("patientID") String patientID, @PathVariable("surgeonName") String surgeonName) {
        // Handle a new guest (if any):
        logger.debug("getCaseDateByPatientIDBySurgeonName called");
        
        logger.debug("mdirCaseDAO := " + mdirCaseDAO);
        
        List<String> dates =  mdirCaseDAO.getDateByPatientIDBySurgeonName(patientID, surgeonName); 
        
        return dates;
    }
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/getrelatedsurgerytypes/{patientID}/{surgeonName}/{caseDate}")
	public List<SurgeryType> getRelatedSurgeryType(@PathVariable("patientID") String patientID, @PathVariable("surgeonName") String surgeonName, @PathVariable("caseDate") String caseDate) {
		logger.debug("getRelatedSurgeryType called");
        
        logger.debug("mdirCaseDAO := " + mdirCaseDAO);
        
        List<SurgeryType> surgeryTypes =  mdirCaseDAO.getRelatedSurgeryType(patientID, surgeonName, caseDate);
        
        return surgeryTypes;
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/getrelatedassemblies/{patientID}/{surgeonName}/{caseDate}/{surgeryTypeID}")
	public List<Assembly> getRelatedAssemblies(
			@PathVariable("patientID") String patientID, 
			@PathVariable("surgeonName") String surgeonName, 
			@PathVariable("caseDate") String caseDate,
			@PathVariable("surgeryTypeID") String surgeryTypeID) {
		logger.debug("getRelatedAssemblies called");
        
        logger.debug("mdirCaseDAO := " + mdirCaseDAO);
         
        List<Assembly> caseAssemly =  mdirCaseDAO.getRelatedAssemblies(patientID, surgeonName, caseDate, surgeryTypeID);
        
        return caseAssemly;
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/getrelatedcasedetails/{patientID}/{surgeonName}/{caseDate}/{surgeryTypeID}")
	public CaseDetails getRelatedCaseDetails(
			@PathVariable("patientID") String patientID, 
			@PathVariable("surgeonName") String surgeonName, 
			@PathVariable("caseDate") String caseDate,
			@PathVariable("surgeryTypeID") String surgeryTypeID) {
		logger.debug("getRelatedCaseDetails called");
        
        logger.debug("mdirCaseDAO := " + mdirCaseDAO);
        
        CaseDetails caseDetailsInstance =  mdirCaseDAO.getRelatedCaseDetails(patientID, surgeonName, caseDate, surgeryTypeID);
        
        return caseDetailsInstance;
	}
	
	
}
