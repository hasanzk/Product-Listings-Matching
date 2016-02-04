package Items;

import Exceptions.EmptyAttributeException;

public class PriceListing {

	/* Price Listing Attributes */
	public static final String [] attribute_name = {"title", "manufacturer", "currency", "price"};
	private String title;
	private String manufacturer;
	private String currency;
	private String price;
	
	/**
	 * Price Listings Constructor
	 * 
	 * @param attribute					: contains ordered strings representing the price listing attribute values
	 * @throws EmptyAttributeException	: thrown if any required attribute is empty or null
	 */
	public PriceListing(String [] attribute) throws EmptyAttributeException {
		if(	attribute == null || 
			attribute[0].equals("") || attribute[0] == null ||
			attribute[2].equals("") || attribute[2] == null ||
			attribute[3].equals("") || attribute[3] == null) 	throw new Exceptions.EmptyAttributeException();
		title			= attribute[0];
		manufacturer 	= attribute[1];
		currency 		= attribute[2];
		price			= attribute[3];
	}
	
	/**
	 *  This method returns a string of the product information
	 */
	public String toString(){
		return "[\"" + title + "\", \"" + manufacturer + "\", \"" + currency + "\", \"" + price + "\"]";
	}
	
	/**
	 * @return the title of the price listing
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the manufacturer of the price listing
	 */
	public String getManufacturer() {
		return manufacturer;
	}
	/**
	 * @return the currency of the price listing
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @return the price of the price listing
	 */
	public String getPrice() {
		return price;
	}
}
