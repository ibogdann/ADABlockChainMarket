package block.chain.market.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import block.chain.market.communication.SQSUtil;
import block.chain.market.communication.OrderMessageCompiler;
import block.chain.market.products.InsufficientStockException;
import block.chain.market.products.Product;
import block.chain.market.products.ProductModelAssembler;
import block.chain.market.products.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
//	@Autowired
//	private SQSUtil<List<Product>> sqsUtilProducts;
//	
//	@Autowired
//	private SQSUtil<List<Integer>> sqsUtilQuantities;
	
	@Autowired
	private SQSUtil<String> sqsUtil;
	
	private final OrderModelAssembler orderAssembler;
	private final ProductModelAssembler productAssmebler;
	
	@Autowired
	private OrderMessageCompiler messageCompiler;

	OrderController(OrderModelAssembler orderAssembler, ProductModelAssembler productAssembler) {
		
//		this.orderRepository = orderRepository;
		this.orderAssembler = orderAssembler;
		this.productAssmebler = productAssembler;
		
	}

	@GetMapping
	CollectionModel<EntityModel<Order>> all() {
		
		List<EntityModel<Order>> orders = orderRepository.findAll().stream()
				.map(orderAssembler::toModel)
				.collect(Collectors.toList());

		return new CollectionModel<>(orders,
				linkTo(methodOn(OrderController.class).all()).withSelfRel());
	  }
	
	@GetMapping("/{id}")
	EntityModel<Order> one(@PathVariable Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));
		
		return orderAssembler.toModel(order);
	}
	
	@GetMapping("/{id}/products")
	CollectionModel<EntityModel<Product>> products(@PathVariable Long id){
		
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));
		
		List<EntityModel<Product>> products = productRepository.findAllById(order.getProductsId())
				.stream()
				.map(productAssmebler::toModel)
				.collect(Collectors.toList());
		
//		for(Long productId: order.getProductsId()) {
//			Product product = productRepository.findById(productId)
//					.orElseThrow(() -> new ProductNotFoundException(productId));
//		}
		
		return new CollectionModel<>(products);
		
	}

	@PostMapping
	ResponseEntity<?> newOrder(@RequestBody Order order) throws JsonProcessingException {

		order.setStatus(Status.IN_PROGRESS);
		
		List<Float> priceList = productRepository.findAllById(order.getProductsId())
				.stream()
				.map(Product::getPrice)
				.collect(Collectors.toList());
		
		List<Integer> stockList = productRepository.findAllById(order.getProductsId())
				.stream()
				.map(Product::getStock)
				.collect(Collectors.toList());
		
		List<Product> productList = productRepository.findAllById(order.getProductsId())
				.stream()
				.collect(Collectors.toList());
		
		List<Product> foreignProducts = new ArrayList<Product>();
		List<Integer> quantities = new ArrayList<Integer>();
		List<Long> ids = new ArrayList<Long>();
		
//		int index = 0;
		
		try {
			float totalValue = 0;
			
			for(int i = 0; i < priceList.size(); i++) {
				
				int requestedQuantity = order.getProductsQuantity().get(i);
				int availableQuantity = stockList.get(i);
				
				if(requestedQuantity <= availableQuantity) {
					totalValue += priceList.get(i) * requestedQuantity;
				}else {
//					index = i;
//					throw new InsufficientStockException(requestedQuantity, availableQuantity);
					ids.add(productList.get(i).getId());
					foreignProducts.add(productList.get(i));
					quantities.add(requestedQuantity);
				}
			}
			
			order.setTotalValue(totalValue);
			
		}catch(InsufficientStockException e) {
			
//			List<Product> productList = productRepository.findAllById(order.getProductsId())
//					.stream()
//					.collect(Collectors.toList());
			
//			return ResponseEntity
//					.status(HttpStatus.CONFLICT)
//					.body(new VndErrors.VndError("Stock not enough", "You can't order the requested amount for product " + productList.get(index).getName()));
			
		}catch (Exception e) {
			// TODO: handle exception
			
		}
		
		if(!foreignProducts.isEmpty() && !quantities.isEmpty()) {
			String message = messageCompiler.compile(foreignProducts, quantities);
			sqsUtil.sendSQSMessage(message);
			order.deleteProducts(ids);
			log.info("Remaining order: " + order);
//			log.info("Sent product request: " + foreignProducts);
//			log.info("Sent quantity request: " + quantities);
//			sqsUtilProducts.sendSQSMessage(foreignProducts);
//			sqsUtilQuantities.sendSQSMessage(quantities);
		}
		
		if(!order.isEmpty()) {
			Order newOrder = orderRepository.save(order);
			return ResponseEntity
					.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
					.body(orderAssembler.toModel(newOrder));
		}
		
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(new VndErrors.VndError("Stock not enough", "Order might have been sent to other server."));
	}
	  
	@DeleteMapping("/{id}/cancel")
	ResponseEntity<RepresentationModel<?>> cancel(@PathVariable Long id) {

		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Status.IN_PROGRESS) {
			order.setStatus(Status.CANCELLED);
			return ResponseEntity.ok(orderAssembler.toModel(orderRepository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
	}
	
	@PutMapping("/{id}/complete")
	ResponseEntity<RepresentationModel<?>> complete(@PathVariable Long id) {

		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		
		List<Integer> stockList = productRepository.findAllById(order.getProductsId())
				.stream()
				.map(Product::getStock)
				.collect(Collectors.toList());
		
		List<Product> productList = productRepository.findAllById(order.getProductsId())
				.stream()
				.collect(Collectors.toList());
		
		if (order.getStatus() == Status.IN_PROGRESS) {
			
			for(int i = 0; i < stockList.size(); i++) {
				productList.get(i).setStock(
						stockList.get(i) - order.getProductsQuantity().get(i)
						);
			}
			
			order.setStatus(Status.COMPLETED);
			productRepository.saveAll(productList);
			return ResponseEntity.ok(orderAssembler.toModel(orderRepository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
}
}
