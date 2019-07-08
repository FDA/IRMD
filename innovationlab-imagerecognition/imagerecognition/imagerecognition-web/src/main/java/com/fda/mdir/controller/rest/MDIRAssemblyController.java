package com.fda.mdir.controller.rest;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//import com.fda.mdir.data.dto.RestResponseDTO;
import com.fda.mdir.db.dao.ejb.EJBFacade;
import com.fda.mdir.db.dao.ejb.MDIRAssemblyDAO;
import com.fda.mdir.db.dao.ejb.MDIRCaseDAO;
import com.fda.mdir.db.dao.ejb.MDIRProductDAO;
import com.fda.mdir.db.dao.ejb.MDIRUserDAO;
import com.fda.mdir.db.entities.Assembly;
import com.fda.mdir.db.entities.AssemblyDetails;
import com.fda.mdir.db.entities.CaseDetails;
import com.fda.mdir.db.entities.Casedetails_assembly;
import com.fda.mdir.db.entities.MDIRUser;
import com.fda.mdir.db.entities.Product;
import com.fda.mdir.init.AppContextListener;

@CrossOrigin
@RestController
public class MDIRAssemblyController {
	private static final Logger logger = LoggerFactory.getLogger(MDIRAssemblyController.class);

	private MDIRAssemblyDAO mdirAssemblyDAO;
	private MDIRCaseDAO mdirCaseDAO;
	private MDIRProductDAO mdirProductDAO;
	private MDIRUserDAO idelabUserDAO;

	@PostConstruct
	public void setup() {

		logger.debug("SETUP METHOD STARTED");

		try {
			EJBFacade ejbFacade = (EJBFacade) AppContextListener.getSpringContext().getBean("ejbFacade");
			String nameMDIRAssemblyDAO = (String) ejbFacade.getEjb().get("MDIRAssemblyDAO");
			String nameMDIRCaseDAO = (String) ejbFacade.getEjb().get("MDIRCaseDAO");
			String nameMDIRProductDAO = (String) ejbFacade.getEjb().get("MDIRProductDAO");
			String nameMDIRUserDAO = (String)ejbFacade.getEjb().get("MDIRUserDAO");
			
			logger.debug("nameMDIRAssemblyDAO := " + nameMDIRAssemblyDAO);
			logger.debug("nameMDIRCaseDAO := " + nameMDIRCaseDAO);

			Context context = new InitialContext();

			logger.debug("EJB Load Start");
			mdirAssemblyDAO = (MDIRAssemblyDAO) context.lookup(nameMDIRAssemblyDAO);
			mdirCaseDAO = (MDIRCaseDAO) context.lookup(nameMDIRCaseDAO);
			mdirProductDAO = (MDIRProductDAO) context.lookup(nameMDIRProductDAO);
			idelabUserDAO = (MDIRUserDAO) context.lookup(nameMDIRUserDAO);
			logger.debug("EJB Load End");
		} catch (NamingException e) {
			e.printStackTrace();
			logger.debug("SETUP METHOD EXCEPTION END");
		}
		logger.debug("SETUP METHOD END");
	}
	
	
	/**
	 * Set Validation 
	 * 1) Its returns Null when Tray is not bind with Case
	 * 2) It return value when Tray is bind with case
	 * 3) To do: When to return validation as when case is already bind with any one case
	 * @param trayID
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping("/searchtraybytraynumber/{trayNumber}")
	public Assembly searchByTrayNumber(
			@PathVariable("trayNumber") String trayNumber) {
		
		// Handle a new guest (if any):
		logger.debug("searchByTrayNumber called");

		logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);

		String trayID = mdirAssemblyDAO.searchByTrayNumber(trayNumber);

		Assembly result = null;

		logger.debug("mdirAssemblyDAO trayID := " + trayID);

		if (trayID != null) {
			Casedetails_assembly caseDetail = mdirAssemblyDAO
					.getCaseAssemblyById(trayID);
			if (caseDetail != null) {
				if (caseDetail.getPreAssembly() == null) {
					trayID = caseDetail.getRefAssembly().getId().toString();
				} else {
					trayID = caseDetail.getPreAssembly().getId().toString();
				}
				if (trayID != null) {
					result = mdirAssemblyDAO.searchByTrayID(trayID);
				}
			}
		}

		return result;
	}	
	/**
	 * Set Validation 
	 * 1) Its returns Null when Tray is not bind with Case
	 * 2) It return value when Tray is bind with case
	 * 3) To do: When to return validation as when case is already bind with any one case
	 * @param trayID
	 * @return
	 */
	@CrossOrigin(origins = "*")
    @RequestMapping("/searchtraybybarcode/{barCodeID}")
    public Assembly searchByTrayBarCode(@PathVariable("barCodeID") String barCodeID) {
        // Handle a new guest (if any):
        logger.debug("searchByTrayBarCode called");
        
        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
                
        String trayID = mdirAssemblyDAO.searchByTrayBarCode(barCodeID);
        
        Assembly result = null;

        logger.debug("mdirAssemblyDAO trayID := " + trayID);
   
        if(trayID != null) {
        	 Casedetails_assembly caseDetail = mdirAssemblyDAO.getCaseAssemblyById(trayID);
        if(caseDetail != null) {
        	if(caseDetail.getPreAssembly() == null) {
        		trayID = caseDetail.getRefAssembly().getId().toString();
        	} else {
        		trayID = caseDetail.getPreAssembly().getId().toString();
        	}
        	if(trayID != null) {
        		result = mdirAssemblyDAO.searchByTrayID(trayID);
        	}
        }
        }
      
        return result;
    }

