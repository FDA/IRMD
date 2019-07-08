package com.fda.mdir.controller.rest;

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
import com.fda.mdir.db.dao.ejb.MDIRProductDAO;
import com.fda.mdir.db.entities.Assembly;
import com.fda.mdir.db.entities.Product;
import com.fda.mdir.init.AppContextListener;

@CrossOrigin
@RestController
public class MDIRProductController {
	private static final Logger logger = LoggerFactory.getLogger(MDIRProductController.class);
	
	private MDIRProductDAO mdirProductDAO;
	
	@PostConstruct
	public void setup() {

		logger.debug("SETUP METHOD STARTED");

		try {
			EJBFacade ejbFacade = (EJBFacade) AppContextListener.getSpringContext().getBean("ejbFacade");
			String nameMDIRProductDAO = (String) ejbFacade.getEjb().get("MDIRProductDAO");

			logger.debug("nameMDIRProductDAO := " + nameMDIRProductDAO);

			Context context = new InitialContext();

			logger.debug("EJB Load Start");
			mdirProductDAO = (MDIRProductDAO) context.lookup(nameMDIRProductDAO);
			logger.debug("EJB Load End");
		} catch (NamingException e) {
			e.printStackTrace();
			logger.debug("SETUP METHOD EXCEPTION END");
		}
		logger.debug("SETUP METHOD END");
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/searchproductbuid/{productID}")
    public Product getProductByID(@PathVariable("productID") String productID) {
        // Handle a new guest (if any):
        logger.debug("getProductByID called");
        
        logger.debug("mdirProductDAO := " + mdirProductDAO);
        
        Product result = mdirProductDAO.getProductByID(productID); 
        
        return result;
   }
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/searchimplantybybarcode/{barCodeID}/{trayType}")
    public String searchImplantyByBarCode(@PathVariable("barCodeID") String barCodeID,
    		@PathVariable("trayType") String productType) {
        // Handle a new guest (if any):
        logger.debug("searchImplantyByBarCode called");
        
        logger.debug("mdirProductDAO := " + mdirProductDAO);
        
        String result = mdirProductDAO.searchProductByBarCode(barCodeID,productType); 
        
        return result;
    }
	
	

}
