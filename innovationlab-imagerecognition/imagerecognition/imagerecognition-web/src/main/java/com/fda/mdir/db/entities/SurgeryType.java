package com.fda.mdir.db.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table
@Entity
public class SurgeryType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String surgeryType;
	
	@OneToMany(mappedBy="surgeryType")
	private List<CaseDetails> cases;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getSurgeryType() {
		return surgeryType;
	}

	public void setSurgeryType(String surgeryType) {
		this.surgeryType = surgeryType;
	}
	
	public List<CaseDetails> getCases() {
		return cases;
	}

	public void setCases(List<CaseDetails> cases) {
		this.cases = cases;
	}

	

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((surgeryType == null) ? 0 : surgeryType.hashCode());
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
		SurgeryType other = (SurgeryType) obj;
		
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (surgeryType == null) {
			if (other.surgeryType != null)
				return false;
		} else if (!surgeryType.equals(other.surgeryType))
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "SurgeryType[ id=" + id + " ]";
    }
	
}
