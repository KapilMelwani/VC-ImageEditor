package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static BufferedImage readImage(String file) {
		BufferedImage aux = null;
		try {
			aux = ImageIO.read(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return aux;
	}

	public static Dimension scaleImage(Dimension image) {
		double scale = 1.0;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension bounds = new Dimension((int) (screen.getWidth() * 0.75), (int) (screen.getHeight() * 0.75));
		System.out.println("Bounds = " + bounds);
		System.out.println("ImageIn = " + image);
		while (image.getWidth() > bounds.getWidth() || image.getHeight() > bounds.getHeight()) {
			scale -= 0.05;
			int width = (int) (image.getWidth() * scale);
			int height = (int) (image.getHeight() * scale);
			image = new Dimension(width, height);
		}
		System.out.println("ImageOut = " + image);
		return image;
	}

	public static String getNameFromPath(String path) {
		File file = new File(path);
		return file.getName();
	}

	public static String getExtFromName(String filename) {
	    try {
	        return filename.substring(filename.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}

	public static void rgbToGrayscale(BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				Color color = new Color(image.getRGB(i, j));
				int red = (int) (color.getRed() * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue = (int) (color.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
				image.setRGB(i, j, newColor.getRGB());
			}
	}

	public static BufferedImage rgbToGrayscaleCopy(BufferedImage original) {
		BufferedImage image = new BufferedImage(0, 0, 0, null);
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				Color color = new Color(image.getRGB(i, j));
				int red = (int) (color.getRed() * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue = (int) (color.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
				image.setRGB(i, j, newColor.getRGB());
			}
		return image;
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static int[][] getRGBValues(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] data = new int[3][256];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 256; j++) {
				data[i][j] = 0;
			}
		}

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
	
	public static int[] getGrayValues(BufferedImage image) {
		int[] values = new int[256];
		int index = 0;
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				Color color = new Color(image.getRGB(i, j));
				int red = (int) (color.getRed() * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue = (int) (color.getBlue() * 0.114);
				values[index++] = red + green + blue;
			}
		return values;
	}
	
	public static int[] getGrayValues(int[][] image) {
		int[] values = new int[256];
		int index = 0;
		for (int i = 0; i < 256; i++) {
			int red = 	(int) (image[0][i] * 0.299);
			int green = (int) (image[1][i] * 0.587);
			int blue = 	(int) (image[2][i] * 0.114);
			values[index++] = red + green + blue;
		}
		return values;
	}

	public static boolean isGrayscale(int[][] data) {
		int r = 0, g = 0, b = 0;
		while (r < 256 && g < 256 && b < 256) {
			if (data[0][r] != data[1][g] || data[1][g] != data[2][b] || data[0][r] != data[2][b])
				return false;
			r++;
			g++;
			b++;
		}
		return true;
	}

}
