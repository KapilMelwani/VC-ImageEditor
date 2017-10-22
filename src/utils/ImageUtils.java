package utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	public static BufferedImage readImage(String file) {
		BufferedImage aux = null;
		try {
			aux = ImageIO.read(new File(file));
		} catch (IOException e) { e.printStackTrace(); }
		return aux;
	}
	
	public static Dimension scaleImage(Dimension image) {
		double scale = 1.0;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension bounds = new Dimension((int)(screen.getWidth()*0.75), (int)(screen.getHeight()*0.75));
		System.out.println("Bounds = " + bounds);
		System.out.println("ImageIn = " + image);
		while(image.getWidth() > bounds.getWidth() || image.getHeight() > bounds.getHeight()) {
			scale -= 0.05;
			int width = (int) (image.getWidth() * scale);
			int height = (int) (image.getHeight() * scale);
			image = new Dimension(width, height);
		}
		System.out.println("ImageOut = " + image);
		return image;
	}
	
	public static String getNameFromPath(String path) {
		if(!path.contains("/"))
			return "";
		String[] tokens = path.split("/");
		return tokens[tokens.length-1];
	}
	
	public static String getExtFromName(String filename) {
		if(!filename.contains("."))
			return "";
		String[] tokens = filename.split(".");
		return tokens[tokens.length-1];
	}

}
