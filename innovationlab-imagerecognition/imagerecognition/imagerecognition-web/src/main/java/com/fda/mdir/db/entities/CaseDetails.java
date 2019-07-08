package com.fda.mdir.db.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fda.mdir.utils.JsonDateSerializer;

@Entity
@Table(name = "CaseDetails")
public class CaseDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn
	private Patient patient;

	@ManyToOne
	@JoinColumn
	private SurgeryType surgeryType;

	private String surgeonName;

	private String hospitalID;

	private String hospitalName;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date caseDate;

	@OneToMany(mappedBy = "caseDetails")
	private List<Casedetails_assembly> casedetailsAssembly;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public SurgeryType getSurgeryType() {
		return surgeryType;
	}

	public void setSurgeryType(SurgeryType surgeryType) {
		this.surgeryType = surgeryType;
	}

	public String getSurgeonName() {
		return surgeonName;
	}

	public void setSurgeonName(String surgeonName) {
		this.surgeonName = surgeonName;
	}

	public String getHospitalID() {
		return hospitalID;
	}

	public void setHospitalID(String hospitalID) {
		this.hospitalID = hospitalID;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Date getCaseDate() {
		return caseDate;
	}

	public void setCaseDate(Date caseDate) {
		this.caseDate = caseDate;
	}

	public List<Casedetails_assembly> getCasedetailsAssembly() {
		return casedetailsAssembly;
	}

	public void setCasedetailsAssembly(List<Casedetails_assembly> casedetailsAssembly) {
		this.casedetailsAssembly = casedetailsAssembly;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caseDate == null) ? 0 : caseDate.hashCode());
		result = prime * result + ((hospitalID == null) ? 0 : hospitalID.hashCode());
		result = prime * result + ((hospitalName == null) ? 0 : hospitalName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((surgeonName == null) ? 0 : surgeonName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaseDetails other = (CaseDetails) obj;
		if (caseDate == null) {
			if (other.caseDate != null)
				return false;
		} else if (!caseDate.equals(other.caseDate))
			return false;
		
		if (hospitalID == null) {
			if (other.hospitalID != null)
				return false;
		} else if (!hospitalID.equals(other.hospitalID))
			return false;
		if (hospitalName == null) {
			if (other.hospitalName != null)
				return false;
		} else if (!hospitalName.equals(other.hospitalName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		if (surgeonName == null) {
			if (other.surgeonName != null)
				return false;
		} else if (!surgeonName.equals(other.surgeonName))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "CaseDetails[ id=" + id + " ]";
	}
}
