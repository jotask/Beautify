package uk.ac.aber.beautify.utils;

import java.awt.image.BufferedImage;

/**
 * Class for utilities like convert between two color spaces or clamp values
 *
 * @author Harry Strange and Jose Vives
 */
public abstract class BeautifyUtils {

	/**
	 * The CIE Standard Illuminant D65 values used in the lab conversion
	 */
	private static final double[] D65 = {95.0429, 100.0, 108.890};

	/**
	 * Clamp the int values between 0 and 255
	 * @author Harry Strange
	 * @param values
	 * 		The int values for clamp
	 * @return
	 * 		The int values clamped
     */
	public static int[] clamp(int[] values) {
		for (int i = 0; i < values.length; i++) {
			values[i] = Math.min(Math.max(0, values[i]), 255);
		}
		return values;
	}

	/**
	 * Clamp the double values between 0 and 255
	 * @autho Harry Strange and Jose Vives
	 * @param values
	 * 		The double values for clamp
	 * @return
	 * 		The double values clamped
	 */
	public static double[] clamp(double[] values) {
		for (int i = 0; i < values.length; i++) {
			values[i] = Math.min(Math.max(0, values[i]), 255);
		}
		return values;
	}
	
	/**
	 * The RGBtoHSV function is taken from the book Principles of Digital Image Processing by 
	 * Burger and Burge.
	 * 
	 * @author Wilhelm Burger
	 * @author Mark J Burge
	 * @param rgb
	 * @return
	 */
	public static double[] RGBtoHSV(double[] rgb) {
		double R = rgb[0];
		double G = rgb[1];
		double B = rgb[2];

		double H = 0, S = 0, V = 0;

		double cMax = 255.0f;

		double cHi = Math.max(R, Math.max(G, B));
		double cLo = Math.min(R, Math.min(G, B));
		double cRng = cHi - cLo;

		V = cHi / cMax;

		if (cHi > 0)	S = (float) cRng / cHi;

		if (cRng > 0) {
			double rr = (double) (cHi - R) / cRng;
			double gg = (double) (cHi - G) / cRng;
			double bb = (double) (cHi - B) / cRng;

			double hh;

			if (R == cHi)		hh = bb - gg;
			else if (G == cHi)	hh = rr - bb + 2.0f;
			else 				hh = gg - rr + 4.0f;

			if (hh < 0)			hh = hh + 6;

			H = hh / 6;
		}

		double[] HSV = new double[3];
		HSV[0] = H; HSV[1] = S; HSV[2] = V;

		return HSV;
	}
	
	/**
	 * The HSVtoRGB function is taken from the book Principles of Digital Image Processing by 
	 * Burger and Burge.
	 * 
	 * @author Wilhelm Burger
	 * @author Mark J Burge
	 * @return
	 */
	public static double[] HSVtoRGB(double[] hsv) {
		double h = hsv[0];
		double s = hsv[1];
		double v = hsv[2];

		double rr = 0, gg = 0, bb = 0;

		double hh = (6 * h) % 6;
		
		int c1 = (int) hh;
		double c2 = hh - c1;

		double x = (1 - s) * v;
		double y = (1 - (s * c2)) * v;
		double z = (1 - (s * (1 - c2))) * v;
		
		switch (c1) {
			case 0:	rr = v; gg = z; bb = x; break;
			case 1: rr = y; gg = v; bb = x; break;
			case 2: rr = x; gg = v; bb = z; break;
			case 3: rr = x; gg = y; bb = v; break;
			case 4: rr = z; gg = x; bb = v; break;
			case 5: rr = v; gg = x; bb = y; break;
		}
		
		int N = 256;

		double r = Math.min(Math.round(rr * N), N-1);
		double g = Math.min(Math.round(gg * N), N-1);
		double b = Math.min(Math.round(bb * N), N-1);

		double[] rgb = new double[3];
		
		rgb[0] = r; rgb[1] = g; rgb[2] = b;
		
		return rgb;
	}

	/**
	 * Convert a pixel value to a rgb values
	 * @param pixel
	 * 		The pixel information to conver
	 * @return
	 * 		The array for the rgb values
     */
	public static int[] convertToRGB(int pixel){
		int rgb[] = new int[3];

		rgb[0] = ( (pixel & 0xff0000) >> 16 );
		rgb[1] = ( (pixel & 0x00ff00) >> 8 );
		rgb[2] = ( (pixel & 0x0000ff) );

		return rgb;
	}

	/**
	 * Convert a rgb array of values to a single pixel value
	 * @param rgb
	 * 		The rgb array to convert
	 * @return
	 * 		The pixel value calculated
     */
	public static int convertToPixel(int[] rgb){
		return ((rgb[0] & 0xff) << 16) | ((rgb[1] & 0xff) << 8) | (rgb[2] & 0xff);
	}

