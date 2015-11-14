package uk.ac.aber.beautify.utils;

public abstract class BeautifyUtils {

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
	public static float[] RGBtoHSV(int[] rgb) {
		int R = rgb[0];
		int G = rgb[1];
		int B = rgb[2];
		
		float H = 0, S = 0, V = 0;
		
		float cMax = 255.0f;
		
		int cHi = Math.max(R, Math.max(G, B));
		int cLo = Math.min(R, Math.min(G, B));
		int cRng = cHi - cLo;
		
		V = cHi / cMax;
		
		if (cHi > 0)	S = (float) cRng / cHi;
		
		if (cRng > 0) {
			float rr = (float) (cHi - R) / cRng;
			float gg = (float) (cHi - G) / cRng;
			float bb = (float) (cHi - B) / cRng;
			
			float hh;
			
			if (R == cHi)		hh = bb - gg;
			else if (G == cHi)	hh = rr - bb + 2.0f;
			else 				hh = gg - rr + 4.0f;
			
			if (hh < 0)			hh = hh + 6;
			
			H = hh / 6;
		}
		
		float[] HSV = new float[3];
		HSV[0] = H; HSV[1] = S; HSV[2] = V;
		
		return HSV;
	}
	
	/**
	 * The HSVtoRGB function is taken from the book Principles of Digital Image Processing by 
	 * Burger and Burge.
	 * 
	 * @author Wilhelm Burger
	 * @author Mark J Burge
	 * @param rgb
	 * @return
	 */
	public static int[] HSVtoRGB(float[] hsv) {
		float h = hsv[0];
		float s = hsv[1];
		float v = hsv[2];
		
		float rr = 0, gg = 0, bb = 0;
		
		float hh = (6 * h) % 6;
		
		int c1 = (int) hh;
		float c2 = hh - c1;
		
		float x = (1 - s) * v;
		float y = (1 - (s * c2)) * v;
		float z = (1 - (s * (1 - c2))) * v;
		
		switch (c1) {
			case 0:	rr = v; gg = z; bb = x; break;
			case 1: rr = y; gg = v; bb = x; break;
			case 2: rr = x; gg = v; bb = z; break;
			case 3: rr = x; gg = y; bb = v; break;
			case 4: rr = z; gg = x; bb = v; break;
			case 5: rr = v; gg = x; bb = y; break;
		}
		
		int N = 256;
		
		int r = Math.min(Math.round(rr * N), N-1);
		int g = Math.min(Math.round(gg * N), N-1);
		int b = Math.min(Math.round(bb * N), N-1);
		
		int[] rgb = new int[3];
		
		rgb[0] = r; rgb[1] = g; rgb[2] = b;
		
		return rgb;
	}
}
