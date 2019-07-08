package com.fda.mdir.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "product_screw")
@Entity
public class Product_Screw {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "tray_id")
	private Product tray_id;
	
	@ManyToOne
	@JoinColumn(name = "screw_id")
	private Product screw_id;

	public Product getTray_id() {
		return tray_id;
	}

	public void setTray_id(Product tray_id) {
		this.tray_id = tray_id;
	}

	public Product getScrew_id() {
		return screw_id;
	}

	public void setScrew_id(Product screw_id) {
		this.screw_id = screw_id;
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
		Product_Screw other = (Product_Screw) obj;
		
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
	}

}