	/**
	 * Set Validation 
	 * 1) Its returns Null when Tray is bind with Case
	 * 2) It return value when Tray is not bind with case
	 * 3) To do: When to return validation as when case is already bind with any one case
	 * 4) To Do : Need to confirm with Marjan for set condition as Assemble with CASE Relation
	 * @param trayID
	 * @return
	 */
	@CrossOrigin(origins = "*")
    @RequestMapping("/searchtraybynumberforassigntray/{trayNumber}")
    public Assembly searchByTrayNumberForAssignTray(@PathVariable("trayNumber") String trayNumber) {
		
		 // Handle a new guest (if any):
        logger.debug("searchByTrayNumberForAssignTray called trayNumber " + trayNumber);        
        String trayID = mdirAssemblyDAO.searchByTrayNumber(trayNumber);
        Assembly result = null;        
        logger.debug("searchByTrayNumberForAssignTray trayID := " + trayID);
        if(trayID != null) {
        	Casedetails_assembly caseDetail = mdirAssemblyDAO.getCaseAssemblyById(trayID);
        	if(caseDetail == null) {
            	result = mdirAssemblyDAO.searchByTrayID(trayID);
            } 
        }
        return result;
    }
		
	/**
	 * Set Validation 
	 * 1) Its returns Null when Tray is bind with Case
	 * 2) It return value when Tray is not bind with case
	 * 3) To do: When to return validation as when case is already bind with any one case
	 * 4) To Do : Need to confirm with Marjan for set condition as Assemble with CASE Relation
	 * @param trayID
	 * @return
	 */
	@CrossOrigin(origins = "*")
    @RequestMapping("/searchtraybybarcodeforassigntray/{barCodeID}")
    public Assembly searchByTrayBarCodeForAssignTray(@PathVariable("barCodeID") String barCodeID) {
        // Handle a new guest (if any):
        logger.debug("searchByTrayBarCodeForAssignTray called");        
        String trayID = mdirAssemblyDAO.searchByTrayBarCode(barCodeID);
        Assembly result = null;        
        logger.debug("searchByTrayBarCodeForAssignTray trayID := " + trayID);
        if(trayID != null) {
        	Casedetails_assembly caseDetail = mdirAssemblyDAO.getCaseAssemblyById(trayID);
        	if(caseDetail == null) {
            	result = mdirAssemblyDAO.searchByTrayID(trayID);
            } 
        }
        return result;
    }
	
	/**
	 * No Validation message for any cases.
	 * It return always ID of latest barcode either it is PRE,POST,PHASE 2 Assemble
	 * @param barCodeID
	 * @return
	 */
	@CrossOrigin(origins = "*")
    @RequestMapping("/searchtraybybarcodeforassembletray/{barCodeID}")
    public Assembly searchtraybybarcodeforassembletray(@PathVariable("barCodeID") String barCodeID) {
        // Handle a new guest (if any):
        logger.debug("searchtraybybarcodeforassembletray called");        
        String trayID = mdirAssemblyDAO.searchByTrayBarCode(barCodeID);
        Assembly result = null;        
        logger.debug("searchtraybybarcodeforassembletray trayID := " + trayID);
        if(trayID != null) {
           result = mdirAssemblyDAO.searchByTrayID(trayID);
        }
        return result;
    }

