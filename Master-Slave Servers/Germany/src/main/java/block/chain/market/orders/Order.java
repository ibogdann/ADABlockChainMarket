package block.chain.market.orders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import block.chain.market.products.Product;
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
	
	public Order() {
		super();
		this.productsId = new HashSet<Long>();
		this.productsQuantity = new ArrayList<Integer>();
		this.totalValue = 0;
		this.status = Status.IN_PROGRESS;
	}
	
	public Order(Set<Long> productsId, List<Integer> productsQuantity, float totalValue, Status status) {
		super();
		this.productsId = productsId;
		this.productsQuantity = productsQuantity;
		this.totalValue = totalValue;
		this.status = status;
	}
	
	public void addProduct(Product product, int quantity) {
		this.productsId.add(product.getId());
		this.productsQuantity.add(quantity);
		this.totalValue += quantity * product.getPrice();
	}
}
