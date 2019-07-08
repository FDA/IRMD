package com.fda.mdir.db.dao.ejb;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fda.mdir.db.entities.Product;
import com.fda.mdir.db.entities.ProductDetails;
import com.fda.mdir.db.entities.Product_Screw;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDIRProductDAO {
	private static final Logger logger = LoggerFactory.getLogger(MDIRProductDAO.class);
	
	@PersistenceContext
    private EntityManager em;
    
    public <T> void persist(T t) {

        em.persist(t);

    }
    
    public Product getProductByID(String productID) {

        TypedQuery <Product> query = em.createQuery("SELECT n FROM Product n WHERE n.id like :productID", Product.class);

        List<Product> rows = query.setParameter("productID", productID).getResultList();
        
        Product productInstance = null;
        
        if(rows != null && rows.size() > 0) {
        	productInstance = rows.get(0);
        }

        return productInstance;
    
    }
    
//    SELECT * fROM product_screw n
//    where n.screw_id IN (SELECT n1.id FROM Product n1 where n1.barCode like  'prod4' ) AND 
//    n.tray_id IN (SELECT n2.id FROM Product n2 where n2.type like  'tray 1' )
    
    public String searchProductByBarCode(String productBarCode, String productType) {

    	List<Product> rows = null;
		Product productInstance = null;
		productType = productType.toLowerCase().trim();
    	productBarCode = productBarCode.toLowerCase().trim();
    	
			List<Product_Screw> product_screw_rows = null;
			Product_Screw productScrewInstance = null;
			TypedQuery<Product_Screw> prod_screw_query = em
					.createQuery(
							"SELECT n FROM Product_Screw n "
									+ "where n.screw_id IN (SELECT n1.id FROM Product n1 where lower(n1.barCode) like :productBarCode ) AND "
									+ "n.tray_id IN (SELECT n2.id FROM Product n2 where lower(n2.type) like :productType ) ",
							Product_Screw.class);

			prod_screw_query.setParameter("productBarCode", productBarCode);
			prod_screw_query.setParameter("productType", productType);

			product_screw_rows = prod_screw_query.getResultList();

			if (product_screw_rows != null && product_screw_rows.size() > 0) {

				productScrewInstance = product_screw_rows.get(0);

				Long productId = productScrewInstance.getScrew_id().getId();
				
				logger.debug("searchProductByBarCode Get product id for screw id " + productId);
				
				TypedQuery<Product> prod_query = em.createQuery("SELECT n FROM Product n "
						+ "where n.id like :productId", Product.class);
				prod_query.setParameter("productId",productId);
						
				rows = prod_query.getResultList();
				if (rows != null && rows.size() > 0) {
					productInstance = rows.get(0);
					return "{\"message\":\"Record Found\", \"product_description\": \""
					+ productInstance.getDescription() + " \", \"screw_id\": \""  + productInstance.getId()  + "\" }";
				} else {
					return "{\"message\":\"This implant does not match with barcode\"}";
				}
			} else {
				return "{\"message\":\"This product cannot be added\"}";
			}
//		}
    }
        

 
    @PreDestroy
    private void beforeGoing() {
        logger.debug("MDIRProductDAO going good bye");
    } 
}
