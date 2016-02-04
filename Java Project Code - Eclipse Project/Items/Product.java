package Items;

import Exceptions.EmptyAttributeException;

public class Product {
	
	/* Product Attributes */
	public static final String [] attribute_name = {"product_name", "manufacturer", "model", "family", "announced_date"};
	private String product_name;
	private String manufacturer;
	private String model;
	private String family;
	private String announced_date;
	
	/**
	 * Products Constructor
	 * 
	 * @param attribute					: contains ordered strings representing the product attribute values
	 * @throws EmptyAttributeException	: thrown if any required attribute is empty or null
	 */
	public Product(String [] attribute) throws EmptyAttributeException{
		if(	attribute == null || 
			attribute[0].equals("") || attribute[0] == null ||
			attribute[1].equals("") || attribute[1] == null ||
			attribute[2].equals("") || attribute[2] == null ||
			attribute[4].equals("") || attribute[4] == null) 	throw new Exceptions.EmptyAttributeException();
		product_name	= attribute[0];
		manufacturer	= attribute[1];
		model			= attribute[2];
		family			= attribute[3];
		announced_date	= attribute[4];
	}
	
	/**
	 * This method returns a string of the product information
	 */
	public String toString(){
		return "[\"" + product_name + "\", \"" + manufacturer + "\", \"" + model + "\", \"" + family + "\", \"" + announced_date + "\"]";
	}
	
	/**
	 * @return the name of the product
	 */
	public String getName() {
		return product_name;
	}
	/**
	 * @return the manufacturer of the product
	 */
	public String getManufacturer() {
		return manufacturer;
	}
	/**
	 * @return the family of the product
	 */
	public String getFamily() {
		return family;
	}
	/**
	 * @return the model of the product
	 */
	public String getModel() {
		return model;
	}
	/**
	 * @return the announced_date of the product
	 */
	public String getAnnounced_date() {
		return announced_date;
	}
}
