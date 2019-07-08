package com.fda.mdir.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Table(name = "casedetails_assembly")
@Entity
public class Casedetails_assembly {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CASEDETAIL_ID")
	private CaseDetails caseDetails;

	@ManyToOne
	@JoinColumn(name = "PRE_ASSEMBLY_ID")
	@JsonIgnore
	@com.fasterxml.jackson.annotation.JsonIgnore
	private Assembly preAssembly;

	@ManyToOne
	@JoinColumn(name = "POST_ASSEMBLY_ID")
	@JsonIgnore
	@com.fasterxml.jackson.annotation.JsonIgnore
	private Assembly postAssembly;

	@ManyToOne
	@JoinColumn(name = "REF_ASSEMBLY_ID")
	@JsonIgnore
	@com.fasterxml.jackson.annotation.JsonIgnore
	private Assembly refAssembly;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CaseDetails getCaseDetails() {
		return caseDetails;
	}

	public void setCaseDetails(CaseDetails caseDetails) {
		this.caseDetails = caseDetails;
	}

	public Assembly getPreAssembly() {
		return preAssembly;
	}

	public void setPreAssembly(Assembly preAssembly) {
		this.preAssembly = preAssembly;
	}

	public Assembly getPostAssembly() {
		return postAssembly;
	}

	public void setPostAssembly(Assembly postAssembly) {
		this.postAssembly = postAssembly;
	}

	
	
	
	public Assembly getRefAssembly() {
		return refAssembly;
	}

	public void setRefAssembly(Assembly refAssembly) {
		this.refAssembly = refAssembly;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Casedetails_assembly other = (Casedetails_assembly) obj;
		
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
	}

}
