package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.RGB;

import static utils.ImageUtils.NTSC_RED;
import static utils.ImageUtils.NTSC_GREEN;
import static utils.ImageUtils.NTSC_BLUE;

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
