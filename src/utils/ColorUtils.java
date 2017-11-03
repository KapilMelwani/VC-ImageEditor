package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

import object.RGB;

public class ColorUtils {
	
	public static final double NTSC_RED = 0.299;
	public static final double NTSC_GREEN = 0.587;
	public static final double NTSC_BLUE = 0.114;
	
	public static boolean isRGB(BufferedImage image) {
		int i = image.getType();
		if(i == BufferedImage.TYPE_3BYTE_BGR)
			return true;
		else if(i == BufferedImage.TYPE_4BYTE_ABGR)
			return true;
		else if(i == BufferedImage.TYPE_4BYTE_ABGR_PRE)
			return true;
		else if(i == BufferedImage.TYPE_INT_ARGB)
			return true;
		else if(i == BufferedImage.TYPE_INT_ARGB_PRE)
			return true;
		else if(i == BufferedImage.TYPE_INT_BGR)
			return true;
		else if(i == BufferedImage.TYPE_INT_RGB)
			return true;
		else if(i == BufferedImage.TYPE_USHORT_555_RGB)
			return true;
		else if(i == BufferedImage.TYPE_USHORT_565_RGB)
			return true;
		return false;
	}
	
	public static boolean isGrayscale(BufferedImage image) {
		int i = image.getType();
		if(i == BufferedImage.TYPE_BYTE_GRAY)
			return true;
		return false;
	}
	
	public static boolean isGrayscale(RGB[][] lut) {
		for(int i = 0; i < lut.length; i++)
			for(int j = 0; j < lut[i].length; j++)
				if(!lut[i][j].isGrayscale())
					return false;
		return true;
	}
	
	public static int[] intToRGB(int color) {
		Color aux = new Color(color);
		int[] rgba = new int[3];
		rgba[0] = aux.getRed();
		rgba[1] = aux.getGreen();
		rgba[2] = aux.getBlue();
		return rgba;
	}

	public static int rgbToInt(int red, int green, int blue) {
		return new Color(red, green, blue).getRGB();
	}
	
}
