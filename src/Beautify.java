/**
 * The Beautify class is the main entry point into the Beautify system.  You can
 * leave BeautifyGUI and BeautifyKernel alone (feel free to have a peak at them 
 * but please don't fiddle!).  
 * 
 * You will need to make sure that you over-ride the line that sets the filterSet
 * within the BeautifyKernel.  At present it uses the DefaultBeautifyFilters()
 * class but you should replace this with your own implementation of the 
 * BeautifyFilters interface.
 * 
 */

import uk.ac.aber.beautify.core.BeautifyKernel;
import uk.ac.aber.beautify.custom.JoseFilter;
import uk.ac.aber.beautify.gui.BeautifyGUI;

public class Beautify {

	public static void main(String[] args) {
		
		BeautifyGUI gui = new BeautifyGUI();
		BeautifyKernel beautify = new BeautifyKernel(gui);

		// TODO delete this line
//		beautify.loadImage("C:\\Users\\Jose\\Documents\\image_01.jpg");
		
		
		// TODO: Over-ride this part with your own filter set
		beautify.setFilterSet(new JoseFilter());
		
	}	
}
