/**
 * The Filter class provides the abstract superclass that all filters used within the Beautify
 * system should extend from.  The filters are the meat of the program and the meat of the Filter
 * class is the filter() method.  The filter() method takes an ImageProcessor as input and returns
 * a filtered version.
 * 
 * You will want to inherit from this class for each filter that you implement.  Then, once you 
 * have implemented your filters make sure that they are linked into your implementation of the
 * BeautifyFilters interface so that they are accessible from the GUI.
 * 
 * @author Harry Strange
 */
package uk.ac.aber.beautify.filters;

import java.awt.image.BufferedImage;

public abstract class Filter {
	
	// The name of the filter.  Why not call it something catchy like, "Really Cool Filter"?
	private String name;
	
	
	/**
	 * This is the heart of the filtering system.  The filter method takes in an ImageProcessor
	 * object (the image to be filtered), processes that image, and then returns the filtered
	 * version.
	 * 
	 * @param 	ImageProcessor - the image to be filtered
	 * @return  ImageProcessor - the processed version of the image
	 */
	public BufferedImage filter(BufferedImage ip) {
		return ip;
	}

	
	/**
	 * getName() is generally called by the GUI to display the name of this filter in the tooltip 
	 * text and also below the filtered image.
	 * 
	 * @return	String - the name of the filter
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Do we really need a Javadoc to explain what this method does?
	 * 
	 * @param String - the name of the filter
	 */
	public void setName(String name) {
		this.name = name;
	}

}
