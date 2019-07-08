/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ejb.EJB;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fda.mdir.db.dao.ejb.MDIRUserDAO;

/**
 *
 * @author Muazzam
 */
public class LABUserTest {
    
    @EJB
    MDIRUserDAO labUserDAO;
    
    public LABUserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testUSEREJB() {
        //System.out.println(labUserDAO.getAllUsers());
    }
}