	/**
	 * Convert RGB to XYZ color space
	 *
	 * Pseudocode from easyRGB.com
	 *
	 * @param pixels
	 * 		The rgb pixel to conver
	 * @return
	 * 		The xyz converted pixels
     */
	public static double[] RGBtoXYZ(double[] pixels){

		double[] rgb = new double[pixels.length];

		for(int i = 0; i < pixels.length; i++) {
			rgb[i] = pixels[i] / 255.0;

			if(rgb[i] > 0.04045){
				rgb[i] = Math.pow(( ( rgb[i] + 0.055 ) / 1.055 ), 2.4);
			}else{
				rgb[i] = rgb[i] / 19.92;
			}
			rgb[i] *= 100;
		}

		double[] xyz = new double[rgb.length];

		xyz[0] = rgb[0] * 0.4124 + rgb[1] * 0.3576 + rgb[2] * 0.1805;
		xyz[1] = rgb[0] * 0.2126 + rgb[1] * 0.7152 + rgb[2] * 0.0722;
		xyz[2] = rgb[0] * 0.0193 + rgb[1] * 0.1192 + rgb[2] * 0.9505;

		return xyz;

	}

	/**
	 * Convert  from xyz to RGB
	 *
	 * Pseudocode from easyRGB.com
	 *
	 * @param pixels
	 * 		The xyz values to convert
	 * @return
	 * 		The rgb values converted
     */
	public static double[] XYZtoRGB(double[] pixels){

		double[] rgb = new double[pixels.length];
		double[] xyz = new double[pixels.length];

		for(int i = 0; i < pixels.length; i++)
			xyz[i] = pixels[i] / 100.0;

		rgb[0] = (xyz[0] * 3.2406) + (xyz[1] * -1.5372) + (xyz[2] * -0.4986);
		rgb[1] = (xyz[0] * -0.9689) + (xyz[1] * 1.8758) + (xyz[2] * 0.0415);
		rgb[2] = (xyz[0] * 0.0557) + (xyz[1] * -0.2040) + (xyz[2] * 1.0570);

		for(int i = 0; i < rgb.length; i++){
			if(rgb[i] > 0.0031308){
				// var_R = 1.055 * ( var_R ^ ( 1 / 2.4 ) ) - 0.055
				rgb[i] = ((1.055 * Math.pow(rgb[i], 1.0 / 2.4)) - 0.055);
			}else{
				// var_R = 12.92 * var_R
				rgb[i] = rgb[i] * 12.92;
			}
			rgb[i] = (rgb[i] < 0) ? 0: rgb[i];
			rgb[i] *= 255;
		}
		return rgb;
	}

	/**
	 * Get a exact copy from a image
	 * @param original
	 * 		The image to copy
	 * @return
	 * 		The new image copy
     */
	public static BufferedImage getCopy(BufferedImage original){
		BufferedImage output = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		output.setData(original.getData());
		return  output;
	}

	/**
	 * Get an image with the same characteristics but with empty
	 * @param original
	 * 		The image to copy
	 * @return
	 * 		The new image copy
     */
	public static BufferedImage getEmptyCopy(BufferedImage original){
		BufferedImage output = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		return  output;
	}

	/**
	 * Convert XYZ color space to LAB color space using the illuminant D65 values
	 *
	 * Pseudocode from easyRGB.com
	 *
	 * @param pixels
	 * 		The XYZ values to convert
	 * @return
	 * 		The lab values converted
     */
	public static double[] XYZtoLAB(double[] pixels) {

		double[] lab = new double[pixels.length];
		double[] xyz = new double[pixels.length];

		xyz[0] = pixels[0] / D65[0];
		xyz[1] = pixels[1] / D65[1];
		xyz[2] = pixels[2] / D65[2];

		for(int i = 0; i < xyz.length; i++){
			if(xyz[i] > 0.008856){
				xyz[i] = Math.pow(xyz[i], ( 1.0 / 3.0 ));
			}else{
				xyz[i] = ( 7.787 * xyz[i] ) + ( 16.0 / 116.0);
			}
		}

		lab[0] = ( 116.0 * xyz[1] ) - 16.0;
		lab[1] = 500.0 * ( xyz[0] - xyz[1] );
		lab[2] = 200.0 * ( xyz[1] - xyz[2] );

		return lab;
	}