	@CrossOrigin(origins = "*")
    @RequestMapping("/getscrewsdetailsbyassemblyid/{trayID}")
    public List<AssemblyDetails> getRelatedAssemblywithRelatedAssemblyDetails(@PathVariable("trayID") String trayID) {
        // Handle a new guest (if any):
        logger.debug("getRelatedAssemblyDetailsWithStatusPresentOthers called");
        
        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
        
        List<AssemblyDetails> result = mdirAssemblyDAO.getRelatedAssemblyDetailsWithStatusPresentOthers(trayID); 
        
        return result;
    }
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/getscrewsdetailsbyremovestatusbyassemblyid/{trayID}")
    public List<AssemblyDetails> getRelatedAssemblywithRelatedAssemblyDetailsbyRemoveStatus(@PathVariable("trayID") String trayID) {
        // Handle a new guest (if any):
        logger.debug("getRelatedAssemblyDetailsWithStatusPresentOthers called");
        
        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
        
        List<AssemblyDetails> result = mdirAssemblyDAO.getRelatedAssemblywithRelatedAssemblyDetailsbyRemoveStatus(trayID); 
        
        return result;
    }
		
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/updatepreimagebyassemblyid/{trayID}", method = RequestMethod.POST, produces = "application/json")
    public String updatePreImageforAssemblyID(HttpServletRequest request, HttpServletResponse response, @RequestParam("file1") MultipartFile picContent, @PathVariable("trayID") String trayID) {
		try {
			
			logger.debug("updatePreImageforAssemblyID called");
	        
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        
	        
	        boolean result = mdirAssemblyDAO.updatePreImageforAssemblyID(trayID, picContent.getBytes()); 
			if(result) {
				return "{\"message\":\"Success\"}";
			}
			return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			
			e.printStackTrace();
			logger.debug("updatePreImageforAssemblyID METHOD EXCEPTION END");
			
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/updatepostimagebyassemblyid/{trayID}", method = RequestMethod.POST, produces = "application/json")
    public String updatePostImageforAssemblyID(HttpServletRequest request, HttpServletResponse response, @RequestParam("file1") MultipartFile picContent, @PathVariable("trayID") String trayID) {
		try {
			logger.debug("updatePostImageforAssemblyID called");
	        
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        
	        boolean result = mdirAssemblyDAO.updatePostImageforAssemblyID(trayID, picContent.getBytes());
	        
			if(result) {
				return "{\"message\":\"Success\"}";
			}
			return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			
			e.printStackTrace();
			logger.debug("updatePostImageforAssemblyID METHOD EXCEPTION END");
			
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/updatedetectedimagebyassemblyid/{trayID}", method = RequestMethod.POST, produces = "application/json")
    public String updateDetectedImageforAssemblyID(HttpServletRequest request, HttpServletResponse response, @RequestParam("file1") MultipartFile picContent, @PathVariable("trayID") String trayID) {
		try {
			logger.debug("updateDetectedImageforAssemblyID called");
	        
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        
	        
	        boolean result = mdirAssemblyDAO.updateDetectedImageforAssemblyID(trayID, picContent.getBytes()); 
			if(result) {
				return "{\"message\":\"Success\"}";
			}
			return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("updateDetectedImageforAssemblyID METHOD EXCEPTION END");
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/updateassemblyimagebyassemblyid/{trayID}", method = RequestMethod.POST, produces = "application/json")
    public String updateAssemblyImageforAssemblyID(HttpServletRequest request, HttpServletResponse response, @RequestParam("file1") MultipartFile picContent, @PathVariable("trayID") String trayID) {
		try {
			logger.debug("updateAssemblyImageforAssemblyID called");
	        
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        
	        
	        boolean result = mdirAssemblyDAO.updateAssemblyImageforAssemblyID(trayID, picContent.getBytes()); 
			if(result) {
				return "{\"message\":\"Success\"}";
			}
			return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("updateAssemblyImageforAssemblyID METHOD EXCEPTION END");
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/getpreimagebyassemblyid/{trayID}", method = RequestMethod.GET, produces = "application/json")
    public String getPreImageforAssemblyID(@PathVariable("trayID") String trayID) {
		try {
			
			logger.debug("getPreImageforAssemblyID called");
	        
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        
	        
	        byte[] result = mdirAssemblyDAO.getPreImageforAssemblyID(trayID); 
			if(result != null) {
				/*InputStream in = new ByteArrayInputStream(result);
				BufferedImage bImageFromConvert = ImageIO.read(in);
				ImageIO.write(bImageFromConvert, "jpg", new File("D:/Aurotech ImageRecognization Backup/out.jpg"));*/
				
				return "{\"data\":\""+Base64.encodeBase64String(result)+"\"}";
			}
			return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("getPreImageforAssemblyID METHOD EXCEPTION END");
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/getpostimagebyassemblyid/{trayID}", method = RequestMethod.GET, produces = "application/json")
    public String getPostImageforAssemblyID(@PathVariable("trayID") String trayID) {
		try {
			
			logger.debug("getPostImageforAssemblyID called");
	        
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        
	        
	        byte[] result = mdirAssemblyDAO.getPostImageforAssemblyID(trayID); 
			if(result != null) {
				return "{\"data\":\""+Base64.encodeBase64String(result)+"\"}";
			}
			return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("getPostImageforAssemblyID METHOD EXCEPTION END");
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/getdetectedimagebyassemblyid/{trayID}", method = RequestMethod.GET, produces = "application/json")
    public String getDetectedImageforAssemblyID(@PathVariable("trayID") String trayID) {
		try {
			
			logger.debug("getdetectedimagebyassemblyid called");
	        
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        
	        
	        byte[] result = mdirAssemblyDAO.getDetectedImageforAssemblyID(trayID); 
			if(result != null) {
				return "{\"data\":\""+Base64.encodeBase64String(result)+"\"}";
			}
			return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("getDetectedImageforAssemblyID METHOD EXCEPTION END");
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/getassemblyimagebyassemblyid/{trayID}", method = RequestMethod.GET, produces = "application/json")
    public String getAssemblyImageforAssemblyID(@PathVariable("trayID") String trayID) {
		try {
			
			logger.debug("getAssemblyImageforAssemblyID called");
	        
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        
	        
	        byte[] result = mdirAssemblyDAO.getAssemblyImageforAssemblyID(trayID); 
			if(result != null) {
				return "{\"data\":\""+Base64.encodeBase64String(result)+"\"}";
			}
			return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("getAssemblyImageforAssemblyID METHOD EXCEPTION END");
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/assignassemblytocase/{assemblyID}/{caseID}", method = RequestMethod.GET, produces = "application/json")
    public String assignAssemblyToCase(@PathVariable("assemblyID") String assemblyID, @PathVariable("caseID") String caseID) {
		try {
			logger.debug("assignAssemblyToCase called");
	        logger.debug("mdirAssemblyDAO := " + mdirAssemblyDAO);
	        logger.debug("mdirCaseDAO := " + mdirCaseDAO);
	        
	        
		    Assembly referenceAssemblyInstance = mdirAssemblyDAO.getAssemblyById(assemblyID);
		    CaseDetails caseInstance = mdirCaseDAO.getCaseByID(caseID);
		    
		    if(caseInstance != null && referenceAssemblyInstance != null) {
		    	if(caseInstance.getId() != null && referenceAssemblyInstance.getId() != null) {
		    		// Validate if entry already exist in casedetails_assembly
		    		Boolean result = false;
		    		result = mdirAssemblyDAO.validateDuplicateInCaseDetailsAssembly(assemblyID, caseID);
		    		if(result) {
		    			Casedetails_assembly casedetails_assemblyInstance = new Casedetails_assembly();
                        casedetails_assemblyInstance.setCaseDetails(caseInstance);
			    		casedetails_assemblyInstance.setPreAssembly(null);
			    		casedetails_assemblyInstance.setPostAssembly(null);
			    		casedetails_assemblyInstance.setRefAssembly(referenceAssemblyInstance);
			    		casedetails_assemblyInstance = mdirAssemblyDAO.persistCaseHasAssembly(casedetails_assemblyInstance);
			    		if(casedetails_assemblyInstance != null && casedetails_assemblyInstance.getId() != null) {
					    	return "{\"message\":\"Success\", \"new_Assigned_Case_Assembly_ID\": \""+casedetails_assemblyInstance.getId()+"\"}";
					    }else {
					    	return "{\"message\":\"Failed\"}";	
					    }
		    		}else {
				    	return "{\"message\":\"Duplicate Record situation with input parameters\"}";	
				   }
		    	}else {
		    		return "{\"message\":\"Case and/or Assembly entities not found\"}";
		    	}
		    }
		    return "{\"message\":\"Failed\"}";
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("assignAssemblyToCase METHOD EXCEPTION END");
			return "{\"message\":\"Failed\"}";
		}
	}
	
	private Product findScrewIDFromAssemblyDetails(List<AssemblyDetails> referenceAssemblyDetailList, String holeNumber, String trayGroup) {
		Product productInstance = null;
		try {
			logger.debug("findScrewIDFromAssemblyDetails called");
			if(referenceAssemblyDetailList != null && referenceAssemblyDetailList.size() > 0) {
				for(int i=0;i<referenceAssemblyDetailList.size();i++) {
					AssemblyDetails referenceAssemblyDetailsInstance = referenceAssemblyDetailList.get(i);
					if(referenceAssemblyDetailsInstance.getHoleNumber().equalsIgnoreCase(holeNumber) 
							&& referenceAssemblyDetailsInstance.getTrayGroup().equalsIgnoreCase(trayGroup)) {
						productInstance = referenceAssemblyDetailsInstance.getScrewId();
						break;
					}
				}
				return productInstance;
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("findScrewIDFromAssemblyDetails METHOD EXCEPTION END");
			productInstance = null;
		}
		return productInstance;
	}	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/createassemblyclone", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
    public Assembly createAssemblyClone(@RequestBody String json, @RequestHeader(value="Authorization") String authString) {
        // Handle a new guest (if any):
        logger.debug("createAssemblyClone json detail : " + json);

//        logger.debug("authorization : " + authString);
        
        try {
		    ObjectMapper mapper = new ObjectMapper();
		    JsonNode root = mapper.readValue(json, JsonNode.class);
		    String referenceAssemblyID = root.get("trayID").getTextValue();
		
		    
		    MDIRUser loginUser = null;
		   
		    
		    if(authString == null) {
		    	logger.debug("authString is null : " + authString);
		    	return null;
		    } else {
		    	loginUser =  idelabUserDAO.getActiveUserBySessionToken(authString);
		    	if(loginUser == null) {
		        	logger.debug("loginUser is null : " + loginUser);
			    	return null;
		    	}
		    }
	    	
		    JsonNode trayBaseLineListVal = root.get("trayBaseline");
		    JsonNode trayBaseLineList = root.get("trayBaseline");

		    Assembly assemblyInstance = new Assembly();
		    
		    if(referenceAssemblyID == null) {
		    	logger.debug("referenceAssemblyID is null : " + referenceAssemblyID);
		    	return null;
		    }
		    
		    for(int j=0;j<trayBaseLineListVal.size();j++) {
		    	String holeNumber = trayBaseLineListVal.get(j).get("holeNumber").getTextValue();
	    		String screwStatus = trayBaseLineListVal.get(j).get("screwStatus").getTextValue();
	    		String trayGroup = trayBaseLineListVal.get(j).get("trayGroup").getTextValue().toString();
	    		
	    		
	    		if(holeNumber == null && screwStatus == null && trayGroup== null) {	
	    			logger.debug("holeNumber : " + holeNumber);
	    			logger.debug("screwStatus : " + screwStatus);
	    			logger.debug("trayGroup : " + trayGroup);
	    			return assemblyInstance;
	    		}
		    }
		    logger.debug("Reference Assembly ID :: "+ referenceAssemblyID);

		    Assembly referenceAssemblyInstance = mdirAssemblyDAO.getAssemblyByIdWithAssemblyDetails(referenceAssemblyID);
		    
		   
		    
		    if(referenceAssemblyInstance != null) {
		    	// Save New Assembly Instance Start
		    	
		    	assemblyInstance.setProduct(referenceAssemblyInstance.getProduct());
		    	assemblyInstance.setDateCreated(new Date());		    			    	
		    	assemblyInstance.setBarCode(referenceAssemblyInstance.getBarCode());
		    	assemblyInstance.setTrayNumber(referenceAssemblyInstance.getTrayNumber());
		    	assemblyInstance.setUser(loginUser);
		    	
		    	assemblyInstance = mdirAssemblyDAO.persistAssembly(assemblyInstance);
		    	// Save New Assembly Instance End
		    	
		    	if(assemblyInstance.getId() != null) {
		    		//Save Assembly Details Start
		    		
//		    		List<AssemblyDetails> referenceAssemblyDetailList = null;
//		    		referenceAssemblyDetailList = referenceAssemblyInstance.getAssemblyDetails();
		    		
		    		for(int i=0;i<trayBaseLineList.size();i++) {
		    		    
		        		String holeNumber = trayBaseLineList.get(i).get("holeNumber").getTextValue();
		        		String screwStatus = trayBaseLineList.get(i).get("screwStatus").getTextValue();
		        		String trayGroup = trayBaseLineList.get(i).get("trayGroup").getTextValue().toString();
		        		
		        	    String productID = null;
		        	    Product referenceProductInstance = null;
		    		    
		    		    if(trayBaseLineList.get(i).get("screwID") != null) {
		    		    	productID =  trayBaseLineList.get(i).get("screwID").getTextValue();	
		    		    }
		    		    if(productID != null) {
		    		    	referenceProductInstance = mdirProductDAO.getProductByID(productID); 
		    		    }
		        		
		        		AssemblyDetails assemblyDetailInstance = new AssemblyDetails();
		        		assemblyDetailInstance.setAssembly(assemblyInstance);
		        		assemblyDetailInstance.setTrayGroup(trayGroup);
		        		assemblyDetailInstance.setHoleNumber(holeNumber);
		        		assemblyDetailInstance.setScrewStatus(screwStatus);
		        		assemblyDetailInstance.setDateRefilled(new Date());
		        		
		        		if(referenceProductInstance != null) {
	        				assemblyDetailInstance.setScrewId(referenceProductInstance);	
	        			}
		        		
//		        		//Find ProductID/ScrewID based on Tray_Group and Hole_Number
//		        		Product referenceProductIDforScrewID = findScrewIDFromAssemblyDetails(referenceAssemblyDetailList, holeNumber, trayGroup);
//		        		if(referenceProductIDforScrewID != null && referenceProductIDforScrewID.getId() != null) {
//		        			assemblyDetailInstance.setScrewId(referenceProductIDforScrewID);
//		        		} 
		        		
		        		mdirAssemblyDAO.persistAssemblyDetails(assemblyDetailInstance);
		        		
		    	    }
		    		//Save Assembly Details End
		    		
			    	return  mdirAssemblyDAO.searchAssemblyTrayID(assemblyInstance.getId().toString());
		    	}
		    	return null;
		    }else {
		    	logger.debug("Assembly entities is not found.");
		    	return assemblyInstance;
		    }
		}catch(Exception e) {
			logger.debug("Exception in createAssemblyClone called. " + e.getMessage());
			return null;
		}
    }
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/saveassembly", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
    public String createPostAssemblyClone(@RequestBody String json, @RequestHeader(value="Authorization") String authString) {
		
		logger.debug("createPostAssemblyClone called");
		
		try {
			boolean result = false;
			
		    ObjectMapper mapper = new ObjectMapper();
		    JsonNode root = mapper.readValue(json, JsonNode.class);
		    String caseID = root.get("caseID").getTextValue();
		    String referenceAssemblyID = root.get("trayID").getTextValue();
		    JsonNode trayBaseLineList = root.get("trayBaseline");
		    MDIRUser loginUser = null;
		    
		    if(authString == null) {
		    	logger.debug("authString is null : " + authString);
		    	return "{\"error\":\"User not authenticated \"}";
		    } else {
		    	loginUser =  idelabUserDAO.getActiveUserBySessionToken(authString);
		    	if(loginUser == null) {
		        	logger.debug("loginUser is null : " + loginUser);
		        	return "{\"error\":\"User not authenticated \"}";
		    	}
		    }
		    		    
		    if(referenceAssemblyID == null ||  caseID == null) {
		    	logger.debug("referenceAssemblyID is null : " + referenceAssemblyID);
		    	logger.debug("caseID is null : " + caseID);
		    	return "{\"message\":\"Case and/or Assembly is null\"}"; 
		    }
		    
		    CaseDetails caseInstance = mdirCaseDAO.getCaseByID(caseID);
		    Assembly referenceAssemblyInstance = mdirAssemblyDAO.
		    		getAssemblyByIdWithAssemblyDetails(referenceAssemblyID);
		    
		    if(caseInstance != null && referenceAssemblyInstance != null) {
		    	Casedetails_assembly casedetails_assemblyInstance = 
		    			mdirAssemblyDAO.getCaseAssemblyById(referenceAssemblyID, caseID);
		    	if(casedetails_assemblyInstance != null) {
		    			
		    		logger.debug("casedetails_assemblyInstance  : " + casedetails_assemblyInstance.getId().toString());
		    			
		    		// Save New Assembly Instance Start
			    	Assembly assemblyInstance = new Assembly();
			    	assemblyInstance.setProduct(referenceAssemblyInstance.getProduct());
			    	assemblyInstance.setDateCreated(new Date());
			    	assemblyInstance.setBarCode(referenceAssemblyInstance.getBarCode());
			    	assemblyInstance.setTrayNumber(referenceAssemblyInstance.getTrayNumber());
			    	assemblyInstance.setAssemblyImage(referenceAssemblyInstance.getAssemblyImage());
			    	assemblyInstance.setUser(loginUser);
			    	assemblyInstance = mdirAssemblyDAO.persistAssembly(assemblyInstance);
			    	// Save New Assembly Instance End
			    	
			    	if(assemblyInstance.getId() != null) {
			    		
			    		//Save Assembly Details Start
			    		List<AssemblyDetails> referenceAssemblyDetailList = null;
			    		referenceAssemblyDetailList = referenceAssemblyInstance.getAssemblyDetails();
				    	
			    		for(int i=0;i<trayBaseLineList.size();i++) {
							    
					    		String holeNumber = trayBaseLineList.get(i).get("HOLE_NUMBER").getTextValue();
					    		String screwStatus = trayBaseLineList.get(i).get("SCREW_STATUS").getTextValue();
					    		String trayGroup = trayBaseLineList.get(i).get("TRAY_GROUP").getNumberValue().toString();
					    		
					    		AssemblyDetails assemblyDetailInstance = new AssemblyDetails();
					    		assemblyDetailInstance.setAssembly(assemblyInstance);
					    		assemblyDetailInstance.setTrayGroup(trayGroup);
					    		assemblyDetailInstance.setHoleNumber(holeNumber);
					    		assemblyDetailInstance.setScrewStatus(screwStatus);
					    		assemblyDetailInstance.setDateRefilled(new Date());
					    		
					    		//Find ProductID/ScrewID based on Tray_Group and Hole_Number
					    		Product referenceProductIDforScrewID = findScrewIDFromAssemblyDetails(referenceAssemblyDetailList, holeNumber, trayGroup);
					    		
					    		if(referenceProductIDforScrewID != null && referenceProductIDforScrewID.getId() != null) {
					    			assemblyDetailInstance.setScrewId(referenceProductIDforScrewID);
					    		}					    		
					    		mdirAssemblyDAO.persistAssemblyDetails(assemblyDetailInstance);
					    		
						    }
				    	//Save Assembly Details End
				    				    		
			    		//Update Post_Assembly_ID in Casedetails_Assembly table based on Case_ID and Pre_Assembly_ID
			    		result =  mdirCaseDAO.updatePostAssemblyIDByCaseandPreAssemblyID(
			    				casedetails_assemblyInstance.getId().toString(), 
			    				casedetails_assemblyInstance.getRefAssembly().getId().toString(),
			    				assemblyInstance 
			    				);
			    		
 			    		if(result) {
 					    	return "{\"message\":\"Success\", \"newAssemblyID\": \""+assemblyInstance.getId()+"\"}";
 					    }else {
 					    	return "{\"message\":\"Failed to update Case Detail Assemble\"}";	
 					    }
			    	} else {
			    		return "{\"message\":\"Failed to create Assembly clone\"}";	
			    	}	
		    	
		    	} else {
		    		return "{\"message\":\"Case with Assembly entities is not found\"}";
		    	}
		    }else {
		    	return "{\"message\":\"Refrence Case and/or Refrence Assembly entities is not found\"}"; 
		    }
		}catch(Exception e) {
			logger.debug("Exception in saveassembly/createPostAssemblyClone called. " + e.getMessage());
			return "{\"message\":\"Failed\"}";
		}
	}
	
	@CrossOrigin(origins = "*")
    @RequestMapping(value="/createpreassemblyclone", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
    public String createPreAssemblyClone(@RequestBody String json, @RequestHeader(value="Authorization") String authString) {
		
		logger.debug("createPreAssemblyClone called");
		
		try {
			boolean result = false;
			
		    ObjectMapper mapper = new ObjectMapper();
		    JsonNode root = mapper.readValue(json, JsonNode.class);
		    String caseID = root.get("caseID").getTextValue();
		    String referenceAssemblyID = root.get("trayID").getTextValue();
		    JsonNode trayBaseLineList = root.get("trayBaseline");
		    MDIRUser loginUser = null;
		    
		    if(authString == null) {
		    	logger.debug("authString is null : " + authString);
		    	return "{\"error\":\"User not authenticated \"}";
		    } else {
		    	loginUser =  idelabUserDAO.getActiveUserBySessionToken(authString);
		    	if(loginUser == null) {
		        	logger.debug("loginUser is null : " + loginUser);
		        	return "{\"error\":\"User not authenticated \"}";
		    	}
		    }
		    
		    if(referenceAssemblyID == null ||  caseID == null) {
		    	logger.debug("referenceAssemblyID is null : " + referenceAssemblyID);
		    	logger.debug("caseID is null : " + caseID);
		    	return "{\"message\":\"Case and/or Assembly is null\"}"; 
		    }

		    CaseDetails caseInstance = mdirCaseDAO.getCaseByID(caseID);
		    Assembly referenceAssemblyInstance = mdirAssemblyDAO.
		    		getAssemblyByIdWithAssemblyDetails(referenceAssemblyID);
		    
		    if(caseInstance != null && referenceAssemblyInstance != null) {
	 		    		Casedetails_assembly casedetails_assemblyInstance = mdirAssemblyDAO.
	 		    				getCaseAssemblyById(referenceAssemblyID, caseID);
	 		    		if(casedetails_assemblyInstance != null) {
	 		    			
	 		    			// Save New Assembly Instance Start
	 				    	Assembly assemblyInstance = new Assembly();
	 				    	assemblyInstance.setProduct(referenceAssemblyInstance.getProduct());
	 				    	assemblyInstance.setDateCreated(new Date());
	 				    	assemblyInstance.setBarCode(referenceAssemblyInstance.getBarCode());
	 				    	assemblyInstance.setTrayNumber(referenceAssemblyInstance.getTrayNumber());
	 				    	assemblyInstance.setUser(loginUser);
	 				    	assemblyInstance = mdirAssemblyDAO.persistAssembly(assemblyInstance);
	 				    	// Save New Assembly Instance End
	 				    	
	 				    	if(assemblyInstance.getId() != null) {
	 				    		//Save Assembly Details Start
	 					    	
	 				    		List<AssemblyDetails> referenceAssemblyDetailList = null;
	 				    		referenceAssemblyDetailList = referenceAssemblyInstance.getAssemblyDetails();

	 				    		for(int i=0;i<trayBaseLineList.size();i++) {
	 							    
	 					    		String holeNumber = trayBaseLineList.get(i).get("HOLE_NUMBER").getTextValue();
	 					    		String screwStatus = trayBaseLineList.get(i).get("SCREW_STATUS").getTextValue();
	 					    		String trayGroup = trayBaseLineList.get(i).get("TRAY_GROUP").getTextValue().toString();
	 					    		
	 					    		AssemblyDetails assemblyDetailInstance = new AssemblyDetails();
	 					    		assemblyDetailInstance.setAssembly(assemblyInstance);
	 					    		assemblyDetailInstance.setTrayGroup(trayGroup);
	 					    		assemblyDetailInstance.setHoleNumber(holeNumber);
	 					    		assemblyDetailInstance.setScrewStatus(screwStatus);
	 					    		assemblyDetailInstance.setDateRefilled(new Date());
	 					    		
	 					    		//Find ProductID/ScrewID based on Tray_Group and Hole_Number
	 					    		Product referenceProductIDforScrewID = findScrewIDFromAssemblyDetails(referenceAssemblyDetailList, holeNumber, trayGroup);
	 					    		
	 					    		if(referenceProductIDforScrewID != null && referenceProductIDforScrewID.getId() != null) {
	 					    			assemblyDetailInstance.setScrewId(referenceProductIDforScrewID);
	 					    		}	 					    		
	 					    		mdirAssemblyDAO.persistAssemblyDetails(assemblyDetailInstance);
	 					    		
	 						    }
	 					    	//Save Assembly Details End
	 				    	
	 				    	} else {
	 				    		return "{\"message\":\"Failed to create Assembly clone\"}";	
	 				    	}	 			    		
	 		    			result =  mdirCaseDAO.updatePreAssemblyIDByCaseandNewAssemblyID(casedetails_assemblyInstance.getId().toString(), 
	 			    				casedetails_assemblyInstance.getRefAssembly().getId().toString(), assemblyInstance);
	 			    		if(result) {
	 					    	return "{\"message\":\"Success\", \"new_Assigned_ID\": \""+assemblyInstance.getId()+"\"}";
	 					    }else {
	 					    	return "{\"message\":\"Failed to update Case Detail Assemble\"}";	
	 					    }
	 		    		}else {
	 				    	return "{\"message\":\"Case with Assembly entities is not found\"}";	
	 				   }
		    }else {
		    	return "{\"message\":\"Reference Case and/or Refrence Assembly entities are not found\"}"; 
		    }
		 
		}catch(Exception e) {
			logger.debug("Exception in createPreAssemblyClone called. " + e.getMessage());
			return "{\"message\":\"Failed\"}";
		}
	}
	
	

}
