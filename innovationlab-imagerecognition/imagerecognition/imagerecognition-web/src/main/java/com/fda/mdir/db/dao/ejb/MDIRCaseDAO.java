package com.fda.mdir.db.dao.ejb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.fda.mdir.db.entities.CaseDetails;
import com.fda.mdir.db.entities.Casedetails_assembly;
import com.fda.mdir.db.entities.SurgeryType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRCaseDAO {
	private static final Logger logger = LoggerFactory.getLogger(MDIRCaseDAO.class);

	@PersistenceContext
	private EntityManager em;

	public <T> void persist(T t) {

		em.persist(t);

	}
	
	public CaseDetails getCaseByID(String caseID) {
		CaseDetails caseDetailInstance = null;
		try {
			caseDetailInstance = em.find(CaseDetails.class, Long.valueOf(caseID));
		}catch(Exception e) {
			logger.debug("Exception in getCaseByID " + e.toString());
			caseDetailInstance = null;
		}
		return caseDetailInstance;
	}

	public List<String> getAllSurgeons() {
	

		List<String> result = null;
		Query query = em.createNativeQuery("SELECT DISTINCT surgeonName FROM casedetails");
		List<Object> caseDetailsRows = query.getResultList();
		if (caseDetailsRows != null) {
			result = new ArrayList<String>();
			for (Object caseDetails : caseDetailsRows) {
				result.add(caseDetails.toString());
			}
		}
		return result;
	}

	public List<String> getAllSurgeonsByPatientID(String patientID) {
		

		List<String> result = null;
		Query query = em.createNativeQuery("SELECT DISTINCT surgeonName FROM casedetails where PATIENT_ID = ?1 ");
		query.setParameter(1, patientID);
		List<Object> caseDetailsRows = query.getResultList();
		if (caseDetailsRows != null) {
			result = new ArrayList<String>();
			for (Object caseDetails : caseDetailsRows) {
				result.add(caseDetails.toString());
			}
		}
		return result;
	}

	public List<String> getDateByPatientIDBySurgeonName(String patientID, String surgeonName) {
		List<String> result = null;
		Query query = em.createNativeQuery(
				"SELECT DISTINCT caseDate FROM casedetails where PATIENT_ID = ?1 AND surgeonName LIKE  ?2 ");
		query.setParameter(1, patientID);
		query.setParameter(2, surgeonName);
		List<Object> caseDetailsRows = query.getResultList();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (caseDetailsRows != null && caseDetailsRows.size() > 0) {
			result = new ArrayList<String>();
			try {
				for (Object caseDetails : caseDetailsRows) {
					Date tempDate = dateFormat.parse(caseDetails.toString());
					result.add(dateFormat1.format(tempDate));
				}
			}catch(Exception e) {
				logger.debug("Exception in getDateByPatientIDBySurgeonName" + e.toString());
			}
		}
		return result;
	}

	public List<SurgeryType> getRelatedSurgeryType(String patientID, String surgeonName, String caseDate) {
		TypedQuery<CaseDetails> query = em.createQuery("SELECT n FROM CaseDetails n WHERE n.patient.id = :patientID AND n.surgeonName like :surgeonName AND n.caseDate = :inputDate",CaseDetails.class);
		query.setParameter("patientID", Long.valueOf(patientID));
		query.setParameter("surgeonName", surgeonName);
		// java.util.Date temp = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSSSS").parse("2012-07-10 14:58:00.000000");
		Date temp = null;
		try {
			temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(caseDate);
			query.setParameter("inputDate", temp);
			List<CaseDetails> caseDetailsRows = query.getResultList();
			List<SurgeryType> result = null;
			if (caseDetailsRows != null && caseDetailsRows.size() > 0) {
				result = new ArrayList<SurgeryType>();
				for (CaseDetails caseDetails : caseDetailsRows) {
					result.add(caseDetails.getSurgeryType());
				}
			}
			return result;
		} catch (ParseException e) {
			logger.debug("Exception in getRelatedSurgeryType" + e.toString());
		}
		return null;
		
	}
	
	public List<Assembly> getRelatedAssemblies(String patientID, String surgeonName, String caseDate, String surgeryTypeID) {
		TypedQuery<CaseDetails> query = em.createQuery("SELECT n FROM CaseDetails n WHERE n.patient.id = :patientID AND "
				+ "n.surgeonName like :surgeonName AND n.caseDate = :inputDate AND n.surgeryType.id = :surgeryTypeID ",CaseDetails.class);
		
		// java.util.Date temp = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSSSS").parse("2012-07-10 14:58:00.000000");
		Date temp = null;
		try {
			temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(caseDate);
			query.setParameter("patientID", Long.valueOf(patientID));
			query.setParameter("surgeonName", surgeonName);
			query.setParameter("inputDate", temp);
			query.setParameter("surgeryTypeID", Long.valueOf(surgeryTypeID));
			List<CaseDetails> caseDetailsRows = query.getResultList();
			List<Assembly> result = null;
			List<Casedetails_assembly> caseAssemlyList = null;
			if (caseDetailsRows != null && caseDetailsRows.size() > 0) {
				CaseDetails uniqueCaseDetailsInstance = caseDetailsRows.get(0);
				caseAssemlyList = uniqueCaseDetailsInstance.getCasedetailsAssembly();
				if (caseAssemlyList != null && caseAssemlyList.size() > 0) {
					result = new ArrayList<Assembly>();
					for(Casedetails_assembly caseAssemly : caseAssemlyList) {
						if(caseAssemly.getPreAssembly() == null) {
							Assembly assObj = caseAssemly.getRefAssembly();
							//assObj.getPreAssembly();
							assObj.getAssemblyDetails();
							assObj.getProduct().getProductDetails();
							result.add(assObj);	
						} else {
							Assembly assObj = caseAssemly.getPreAssembly();
							//assObj.getPreAssembly();
							assObj.getAssemblyDetails();
							assObj.getProduct().getProductDetails();
							result.add(assObj);	
						}
						
					}
				}
			}
			return result;
		} catch (ParseException e) {
			logger.debug("Exception in getRelatedAssemblies" + e.toString());
		}
		return null;
	}
	
	public CaseDetails getRelatedCaseDetails(String patientID, String surgeonName, String caseDate, String surgeryTypeID) {
		TypedQuery<CaseDetails> query = 
				em.createQuery("SELECT n FROM CaseDetails n WHERE n.patient.id = :patientID AND "
				+ "n.surgeonName like :surgeonName AND n.caseDate = :inputDate "
				+ "AND n.surgeryType.id = :surgeryTypeID ",CaseDetails.class);
		
		// java.util.Date temp = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSSSS").parse("2012-07-10 14:58:00.000000");
		Date temp = null;
		CaseDetails uniqueCaseDetailsInstance = null;
		try {
			temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(caseDate);
			query.setParameter("patientID", Long.valueOf(patientID));
			query.setParameter("surgeonName", surgeonName);
			query.setParameter("inputDate", temp);
			query.setParameter("surgeryTypeID", Long.valueOf(surgeryTypeID));
			List<CaseDetails> caseDetailsRows = query.getResultList();
			if (caseDetailsRows != null && caseDetailsRows.size() > 0) {
				uniqueCaseDetailsInstance = caseDetailsRows.get(0);
			}
			return uniqueCaseDetailsInstance;
		} catch (ParseException e) {
			logger.debug("Exception in getRelatedCaseDetails" + e.toString());
		}
		return null;
	}
	

	public boolean updatePostAssemblyIDByCaseandPreAssemblyID(String caseID, String refAssemblyId, Assembly newAssemblyInstance) {
		try {
			TypedQuery <Casedetails_assembly> query = em.createQuery("UPDATE Casedetails_assembly n SET n.postAssembly=:postAssemblyID"
					+ " where n.id = :caseID AND n.refAssembly.id = :refAssembly ", Casedetails_assembly.class);
			
			
			
			if(newAssemblyInstance != null && newAssemblyInstance.getId() != null) {
				
				logger.debug("newAssemblyInstance.getId() " + newAssemblyInstance.getId().toString());
				logger.debug("caseID " + caseID);
				logger.debug("refAssemblyId " + refAssemblyId);
				
				query.setParameter("postAssemblyID", newAssemblyInstance);
				query.setParameter("caseID", Long.valueOf(caseID));
				query.setParameter("refAssembly", Long.valueOf(refAssemblyId));
				int updateCount = query.executeUpdate();
				if(updateCount == 1) {
					return true;
				}
			}
			return false;
		}catch(Exception e) {
			logger.debug("Exception in updatePostAssemblyIDByCaseandPreAssemblyID " + e.toString());
			return false;
		}
	}
	
	public boolean updatePreAssemblyIDByCaseandNewAssemblyID(String caseassemleid, String refAssemblyId, Assembly newAssemblyInstance) {
		try {			
			TypedQuery <Casedetails_assembly> query = em.createQuery(
					"UPDATE Casedetails_assembly n SET n.preAssembly=:preAssemblyId"
					+ " where n.id = :caseassemleid  AND n.refAssembly.id = :refAssembly", Casedetails_assembly.class);
			query.setParameter("preAssemblyId", newAssemblyInstance);
			query.setParameter("caseassemleid", Long.valueOf(caseassemleid));
			query.setParameter("refAssembly",  Long.valueOf(refAssemblyId));
			
			if( query.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
		}catch(Exception e) {
			logger.debug("Exception in updatePreAssemblyIDByCaseandNewAssemblyID " + e.toString());
			return false;
		}
		
	}
	
	@PreDestroy
	private void beforeGoing() {
		logger.debug("MDIRCaseDAO going good bye");
	}
}
