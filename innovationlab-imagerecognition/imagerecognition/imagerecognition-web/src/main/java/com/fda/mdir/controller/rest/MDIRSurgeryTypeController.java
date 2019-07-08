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
import com.fda.mdir.db.dao.ejb.MDIRSurgeryTypeDAO;
import com.fda.mdir.db.entities.SurgeryType;
import com.fda.mdir.init.AppContextListener;

@CrossOrigin
@RestController
public class MDIRSurgeryTypeController {
	private static final Logger logger = LoggerFactory.getLogger(MDIRSurgeryTypeController.class);

	private MDIRSurgeryTypeDAO mdirSurgeryTypeDAO;

	@PostConstruct
	public void setup() {
		logger.debug("SETUP METHOD STARTED");
		try {
			EJBFacade ejbFacade = (EJBFacade) AppContextListener.getSpringContext().getBean("ejbFacade");
			String nameMDIRSurgeryTypeDAO = (String) ejbFacade.getEjb().get("MDIRSurgeryTypeDAO");

			logger.debug("nameMDIRSurgeryTypeDAO := " + nameMDIRSurgeryTypeDAO);

			Context context = new InitialContext();

			logger.debug("EJB Load Start");
			mdirSurgeryTypeDAO = (MDIRSurgeryTypeDAO) context.lookup(nameMDIRSurgeryTypeDAO);
			logger.debug("EJB Load End");
		} catch (NamingException e) {
			e.printStackTrace();

			logger.debug("SETUP METHOD EXCEPTION END");
		}

		logger.debug("SETUP METHOD END");
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/surgery/getsurgerytypes")
    public List<SurgeryType> getAllSurgeryTypes() {
        // Handle a new guest (if any):
        logger.debug("getall Surgery type called");
        
        logger.debug("mdirSurgeryTypeDAO := " + mdirSurgeryTypeDAO);
        
        List<SurgeryType> names =  mdirSurgeryTypeDAO.getAllSurgeryTypes(); 
        
        return names;
    }
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/surgery/getsurgerytypes/{surgeryTypeID}")
    public SurgeryType getSurgeryTypeBySurgeryTypeID(@PathVariable("surgeryTypeID") String surgeryTypeID) {
        logger.debug("getSurgeryTypeBySurgeryTypeID called");
        
        logger.debug("mdirSurgeryTypeDAO := " + mdirSurgeryTypeDAO);
        
        SurgeryType surgeryTypeInstance =  mdirSurgeryTypeDAO.getSurgeryTypeBySurgeryID(surgeryTypeID); 
        
        return surgeryTypeInstance;
    }
}
