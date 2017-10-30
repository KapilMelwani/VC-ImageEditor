package utils;

import java.awt.image.BufferedImage;

import main.RGB;

public class HistogramUtils {
	
	public static RGB[][] getLUT(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		RGB[][] lut = new RGB[height][width];

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				lut[i][j] = new RGB(image.getRGB(i, j));
		return lut;
	}

}
