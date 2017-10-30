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
	
	
	
	public static int[][] rgbValueCount(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] data = new int[3][256];

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 256; j++)
				data[i][j] = 0;

		for (int c = 0; c < width; c++) {
			for (int r = 0; r < height; r++) {
				
				Color color = new Color(image.getRGB(c, r));
				data[0][color.getRed()]++;
				data[1][color.getGreen()]++;
				data[2][color.getBlue()]++;
			}
		}
		return data;
	}
	
	public static int[] getGrayValues(int[][] image) {
		int[] values = new int[256];
		int index = 0;
		for (int i = 0; i < 256; i++) {
			int red = (int) (image[0][i] * NTSC_RED);
			int green = (int) (image[1][i] * NTSC_GREEN);
			int blue = (int) (image[2][i] * NTSC_BLUE);
			values[index++] = red + green + blue;
		}
		return values;
	}
	
	public static int[] cumulativeValueCount(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] data = new int[3][256];

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 256; j++)
				data[i][j] = 0;

		for (int c = 0; c < width; c++) {
			for (int r = 0; r < height; r++) {
				
				Color color = new Color(image.getRGB(c, r));
				data[0][color.getRed()]++;
				data[1][color.getGreen()]++;
				data[2][color.getBlue()]++;
			}
		}
		return data;
	}

}
