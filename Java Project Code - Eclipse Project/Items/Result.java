package Items;

import java.util.ArrayList;

import Exceptions.EmptyAttributeException;

public class Result {
	
	/* Result Attributes */
	private String product_name;
	private ArrayList<PriceListing> listings = new ArrayList<PriceListing>();
	
	/**
	 * Result Constructor
	 * 
	 * @param product_name				: the name of the product to which price listings is associated
	 * @throws EmptyAttributeException	: thrown if product name is empty or null
	 */
	public Result(String product_name) throws EmptyAttributeException{
		if(product_name.equals("") || product_name == null) throw new Exceptions.EmptyAttributeException();
		this.product_name = product_name;
	}
	
	/**
	 * Adds a price listing to the product price listings ArrayList
	 * 
	 * @param listing : contains a price listing object
	 */
	public void addListing(PriceListing listing) {
		listings.add(listing);
	}
	
	/**
	 * This method return the result in JSON Line format
	 */
	public String toString(){
		listings.trimToSize();
		String output = "{\"listings\": [";
		for(int i = 0; i < listings.size() - 1; i++) output += listings.get(i).toString() + ", ";
		if(!listings.isEmpty()) output += listings.get(listings.size() - 1).toString();
		output += "], \"product_name\": \"" + product_name + "\"}";
		return output;
	}
	
	/**
	 * @return the product name of the result
	 */
	public String getProductName() {
		return product_name;
	}
	/**
	 * @return a copy of the listings associated with the product
	 */
	public ArrayList<PriceListing> getListings() {
		return (ArrayList<PriceListing>) listings.clone();
	}
}