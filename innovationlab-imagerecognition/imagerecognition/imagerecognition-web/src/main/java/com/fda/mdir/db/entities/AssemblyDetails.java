package com.fda.mdir.db.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fda.mdir.utils.JsonDateSerializer;

@Entity
@Table
public class AssemblyDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn
	@JsonIgnore
	@com.fasterxml.jackson.annotation.JsonIgnore
	private Assembly assembly;

	private String holeNumber;

	@ManyToOne
	@JoinColumn(name = "SCREW_ID")
	//@JsonIgnore
	//@com.fasterxml.jackson.annotation.JsonIgnore
	private Product screwId;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date dateUsed;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date dateRefilled;

	private String trayGroup;

	private String screwStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	@com.fasterxml.jackson.annotation.JsonIgnore
	public Assembly getAssembly() {
		return assembly;
	}

	public void setAssembly(Assembly assembly) {
		this.assembly = assembly;
	}

	public String getHoleNumber() {
		return holeNumber;
	}

	public void setHoleNumber(String holeNumber) {
		this.holeNumber = holeNumber;
	}

	//@JsonIgnore
	//@com.fasterxml.jackson.annotation.JsonIgnore
	public Product getScrewId() {
		return screwId;
	}

	public void setScrewId(Product screwId) {
		this.screwId = screwId;
	}

	public Date getDateUsed() {
		return dateUsed;
	}

	public void setDateUsed(Date dateUsed) {
		this.dateUsed = dateUsed;
	}

	public Date getDateRefilled() {
		return dateRefilled;
	}

	public void setDateRefilled(Date dateRefilled) {
		this.dateRefilled = dateRefilled;
	}

	public String getTrayGroup() {
		return trayGroup;
	}

	public void setTrayGroup(String trayGroup) {
		this.trayGroup = trayGroup;
	}

	public String getScrewStatus() {
		return screwStatus;
	}

	public void setScrewStatus(String screwStatus) {
		this.screwStatus = screwStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateRefilled == null) ? 0 : dateRefilled.hashCode());
		result = prime * result + ((dateUsed == null) ? 0 : dateUsed.hashCode());
		result = prime * result + ((holeNumber == null) ? 0 : holeNumber.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((screwStatus == null) ? 0 : screwStatus.hashCode());
		result = prime * result + ((trayGroup == null) ? 0 : trayGroup.hashCode());
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
		AssemblyDetails other = (AssemblyDetails) obj;
		if (dateRefilled == null) {
			if (other.dateRefilled != null)
				return false;
		} else if (!dateRefilled.equals(other.dateRefilled))
			return false;
		if (dateUsed == null) {
			if (other.dateUsed != null)
				return false;
		} else if (!dateUsed.equals(other.dateUsed))
			return false;
		if (holeNumber == null) {
			if (other.holeNumber != null)
				return false;
		} else if (!holeNumber.equals(other.holeNumber))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (screwStatus == null) {
			if (other.screwStatus != null)
				return false;
		} else if (!screwStatus.equals(other.screwStatus))
			return false;
		if (trayGroup == null) {
			if (other.trayGroup != null)
				return false;
		} else if (!trayGroup.equals(other.trayGroup))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AssemblyDetails[ id=" + id + " ]";
	}
}
