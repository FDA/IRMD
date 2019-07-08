/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.db.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Muazzam
 */

@Entity
@NamedQueries({
		@NamedQuery(name = "findUserBySessionToken", query = "SELECT U FROM MDIRUser U INNER JOIN U.MDIRUserSessions S WHERE S.uniqueID=:tokenid") })
public class MDIRUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String email;
	private String password;
	private String firstName;
	private String lastName;

	
	@OneToMany
	private Set<MDIRRole> mdirRole;
	
	@Column(nullable = true)
	private String fdacenter;

	@OneToMany(mappedBy = "labUser", fetch = FetchType.LAZY)
	private List<MDIRUserSession> MDIRUserSessions;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
	private List<Assembly> assemblies;


	public Set<MDIRRole> getMdirRole() {
		return mdirRole;
	}

	public void setMdirRole(Set<MDIRRole> mdirRole) {
		this.mdirRole = mdirRole;
	}

	public List<Assembly> getAssemblies() {
		return assemblies;
	}

	public void setAssemblies(List<Assembly> assemblies) {
		this.assemblies = assemblies;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	// @JsonIgnore
	// @OneToMany( mappedBy = "labUser", fetch = FetchType.LAZY)
	// private List<LabEventRegistration> labEventRegistration;

	public List<MDIRUserSession> getMDIRUserSessions() {
		return MDIRUserSessions;
	}

	public void setMDIRUserSessions(List<MDIRUserSession> MDIRUserSessions) {
		this.MDIRUserSessions = MDIRUserSessions;
	}

	public String getFdacenter() {
		return fdacenter;
	}

	public void setFdacenter(String fdacenter) {
		this.fdacenter = fdacenter;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MDIRUser() {
	}

	public MDIRUser(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fdacenter == null) ? 0 : fdacenter.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		MDIRUser other = (MDIRUser) obj;
		
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fdacenter == null) {
			if (other.fdacenter != null)
				return false;
		} else if (!fdacenter.equals(other.fdacenter))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "com.fda.db.entities.NarmsUser[ id=" + id + " ]";
	}

}