	/**
	 * Convert LAB to XYZ color space
	 *
	 * Pseudocode from easyRGB.com
	 *
	 * @param lab
	 * 		The lab values to convert
	 * @return
	 * 		The XYZ values converted
     */
	public static double[] LABtoXYZ(double[] lab) {

		double[] xyz = new double[lab.length];

		xyz[1] = ( lab[0] + 16.0 ) / 116.0;
		xyz[0] = ( lab[1] / 500.0 ) + xyz[1];
		xyz[2] = xyz[1] - ( lab[2] / 200.0 );

		for(int i = 0; i < xyz.length; i++){
			if(Math.pow( xyz[i], 3.0 ) > 0.008856){
				xyz[i] = Math.pow( xyz[i], 3.0);
			}else{
				xyz[i] = ( xyz[i] - ( 16.0 / 116.0) ) / 7.787;
			}
		}

		xyz[0] = xyz[0] * D65[0];
		xyz[1] = xyz[1] * D65[1];
		xyz[2] = xyz[2] * D65[2];

		return  xyz;

	}

	/**
	 * My owm implementation for convert rgb to hsv, without the the 255 division
	 *
	 * Pseudocode from easyRGB.com
	 *
	 * @param tmp
	 * 		The rgb values to convert
	 * @return
	 * 		The hsv values converted
     */
	public static double[] RGB2HSV(double[] tmp){

		double R = tmp[0] / 255.0;
		double G = tmp[1] / 255.0;
		double B = tmp[2] / 255.0;

		double min = (Math.min(Math.min(R, G), B));
		double max = (Math.max(Math.max(R, G), B));
		double delta = max - min;

		double H = max;
		double S = max;
		double V = max;

		if(delta == 0){
			H = 0;
			S = 0;
		}else{

			S = delta / max;

			double delR = ( ( ( max - R ) / 6.0 ) + ( delta / 2.0 ) ) / delta;
			double delG = ( ( ( max - G ) / 6.0 ) + ( delta / 2.0 ) ) / delta;
			double delB = ( ( ( max - B ) / 6.0 ) + ( delta / 2.0 ) ) / delta;

			if(R == max){
				H = delB - delG;
			}else if(G == max){
				H = (1.0/3.0) + delR - delB;
			}else if(B == max){
				H = (2.0/3.0) + delG - delR;
			}

			if(H < 0) H += 1;
			if(H > 1) H -= 1;
		}

		double[] hsv = new double[3];
		hsv[0] = H;
		hsv[1] = S;
		hsv[2] = V;
		return hsv;
	}

	/**
	 * My owm implementation for convert hsv to rgb, without the the 255 division
	 *
	 * Pseudocode from easyRGB.com
	 *
	 * @param hsv
	 * 		The hsv values to convert
	 * @return
	 * 		The rgb values converted
	 */
	public static double[] HSV2RGB(double[] hsv){

		double[] rgb = new double[3];

		int h = 0;
		int s = 1;
		int v = 2;

		if(hsv[s] == 0){
			for(int i = 0; i < rgb.length; i++){
				rgb[i] = hsv[v] * 255.0;
			}
		}else{
			double H = hsv[h] * 6;
			if(H == 6) H = 0;
			int i = (int) H;
			double one = hsv[v] * (1 - hsv[s]);
			double two = hsv[v] * (1 - hsv[s] * (H - i) );
			double thee = hsv[v] * (1 - hsv[s] *(1-(H - i)));
			if( i == 0){
				rgb[0] = hsv[v];
				rgb[1] = thee;
				rgb[2] = one;
			}else if( i == 1) {
				rgb[0] = two;
				rgb[1] = hsv[v];
				rgb[2] = one;
			}else if( i==2){
				rgb[0] = one;
				rgb[1] = hsv[v];
				rgb[2] = thee;
			}else if(i==3){
				rgb[0] = one;
				rgb[1] = two;
				rgb[2] = hsv[v];
			}else if(i==4) {
				rgb[0] = thee;
				rgb[1] = one;
				rgb[2] = hsv[v];
			}else{
				rgb[0] = hsv[v];
				rgb[1] = one;
				rgb[2] = two;
			}
			for(int j = 0; j < rgb.length; j++){
				rgb[j] *= 255;
			}
		}

		return rgb;
	}

	/**
	 * Quick method for convert between RGB to lab
	 * @param rgb
	 * 		The rgb values
	 * @return
	 * 		The lab values
     */
	public static double[] RGBtoLAB(double[] rgb){
		return BeautifyUtils.XYZtoLAB(BeautifyUtils.RGBtoXYZ(rgb));
	}

	/**
	 * Quick method for conver between LAB to rgb
	 * @param lab
	 * 		The lab values
	 * @return
	 * 		The rgb values
     */
	public static double[] LABtoRGB(double[] lab){
		return BeautifyUtils.XYZtoRGB(BeautifyUtils.LABtoXYZ(lab));
	}

}
