package block.chain.market.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import block.chain.market.products.Product;
import block.chain.market.products.ProductRepository;
//import block.chain.market.web.ApplicationContextHolder;

@Component
public class OrderMessageCompiler {

	@Autowired
	private ProductRepository productRepository;
	private int i;
	
	public OrderMessageCompiler()
	{
//		productRepository = ApplicationContextHolder.getContext().getBean(ProductRepository.class);
	}
	
	public String compile(List<Product> products, List<Integer> quantities)
	{
		String message = "";
		for(i=0; i<products.size(); i++)
		{
			message +=products.get(i).getName()+"/"+quantities.get(i)+"|";
		}
		return message;
	}
	
	public Map<Product, Integer> decompile(String message)
	{
		Map<Product, Integer> resultMap = new HashMap<>();
		List<String> productsNames = new ArrayList<>();
		List<Integer> quantities = new ArrayList<>();
		String r="";
		while(message.contains("|"))
		{
			i=message.indexOf("|");
			r=message.substring(0,i);
			message=message.substring(i+1,message.length());
			i=r.indexOf("/");
			productsNames.add(r.substring(0,i));
			quantities.add(Integer.valueOf(r.substring(i+1,r.length())));
		}
		List<Product> products = productRepository.findAll();
		for(Product p:products)
		{
			if(productsNames.contains(p.getName()))
			{
				resultMap.put(p,quantities.get(productsNames.indexOf(p.getName())));
			}
		}
		return resultMap;
	}
}
