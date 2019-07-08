package com.fda.mdir.db.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String manufacturer;
	private String model;
	private String brandName;
	private String description;
	private String serializedFlag;
	private String type;
	private String lotNo;
	private double length;
	private double width;
	private double height;
	private String catalogNo;
	private String udi;
	private String snowmed;
	private String mriSafety;
	private String latexInfo;
	private String mfrDate;
	private double diameter;
	private String barCode;

	/**
	 * This will be mapped to tray in assembly table
	 */
	@OneToMany(mappedBy = "product")
	private List<Assembly> assembly;

	/**
	 * This will be mapped to screws in assemblyDetails table
	 */
	@OneToMany(mappedBy = "screwId")
	private List<AssemblyDetails> assemblyDetails;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
	private List<ProductDetails> productDetails;

	@OneToMany(mappedBy = "tray_id")
	private List<Product_Screw> tray_id;
	
	@OneToMany(mappedBy = "screw_id")
	private List<Product_Screw> screw_id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSerializedFlag() {
		return serializedFlag;
	}

	public void setSerializedFlag(String serializedFlag) {
		this.serializedFlag = serializedFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getCatalogNo() {
		return catalogNo;
	}

	public void setCatalogNo(String catalogNo) {
		this.catalogNo = catalogNo;
	}

	public String getUdi() {
		return udi;
	}

	public void setUdi(String udi) {
		this.udi = udi;
	}

	public String getSnowmed() {
		return snowmed;
	}

	public void setSnowmed(String snowmed) {
		this.snowmed = snowmed;
	}

	public String getMriSafety() {
		return mriSafety;
	}

	public void setMriSafety(String mriSafety) {
		this.mriSafety = mriSafety;
	}

	public String getLatexInfo() {
		return latexInfo;
	}

	public void setLatexInfo(String latexInfo) {
		this.latexInfo = latexInfo;
	}

	public String getMfrDate() {
		return mfrDate;
	}

	public void setMfrDate(String mfrDate) {
		this.mfrDate = mfrDate;
	}

	public double getDiameter() {
		return diameter;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}

	public List<Assembly> getAssembly() {
		return assembly;
	}

	public void setAssembly(List<Assembly> assembly) {
		this.assembly = assembly;
	}

	public List<ProductDetails> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetails> productDetails) {
		this.productDetails = productDetails;
	}

	public List<AssemblyDetails> getAssemblyDetails() {
		return assemblyDetails;
	}

	public void setAssemblyDetails(List<AssemblyDetails> assemblyDetails) {
		this.assemblyDetails = assemblyDetails;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	public List<Product_Screw> getTray_id() {
		return tray_id;
	}

	public void setTray_id(List<Product_Screw> tray_id) {
		this.tray_id = tray_id;
	}

	public List<Product_Screw> getScrew_id() {
		return screw_id;
	}

	public void setScrew_id(List<Product_Screw> screw_id) {
		this.screw_id = screw_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brandName == null) ? 0 : brandName.hashCode());
		result = prime * result + ((barCode == null) ? 0 : barCode.hashCode());
		result = prime * result + ((catalogNo == null) ? 0 : catalogNo.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		long temp;
		temp = Double.doubleToLongBits(diameter);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(height);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((latexInfo == null) ? 0 : latexInfo.hashCode());
		temp = Double.doubleToLongBits(length);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((lotNo == null) ? 0 : lotNo.hashCode());
		result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
		result = prime * result + ((mfrDate == null) ? 0 : mfrDate.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((mriSafety == null) ? 0 : mriSafety.hashCode());
		result = prime * result + ((serializedFlag == null) ? 0 : serializedFlag.hashCode());
		result = prime * result + ((snowmed == null) ? 0 : snowmed.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((udi == null) ? 0 : udi.hashCode());
		temp = Double.doubleToLongBits(width);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Product other = (Product) obj;
		
		if (barCode == null) {
			if (other.barCode != null)
				return false;
		} else if (!barCode.equals(other.barCode))
			return false;
		if (brandName == null) {
			if (other.brandName != null)
				return false;
		} else if (!brandName.equals(other.brandName))
			return false;
		if (catalogNo == null) {
			if (other.catalogNo != null)
				return false;
		} else if (!catalogNo.equals(other.catalogNo))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (Double.doubleToLongBits(diameter) != Double.doubleToLongBits(other.diameter))
			return false;
		if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (latexInfo == null) {
			if (other.latexInfo != null)
				return false;
		} else if (!latexInfo.equals(other.latexInfo))
			return false;
		if (Double.doubleToLongBits(length) != Double.doubleToLongBits(other.length))
			return false;
		if (lotNo == null) {
			if (other.lotNo != null)
				return false;
		} else if (!lotNo.equals(other.lotNo))
			return false;
		if (manufacturer == null) {
			if (other.manufacturer != null)
				return false;
		} else if (!manufacturer.equals(other.manufacturer))
			return false;
		if (mfrDate == null) {
			if (other.mfrDate != null)
				return false;
		} else if (!mfrDate.equals(other.mfrDate))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (mriSafety == null) {
			if (other.mriSafety != null)
				return false;
		} else if (!mriSafety.equals(other.mriSafety))
			return false;
		if (serializedFlag == null) {
			if (other.serializedFlag != null)
				return false;
		} else if (!serializedFlag.equals(other.serializedFlag))
			return false;
		if (snowmed == null) {
			if (other.snowmed != null)
				return false;
		} else if (!snowmed.equals(other.snowmed))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (udi == null) {
			if (other.udi != null)
				return false;
		} else if (!udi.equals(other.udi))
			return false;
		if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product[ id=" + id + " ]";
	}

}
