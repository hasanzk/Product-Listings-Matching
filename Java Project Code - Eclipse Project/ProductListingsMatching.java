import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Exceptions.EmptyAttributeException;
import Items.PriceListing;
import Items.Product;
import Items.Result;

/**
 * @author	:	Hasan Al-Khabbaz
 * @Date	: 	03/02/2016
 * @version	:	1.0.0
 */

public class ProductListingsMatching {
	
	/** 
	 * Data objects list variables
	 * These lists store all the product, price listing & result objects
	 */
	private static ArrayList<Product> products = new ArrayList<Product>();
	private static ArrayList<PriceListing> listings = new ArrayList<PriceListing>();
	private static ArrayList<Result> results = new ArrayList<Result>();
	
	/**
	 * Program execution starts from this method
	 */
	public static void main(String[] args) {
		Thread thread = new Thread(){
			public void run() {
				JOptionPane.showMessageDialog(null, new JLabel("<html><center>Program has started."
																+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br><br>"
																+ "Please wait few seconds.."
																+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br><br>"
																+ "This window will close automatically when results is ready"
																+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</center></html>"),
																				null, JOptionPane.NO_OPTION);
			}
		};
		thread.start();
		loadItems(Product.attribute_name, "products");				// loading products into the program product list variable
		System.out.println("Products are loaded.");
		
		loadItems(PriceListing.attribute_name, "listings");			// loading price listings into the program price listing list variable
		System.out.println("Listings are loaded.");
		
		System.out.println("Matching records...");
		matchProductsWithListings();								// matching records
		System.out.println("Saving...");
		saveResults();												// saving results
		System.out.println("Done!");
		
		thread.interrupt();
		JOptionPane.showMessageDialog(null, "Program ended & results are ready", null, JOptionPane.NO_OPTION);
	}
	

	/**
	 * This Method loads both products and price listings from files into the program
	 * 
	 * @param attribute_list	: This parameter contains the names of all attributes that needs to be loaded
	 * @param file_name			: This parameter contains the name of the file to load items from
	 */
	private static void loadItems(String [] attribute_list, String file_name){
		try {
			FileReader source_file = new FileReader("InputFiles/" + file_name + ".txt");	// opening the file source
			
			int temp;																		// stores the value read from the file in the while loop
			char buffer;																	// stores the read value as a character
			String attribute_name = "";														// stores the name of the current attribute to read
			int attribute_index = -1;														// stores the index where current attribute will be stored in array attribute_value
			String [] attribute_value = new String [attribute_list.length];					// stores ordered values of all item attributes
			for (int i = 0; i < attribute_list.length; i++) attribute_value[i] = "";		// initializing attribute_value cells
			
			/* start reading from the file character by character */
			while((temp = source_file.read()) >= 0){
				buffer = (char) temp;														// parsing read value into character format
				
				/* reading the attribute name */
				if(buffer == '"' && attribute_index == -1){
					buffer = (char) source_file.read();
					while(buffer != '"'){													// detecting the end of attribute name
						if(buffer == '-') 	attribute_name += '_'; 							// Java variable names don't allow - character, replace it with _ character
						else 				attribute_name += buffer;						// adding the character to the attribute name
						buffer = (char) source_file.read();
					}
					for (int i = 0; i < attribute_list.length; i++){						// searching for the attribute index in the attribute list
						if(attribute_name.equals(attribute_list[i])){
							attribute_index = i;
							continue;
						}
					}
				}
				
				/* reading the attribute value */
				else if(buffer == '"' && attribute_index != -1){
					buffer = (char) source_file.read();
					while(buffer != '"'){													// detecting the end of attribute value
						if(buffer == '\\')													// detecting the escaping \ character
							attribute_value[attribute_index] += buffer + (char) source_file.read(); // loading escaped " character
						else if(buffer == '_') attribute_value[attribute_index] += ' ';		// replacing _ character by space character
						else attribute_value[attribute_index] += buffer;					// adding the character to the attribute value
						buffer = (char) source_file.read();
					}
					attribute_index	= -1; 													// reseting the attribute index
					attribute_name	= ""; 													// reseting the attribute name
				}
				
				/* creating item and adding it to the list */
				else if(buffer == '}'){ 													// detecting the end of item information
					if(file_name.equals("products"))										// deciding to create a product or a price listing
						products.add(new Product(attribute_value));
					else if(file_name.equals("listings"))
						listings.add(new PriceListing(attribute_value));
					for (int i = 0; i <attribute_list.length; i++) attribute_value[i] = "";	// reseting attributes' values
				}
			}
			
			/* adjusting list size */
			if(file_name.equals("products"))
				products.trimToSize();														// adjust products list size
			else if(file_name.equals("listings"))
				listings.trimToSize();														// adjust listings list size
			
			source_file.close();															// closing items source file
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Can't access " + file_name + " source file", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Can't read from " + file_name + " source file", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		} catch (EmptyAttributeException e) {
			JOptionPane.showMessageDialog(null, "Can't load " + file_name, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * This method checks if there any matches between products and listings, and create correspondent results
	 */
	private static void matchProductsWithListings(){
		try {
			Product product;
			PriceListing listing;
			Result result;
			ArrayList<PriceListing> listingsCopy = (ArrayList<PriceListing>) listings.clone();	// a copy of listings to keep original list as is.
			
			/* matching products with listings */
			for(int i = 0; i < products.size(); i++){
				product = products.get(i);
				result = new Result(product.getName());											// creating a result for each product
				for(int j = 0; j < listingsCopy.size(); j++){
					listing = listingsCopy.get(j);
					if(	contains(product.getManufacturer(), listing.getManufacturer()) &&		// checking if there is any match
						contains(listing.getTitle(), product.getManufacturer()) &&
						contains(listing.getTitle(), product.getModel()) &&
						contains(listing.getTitle(), product.getFamily())){
						
						result.addListing(listing);												// adding the listing to the result listings list
						listingsCopy.remove(j);													// removing the added listing from the copied list of listings
						listingsCopy.trimToSize();												// adjusting copied list size
						j--;																	// adjusting counter
					}
					
				}
				results.add(result);															// adding the result to the list of results
			}
			results.trimToSize();																// adjusting results list size
		} catch (EmptyAttributeException e) {
			JOptionPane.showMessageDialog(null, "Can't create result", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * This method saves all results in an output file (one JSON line per result)
	 */
	private static void saveResults() {
		try {
			FileWriter output_file = new FileWriter("OutputFile/Results.txt");								// creating and/or opening the output file
			
			for(int i = 0; i < results.size(); i++) output_file.write(results.get(i).toString() + "\n");	// writing one result per line
			
			output_file.close();																			// closing output file
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Can't write to the output file", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * 
	 * This method removes special characters from both strings and check if one of them contains the other.
	 * 
	 * @param str1
	 * @param str2
	 * @return true if str1 contains str2 or the opposite
	 */
	private static boolean contains(String str1, String str2){
		
		/* removing all non alphanumeric characters from str1 */
		for(int i = 0; i < str1.length(); i++)
			if(!Character.isAlphabetic((int) str1.charAt(i)) && !Character.isDigit(str1.charAt(i)))
				str1 = str1.substring(0, i) + ' ' + str1.substring(i+1);
		
		/* removing all non alphanumeric characters from str2 */
		for(int i = 0; i < str2.length(); i++)
			if(!Character.isAlphabetic((int) str2.charAt(i)) && !Character.isDigit(str2.charAt(i)))
				str2 = str2.substring(0, i) + ' ' + str2.substring(i+1);
		
		/* adding spaces to both ends of str1 and str2 to avoid missing first and last words */
		str1 = ' ' + str1.toLowerCase() + ' '; 
		str2 = ' ' + str2.toLowerCase() + ' ';
		
		/* check if one of the strings contains the other */
		if(str1.contains(str2) || str2.contains(str1)) return true;
		else return false;
	}
}
