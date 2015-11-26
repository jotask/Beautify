package uk.ac.aber.beautify.utils;

import java.awt.image.BufferedImage;

public abstract class BeautifyUtils {

	private static final double[] D65 = {95.0429, 100.0, 108.890};

	public static int[] clamp(int[] values) {
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

	public static int[] convertToRGB(int pixel){
		int rgb[] = new int[3];

		rgb[0] = ( (pixel & 0xff0000) >> 16 );
		rgb[1] = ( (pixel & 0x00ff00) >> 8 );
		rgb[2] = ( (pixel & 0x0000ff) );

		return rgb;
	}

	public static int convertToPixel(int[] rgb){
		return ((rgb[0] & 0xff) << 16) | ((rgb[1] & 0xff) << 8) | (rgb[2] & 0xff);
	}

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

	public static BufferedImage getCopy(BufferedImage original){
		BufferedImage output = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
//		output.setData(original.getData());
		return  output;
	}

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

	public static double[] RGBtoLAB(double[] rgb){
		return BeautifyUtils.XYZtoLAB(BeautifyUtils.RGBtoXYZ(rgb));
	}

	public static double[] LABtoRGB(double[] lab){
		return BeautifyUtils.XYZtoRGB(BeautifyUtils.LABtoXYZ(lab));
	}

}
