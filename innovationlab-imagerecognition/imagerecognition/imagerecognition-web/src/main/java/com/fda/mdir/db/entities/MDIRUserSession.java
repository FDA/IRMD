/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fda.mdir.db.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Muazzam
 */
@Entity
@NamedQueries({
     @NamedQuery(name = "findUserIDByToken",query = "SELECT S.clientID FROM MDIRUserSession S WHERE S.uniqueID=:tokenid")
})
@XmlRootElement
public class MDIRUserSession implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    
    
    private Long clientID;
    private String uniqueID;

    @ManyToOne
    @JoinColumn(name = "lab_user_id")
    @JsonIgnore    
    MDIRUser labUser;
    
    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Long getClientID() {
        return clientID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public MDIRUserSession(){
    }
    
    public MDIRUserSession(Long clientID , String uniqueID){
        this.clientID = clientID;
        this.uniqueID = uniqueID;
    }
    
    public MDIRUser getLabUser() {
		return labUser;
	}

	public void setLabUser(MDIRUser labUser) {
		this.labUser = labUser;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MDIRUserSession)) {
            return false;
        }
        MDIRUserSession other = (MDIRUserSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fda.db.entities.Sessions[ id=" + id + " ]";
    }
    
}
