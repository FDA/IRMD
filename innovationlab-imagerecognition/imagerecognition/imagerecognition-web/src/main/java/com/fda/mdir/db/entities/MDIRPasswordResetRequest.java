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


@Entity
public class MDIRPasswordResetRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String email;
    private Long idUser;
    private String code;
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setCode(String code) {
        this.code = code;
    }

   

    public String getEmail() {
        return this.email;
    }

    public Long getIdUser() {
        return this.idUser;
    }

    public String getCode() {
        return this.code;
    }

   
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public MDIRPasswordResetRequest(){

    }   
    
    public MDIRPasswordResetRequest(String email,Long idUser,String code){
        this.email = email;
        this.idUser = idUser;
        this.code = code;
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
        if (!(object instanceof MDIRPasswordResetRequest)) {
            return false;
        }
        MDIRPasswordResetRequest other = (MDIRPasswordResetRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.aurotech.db.entities.NarmsResetRequest[ id=" + id + " ]";
    }
    
}
