/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.db.dao.ejb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author Muazzam
 */
public class EJBFacade {
    
    private static final Logger logger = LoggerFactory.getLogger(EJBFacade.class);
    
    private Map ejb;

	public Map getEjb() {
		return ejb;
	}

	public void setEjb(Map ejb) {
		this.ejb = ejb;
	}
    
    
    
    
}
