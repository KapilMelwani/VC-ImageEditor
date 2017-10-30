package utils;

import java.awt.Color;

public class ColorUtils {
	
	public static final double NTSC_RED = 0.299;
	public static final double NTSC_GREEN = 0.587;
	public static final double NTSC_BLUE = 0.114;
	
	/**
	 * [0] = RED [1] = GREEN [2] = BLUE
	 * 
	 * @param color
	 * @return
	 */
	public static int[] intToRGB(int color) {
		Color aux = new Color(color);
		int[] rgba = new int[3];
		rgba[0] = aux.getRed();
		rgba[1] = aux.getGreen();
		rgba[2] = aux.getBlue();
		return rgba;
	}
	
	public static int rgbToGray(int color) {
		
	}
}
