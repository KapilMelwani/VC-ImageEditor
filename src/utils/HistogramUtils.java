package utils;

import java.awt.image.BufferedImage;

import object.RGB;

public class HistogramUtils {
	
	public static RGB[][] getLUT(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		RGB[][] lut = new RGB[width][height];

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				lut[j][i] = new RGB(image.getRGB(j, i));
		return lut;
	}
	
	public static BufferedImage specifyHistogram(BufferedImage image, int[] normalized) {
		int height = image.getHeight();
		int width = image.getWidth();
		if(ColorUtils.isGrayscale(image))
			image = ImageUtils.rgbToGrayscaleCopyAuto(image);
		for(int row = 0; row < height; row++)
			for(int col = 0; col < width; col++) {
				int gray = new RGB(image.getRGB(row, col)).gray();
				image.setRGB(row, col, gray);
			}
		return image;	
	}

}
