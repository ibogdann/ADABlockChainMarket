package block.chain.market.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//import block.chain.market.products.Product;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {
	private @Id @GeneratedValue Long id;
	
	@ElementCollection
	private Set<Long> productsId;
	
	@ElementCollection
	private List<Integer> productsQuantity;
	
	private float totalValue;
	private Status status;
	
	Order() {}
	
	public Order(Set<Long> productsId, List<Integer> productsQuantity, float totalValue, Status status) {
		super();
		this.productsId = productsId;
		this.productsQuantity = productsQuantity;
		this.totalValue = totalValue;
		this.status = status;
	}
	
	public void deleteProducts(List<Long> productIds) {
		for(Long id: productIds) {
			productsQuantity.remove(new ArrayList<Long>(productsId).indexOf(id));
			productsId.remove(id);
		}
	}
	
	public void deleteProduct(Long id) {
		productsQuantity.remove(new ArrayList<Long>(productsId).indexOf(id));
		productsId.remove(id);
	}
	
	public boolean isEmpty() {
		return productsId.isEmpty();
	}
}
