package com.fda.mdir.db.dao.ejb;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fda.mdir.db.entities.Assembly;
import com.fda.mdir.db.entities.AssemblyDetails;
import com.fda.mdir.db.entities.CaseDetails;
import com.fda.mdir.db.entities.Casedetails_assembly;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRAssemblyDAO {
	private static final Logger logger = LoggerFactory.getLogger(MDIRAssemblyDAO.class);

	@PersistenceContext
	private EntityManager em;

	public <T> void persist(T t) {

			em.persist(t);

	}
	
	public Assembly persistAssembly(Assembly assemblyInstance) {
		try {
			em.persist(assemblyInstance);
			em.flush();
			return assemblyInstance;
		}catch(Exception e) {	
			logger.debug("Exception in persistAssembly " + e.toString());
			return null;
		}
	}
	
	public AssemblyDetails persistAssemblyDetails(AssemblyDetails assemblyDetailsInstance) {
		try {
			em.persist(assemblyDetailsInstance);
			em.flush();
			return assemblyDetailsInstance;
		}catch(Exception e) {
			logger.debug("Exception in persistAssemblyDetails " + e.toString());
			return null;
		}
	}
	
	public Casedetails_assembly persistCaseHasAssembly(Casedetails_assembly casedetails_assemblyInstance) {
		try {
			em.persist(casedetails_assemblyInstance);
			em.flush();
			return casedetails_assemblyInstance;
		}catch(Exception e) {
			logger.debug("Exception in persistCaseHasAssembly " + e.toString());
			return null;
		}
	}
	
	public Assembly getAssemblyById(String assemblyID) {
		Assembly assemblyInstance = null;
		try {
			assemblyInstance = em.find(Assembly.class, Long.valueOf(assemblyID));
		}catch(Exception e) {
			logger.debug("Exception in getAssemblyById " + e.toString());
			assemblyInstance = null;
		}
		return assemblyInstance;
	}
	
	public Assembly getAssemblyByIdWithAssemblyDetails(String assemblyID) {
		Assembly assemblyInstance = null;
		try {
			assemblyInstance = em.find(Assembly.class, Long.valueOf(assemblyID));
			assemblyInstance.getAssemblyDetails();
		}catch(Exception e) {
			logger.debug("Exception in getAssemblyByIdWithAssemblyDetails " + e.toString());
			assemblyInstance = null;
		}
		return assemblyInstance;
	}
	

	
	public Assembly searchByTrayID(String trayID) {

        TypedQuery <Assembly> query = em.createQuery("SELECT n FROM Assembly n where n.id = :trayID", Assembly.class);

        query.setParameter("trayID", Long.valueOf(trayID));
        
        List<Assembly> trayRows = query.getResultList();
        
        Assembly result = null;
        
        if(trayRows != null && trayRows.size() > 0) {
        	result = trayRows.get(0);
        	result.getPreAssembly();
        	result.getPostAssembly();
        	result.getRefAssembly();
        	result.getAssemblyDetails();
        	result.getProduct().getProductDetails();
        }
        return result;
    }
	
	public String searchByTrayBarCode(String trayBarCode) {
		String result = null;

		TypedQuery <Assembly> query = em.createQuery("SELECT n FROM Assembly n where n.barCode like :barcodeID "
		+ " order by n.dateCreated DESC ", Assembly.class);

	        query.setParameter("barcodeID", trayBarCode);
	        
	        List<Assembly> trayRows = query.getResultList();
	        
	        if(trayRows != null && trayRows.size() > 0) {
	        	result = trayRows.get(0).getId().toString();
	        } 	        
		return result;
    }
	
	public String searchByTrayNumber(String trayNumber) {
		String result = null;

		TypedQuery <Assembly> query = em.createQuery("SELECT n FROM Assembly n where n.trayNumber like :trayNumber "
		+ " order by n.dateCreated DESC ", Assembly.class);

	        query.setParameter("trayNumber", trayNumber);
	        
	        List<Assembly> trayRows = query.getResultList();
	        
	        if(trayRows != null && trayRows.size() > 0) {
	        	result = trayRows.get(0).getId().toString();
	        } 	        
		return result;
    }
	
	public Assembly searchAssemblyTrayID(String trayID) {

        TypedQuery <Assembly> query = em.createQuery("SELECT n FROM Assembly n where n.id = :trayID", Assembly.class);

        query.setParameter("trayID", Long.valueOf(trayID));
        
        List<Assembly> trayRows = query.getResultList();
        
        Assembly result = null;
        
        if(trayRows != null && trayRows.size() > 0) {
        	result = trayRows.get(0);
        	result.getAssemblyDetails();
        	result.getProduct().getProductDetails();
        }
        return result;
    }
	
	public Boolean validateDuplicateInCaseDetailsAssembly(String assemblyId, String casedetailsId) {


        
		
        TypedQuery <Casedetails_assembly> query = em.createQuery("SELECT n FROM Casedetails_assembly n where "
        		+ "n.caseDetails.id = :casedetailsId AND n.refAssembly.id = :refAssemblyId ", Casedetails_assembly.class);
        
        query.setParameter("refAssemblyId", Long.valueOf(assemblyId));
        query.setParameter("casedetailsId", Long.valueOf(casedetailsId));
        
        List<Casedetails_assembly> trayRows = query.getResultList();
        
        if(trayRows != null && trayRows.size() == 0) {
        	return true;
        }
        return false;
    }
	
	
	public Casedetails_assembly getCaseAssemblyById(String assemblyId, String casedetailId) {
		
        TypedQuery <Casedetails_assembly> query = em.createQuery("SELECT n FROM Casedetails_assembly n "
        		+ "where n.caseDetails.id = :caseDetailId AND (n.preAssembly.id = :preAssemblyId "
        		+ "OR n.refAssembly.id = :refAssemblyId)", Casedetails_assembly.class);

        query.setParameter("preAssemblyId", Long.valueOf(assemblyId));
        query.setParameter("refAssemblyId", Long.valueOf(assemblyId));
        query.setParameter("caseDetailId", Long.valueOf(casedetailId));
                
        List<Casedetails_assembly> trayRows = query.getResultList();
        
        if(trayRows != null && trayRows.size() > 0) {
        	
        	 logger.debug("Get Casedetails_assembly Tray Rows size " + trayRows.size());
        	
        	return trayRows.get(0);
        }
        return null;
    }
	


	
	public Casedetails_assembly getCaseAssemblyById(String assemblyId) {
        
		try {
        TypedQuery <Casedetails_assembly> query = em.createQuery("SELECT n FROM Casedetails_assembly n "
        		+ "where n.preAssembly.id = :preAssemblyId OR ( n.preAssembly.id IS NULL AND "
        		+ "n.refAssembly.id = :refAssembly) ", Casedetails_assembly.class);

        query.setParameter("refAssembly", Long.valueOf(assemblyId));
        query.setParameter("preAssemblyId", Long.valueOf(assemblyId));

        List<Casedetails_assembly> trayRows = query.getResultList();
        
        logger.debug("getCaseAssemblyById := " + trayRows.size());
        
        if(trayRows != null && trayRows.size() > 0) {
        	
        	logger.debug("Get getCaseAssemblyById BY ID " + trayRows.size());
        	
        	return trayRows.get(0);
        }
		}catch(Exception e) {
			logger.debug("Get getCaseAssemblyById BY ID Exceptin " + e.getMessage());
		}
        return null;
    }
	
	public List<AssemblyDetails> getRelatedAssemblyDetailsWithStatusPresentOthers(String trayID) {

		List<String> screwStatuses = Arrays.asList("Present", "Other");
		
        TypedQuery <AssemblyDetails> query = em.createQuery("SELECT n FROM AssemblyDetails n where n.assembly.id = :trayID AND n.screwStatus IN :screwStatuses", AssemblyDetails.class);

        query.setParameter("trayID", Long.valueOf(trayID));
        query.setParameter("screwStatuses", screwStatuses);
        
        List<AssemblyDetails> assemblyDetailsRows = null; 
        assemblyDetailsRows = query.getResultList();
        
        return assemblyDetailsRows;
    }
	
	public List<AssemblyDetails> getRelatedAssemblyDetailsWithStatusPresent(String trayID) {

		List<String> screwStatuses = Arrays.asList("Present");
		
        TypedQuery <AssemblyDetails> query = em.createQuery("SELECT n FROM AssemblyDetails n where n.assembly.id = :trayID AND n.screwStatus IN :screwStatuses", AssemblyDetails.class);

        query.setParameter("trayID", Long.valueOf(trayID));
        query.setParameter("screwStatuses", screwStatuses);
        
        List<AssemblyDetails> assemblyDetailsRows = null; 
        assemblyDetailsRows = query.getResultList();
        
        return assemblyDetailsRows;
    }
	
	public List<AssemblyDetails> getRelatedAssemblywithRelatedAssemblyDetailsbyRemoveStatus(String trayID) {

		List<String> screwStatuses = Arrays.asList("Removed");
		
        TypedQuery <AssemblyDetails> query = em.createQuery("SELECT n FROM AssemblyDetails n where n.assembly.id = :trayID AND n.screwStatus IN :screwStatuses", AssemblyDetails.class);

        query.setParameter("trayID", Long.valueOf(trayID));
        query.setParameter("screwStatuses", screwStatuses);
        
        List<AssemblyDetails> assemblyDetailsRows = null; 
        assemblyDetailsRows = query.getResultList();
        
        return assemblyDetailsRows;
    }
	
	public boolean updatePreImageforAssemblyID(String trayID, byte[] imageData) {
		try {
			TypedQuery <Assembly> query = em.createQuery("UPDATE Assembly n SET n.preImage=:imageData where n.id = :trayID", Assembly.class);
			query.setParameter("imageData", imageData);
			query.setParameter("trayID", Long.valueOf(trayID));
			
			int updateCount = query.executeUpdate();
			if(updateCount == 1) {
				return true;
			}
			return false;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public boolean updatePostImageforAssemblyID(String trayID, byte[] imageData) {
		try {
			TypedQuery <Assembly> query = em.createQuery("UPDATE Assembly n SET n.postImage=:imageData where n.id = :trayID", Assembly.class);
			query.setParameter("imageData", imageData);
			query.setParameter("trayID", Long.valueOf(trayID));
			
			int updateCount = query.executeUpdate();
			if(updateCount == 1) {
				return true;
			}
			return false;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public boolean updateDetectedImageforAssemblyID(String trayID, byte[] imageData) {
		try {
			TypedQuery <Assembly> query = em.createQuery("UPDATE Assembly n SET n.detectedImage=:imageData where n.id = :trayID", Assembly.class);
			query.setParameter("imageData", imageData);
			query.setParameter("trayID", Long.valueOf(trayID));
			
			int updateCount = query.executeUpdate();
			if(updateCount == 1) {
				return true;
			}
			return false;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public boolean updateAssemblyImageforAssemblyID(String trayID, byte[] imageData) {
		try {
			TypedQuery <Assembly> query = em.createQuery("UPDATE Assembly n SET n.assemblyImage = :imageData "
					+ "where n.id=:trayID", Assembly.class);
			query.setParameter("imageData", imageData);
			query.setParameter("trayID", Long.valueOf(trayID));
			
			int updateCount = query.executeUpdate();
			if(updateCount == 1) {
				return true;
			}
			return false;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public byte[] getPreImageforAssemblyID(String trayID) {
		try {
			byte[] result = null;
			Query query = em.createNativeQuery("SELECT preImage from Assembly where id = ?1");
			query.setParameter(1, trayID);
			List<Object> assemblyRows = query.getResultList();
			if (assemblyRows != null && assemblyRows.size() == 1) {
				result = (byte[])assemblyRows.get(0);
			}
			return result;
		}catch(Exception e) {
			logger.debug("Exception in getPreImageforAssemblyID " + e.toString());
			return null;
		}
	}
	
	public byte[] getPostImageforAssemblyID(String trayID) {
		try {
			byte[] result = null;
			Query query = em.createNativeQuery("SELECT postImage from Assembly where id = ?1");
			query.setParameter(1, trayID);
			List<Object> assemblyRows = query.getResultList();
			if (assemblyRows != null && assemblyRows.size() == 1) {
				result = (byte[])assemblyRows.get(0);
			}
			return result;
		}catch(Exception e) {
			logger.debug("Exception in getPostImageforAssemblyID " + e.toString());
			return null;
		}
	}
	
	public byte[] getDetectedImageforAssemblyID(String trayID) {
		try {
			byte[] result = null;
			Query query = em.createNativeQuery("SELECT detectedImage from Assembly where id = ?1");
			query.setParameter(1, trayID);
			List<Object> assemblyRows = query.getResultList();
			if (assemblyRows != null && assemblyRows.size() == 1) {
				result = (byte[])assemblyRows.get(0);
			}
			return result;
		}catch(Exception e) {
			logger.debug("Exception in getDetectedImageforAssemblyID " + e.toString());
			return null;
		}
	}
	
	public byte[] getAssemblyImageforAssemblyID(String trayID) {
		try {
			byte[] result = null;
			Query query = em.createNativeQuery("SELECT assemblyImage from Assembly where id = ?1");
			query.setParameter(1, trayID);
			List<Object> assemblyRows = query.getResultList();
			if (assemblyRows != null && assemblyRows.size() == 1) {
				result = (byte[])assemblyRows.get(0);
			}
			return result;
		}catch(Exception e) {
			logger.debug("Exception in getAssemblyImageforAssemblyID " + e.toString());
			return null;
		}
	}
	
	
	@PreDestroy
	private void beforeGoing() {
		logger.debug("MDIRAssemblyDAO going good bye");
	}

}
