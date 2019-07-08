package com.fda.mdir.db.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fda.mdir.utils.JsonDateSerializer;

@Table
@Entity
public class Assembly {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn
	private MDIRUser user;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date dateUpdated;

	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(name = "assemblyImage", length = 16000000)
	private byte[] assemblyImage;

	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(name = "preImage", length = 16000000)
	private byte[] preImage;

	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(name = "postImage", length = 16000000)
	private byte[] postImage;

	@Lob
	@Column(name = "detectedImage", length = 16000000)
	private byte[] detectedImage;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "assembly")
	private List<AssemblyDetails> assemblyDetails;

	@OneToMany(mappedBy = "preAssembly")
	private List<Casedetails_assembly> preAssembly;

	@OneToMany(mappedBy = "postAssembly")
	private List<Casedetails_assembly> postAssembly;

	@OneToMany(mappedBy = "refAssembly")
	private List<Casedetails_assembly> refAssembly;
	
	private String barCode;
	
	private String trayNumber;

	@ManyToOne
	@JoinColumn
	private Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MDIRUser getUser() {
		return user;
	}

	public void setUser(MDIRUser user) {
		this.user = user;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public byte[] getAssemblyImage() {
		return assemblyImage;
	}

	public void setAssemblyImage(byte[] assemblyImage) {
		this.assemblyImage = assemblyImage;
	}

	public byte[] getPreImage() {
		return preImage;
	}

	public void setPreImage(byte[] preImage) {
		this.preImage = preImage;
	}

	public byte[] getPostImage() {
		return postImage;
	}

	public void setPostImage(byte[] postImage) {
		this.postImage = postImage;
	}

	public byte[] getDetectedImage() {
		return detectedImage;
	}

	public void setDetectedImage(byte[] detectedImage) {
		this.detectedImage = detectedImage;
	}

	public List<AssemblyDetails> getAssemblyDetails() {
		return assemblyDetails;
	}

	public void setAssemblyDetails(List<AssemblyDetails> assemblyDetails) {
		this.assemblyDetails = assemblyDetails;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getTrayNumber() {
		return trayNumber;
	}

	public void setTrayNumber(String trayNumber) {
		this.trayNumber = trayNumber;
	}

	public List<Casedetails_assembly> getPreAssembly() {
		return preAssembly;
	}

	public void setPreAssembly(List<Casedetails_assembly> preAssembly) {
		this.preAssembly = preAssembly;
	}

	public List<Casedetails_assembly> getPostAssembly() {
		return postAssembly;
	}

	public void setPostAssembly(List<Casedetails_assembly> postAssembly) {
		this.postAssembly = postAssembly;
	}
	
	public List<Casedetails_assembly> getRefAssembly() {
		return refAssembly;
	}

	public void setRefAssembly(List<Casedetails_assembly> refAssembly) {
		this.refAssembly = refAssembly;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(assemblyImage);
		result = prime * result + ((barCode == null) ? 0 : barCode.hashCode());
		result = prime * result + ((trayNumber == null) ? 0 : trayNumber.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateUpdated == null) ? 0 : dateUpdated.hashCode());
		result = prime * result + Arrays.hashCode(detectedImage);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Arrays.hashCode(postImage);
		result = prime * result + Arrays.hashCode(preImage);
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
		Assembly other = (Assembly) obj;
		
		if (!Arrays.equals(assemblyImage, other.assemblyImage))
			return false;
		if (barCode == null) {
			if (other.barCode != null)
				return false;
		} else if (!barCode.equals(other.barCode))
			return false;
		if (trayNumber == null) {
			if (other.trayNumber != null)
				return false;
		} else if (!barCode.equals(other.barCode))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateUpdated == null) {
			if (other.dateUpdated != null)
				return false;
		} else if (!dateUpdated.equals(other.dateUpdated))
			return false;
		if (!Arrays.equals(detectedImage, other.detectedImage))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (!Arrays.equals(postImage, other.postImage))
			return false;
		if (!Arrays.equals(preImage, other.preImage))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Assembly[ id=" + id + " ]";
	}

}
