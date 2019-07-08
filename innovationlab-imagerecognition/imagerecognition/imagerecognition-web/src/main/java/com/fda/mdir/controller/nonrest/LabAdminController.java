/**
 * 
 */
package com.fda.mdir.controller.nonrest;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fda.mdir.db.dao.ejb.EJBFacade;
import com.fda.mdir.db.dao.ejb.MDIRUserDAO;
import com.fda.mdir.init.AppContextListener;
import com.fda.mdir.service.PushNotificationService;

/**
 * @author kbdayma
 *
 */
@Controller
public class LabAdminController {

	private static final Logger logger = LoggerFactory.getLogger(LabAdminController.class);

	@Autowired
	private PushNotificationService pushNotificationService;

	private MDIRUserDAO idelabUserDAO;

	private SimpleDateFormat format = new SimpleDateFormat();

	@PostConstruct
	public void setup() {

		logger.debug("SETUP METHOD STARTED");
		try {
			EJBFacade ejbFacade = (EJBFacade) AppContextListener.getSpringContext().getBean("ejbFacade");
			//String nameIDEALABEventDAO = (String) ejbFacade.getEjb().get("IDEALABEventDAO");
			String nameIDEALABUserDAO = (String) ejbFacade.getEjb().get("MDIRUserDAO");

			//logger.debug("nameIDEALABUserDAO := " + nameIDEALABEventDAO);
			logger.debug("nameIDEALABUserDAO := " + nameIDEALABUserDAO);

			Context context = new InitialContext();

			logger.debug("EJB Load Start");
			idelabUserDAO = (MDIRUserDAO) context.lookup(nameIDEALABUserDAO);

			logger.debug("EJB Load End");
		} catch (NamingException e) {
			e.printStackTrace();
			logger.debug("SETUP METHOD EXCEPTION END");
		}

		logger.debug("SETUP METHOD END");
	}


    
    @RequestMapping(value = "/auth/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {

		logger.debug("Inside login");
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("/auth/login");

		return model;
	}
		
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public ModelAndView welcomePage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("/auth/login");
		return model;
	}
}
