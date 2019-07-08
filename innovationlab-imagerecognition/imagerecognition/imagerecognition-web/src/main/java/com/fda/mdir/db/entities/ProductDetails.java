package com.fda.mdir.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class ProductDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	@ManyToOne
	@JoinColumn
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private Product product;
	
	
	private String trayGroup;
	private double diameter;
	private String holeNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getTrayGroup() {
		return trayGroup;
	}

	public void setTrayGroup(String trayGroup) {
		this.trayGroup = trayGroup;
	}

	public double getDiameter() {
		return diameter;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}

	public String getHoleNumber() {
		return holeNumber;
	}

	public void setHoleNumber(String holeNumber) {
		this.holeNumber = holeNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(diameter);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((holeNumber == null) ? 0 : holeNumber.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ProductDetails other = (ProductDetails) obj;
		if (Double.doubleToLongBits(diameter) != Double.doubleToLongBits(other.diameter))
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
		
		if (trayGroup == null) {
			if (other.trayGroup != null)
				return false;
		} else if (!trayGroup.equals(other.trayGroup))
			return false;
		return true;
	}

}
