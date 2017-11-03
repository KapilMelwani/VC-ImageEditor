package frames;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

import object.FunctionSegment;
import object.LUT;
import utils.ColorUtils;
import utils.ImageUtils;

public class Image {
	
	private BufferedImage image, original;
	private String path;
	
	public Image(String path) {
		setPath(path);
		setImage(ImageUtils.readImage(getPath()));
		setOriginal(ImageUtils.readImage(getPath()));
	}
	
	public Image(BufferedImage image) {
		setPath(null);
		setImage(ImageUtils.copyImage(image));
		setOriginal(ImageUtils.copyImage(image));
	}
	
	public Dimension getImageDimension() {
		return new Dimension(image().getWidth(), image().getHeight());
	}

	public String getFileName() {
		if(getPath() == null)
			return "";
		return ImageUtils.getNameFromPath(getPath());
	}

	public String getFormat() {
		return ImageUtils.getExtFromName(getFileName());
	}

	public String getResolution() {
		return image().getWidth() + "x" + image().getHeight();
	}
	
	public BufferedImage getSubimage(int x, int y, int w, int h) {
		return image().getSubimage(x, y, w, h);
	}
	
	public BufferedImage toGrayScale() {
		return ImageUtils.rgbToGrayscaleCopy(image);
	}
	
	public void reset() {
		setImage(ImageUtils.copyImage(getOriginal()));
	}
	
	public int getWidth() {
		return image().getWidth();
	}
	
	public int getHeight() {
		return image().getHeight();
	}
	
	public void brighten(int offset) {
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				int[] rgb = ColorUtils.intToRGB(original.getRGB(col, row));
				rgb[0] = ImageUtils.truncate(rgb[0] + offset);
				rgb[1] = ImageUtils.truncate(rgb[1] + offset);
				rgb[2] = ImageUtils.truncate(rgb[2] + offset);
				image.setRGB(col, row, ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
	}

	public void contrast(float contrast) {
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				float factor = (259 * (contrast + 255)) / (255 * (259 - contrast));
				int[] rgb = ColorUtils.intToRGB(original.getRGB(col, row));
				rgb[0] = (int) ImageUtils.truncate(factor * (rgb[0] - 128) + 128);
				rgb[1] = (int) ImageUtils.truncate(factor * (rgb[1] - 128) + 128);
				rgb[2] = (int) ImageUtils.truncate(factor * (rgb[2] - 128) + 128);
				System.out.println(original.getRGB(col, row) + " - - " + ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
				image.setRGB(col, row, ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
	}
	
	public void adjustment(int offset, float contrast) {
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				int[] rgb = ColorUtils.intToRGB(original.getRGB(col, row));
				rgb[0] = ImageUtils.truncate(rgb[0] + offset);
				rgb[1] = ImageUtils.truncate(rgb[1] + offset);
				rgb[2] = ImageUtils.truncate(rgb[2] + offset);
				float factor = (259 * (contrast + 255)) / (255 * (259 - contrast));
				rgb[0] = (int) ImageUtils.truncate(factor * (rgb[0] - 128) + 128);
				rgb[1] = (int) ImageUtils.truncate(factor * (rgb[1] - 128) + 128);
				rgb[2] = (int) ImageUtils.truncate(factor * (rgb[2] - 128) + 128);
				image.setRGB(col, row, ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
	}
	
	public boolean isGrayscale() {
		int i = image.getType();
		if(i == BufferedImage.TYPE_BYTE_GRAY)
			return true;
		for (int row = 0; row < image.getHeight(); row++)
			for (int col = 0; col < image.getWidth(); col++) {
				int[] rgb = ColorUtils.intToRGB(original.getRGB(col, row));
				if(rgb[0] != rgb[1] || rgb[1] != rgb[2] || rgb[0] != rgb[2])
					return false;
			}
		return true;
	}
	
	public BufferedImage linearTransform(List<FunctionSegment> f) {
		BufferedImage aux = ImageUtils.copyImage(image());
		for (int row = 0; row < aux.getHeight(); row++) {
			for (int col = 0; col < aux.getWidth(); col++) {
				int[] rgb = ColorUtils.intToRGB(aux.getRGB(col, row));
				for(int i = 0; i <rgb.length; i++) {
					for (FunctionSegment segment : f) {
						if (segment.getP1().getX() <= rgb[i]) {
							rgb[i] = ImageUtils.truncate((int) segment.f(rgb[i]));
							break;
						}
					}
				}
				aux.setRGB(col, row, ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
		return aux;
	}
	
	public double shannonEntropy() {
		double[] normalized = new LUT(image()).normalizedCount();
		double entropy = 0.0d;
		for(int i = 0; i < normalized.length; i++)
			entropy -= normalized[i] * log2(normalized[i]);
		return entropy;
	}
	
	public static double log2(double value) {
		if(value == 0)
			return 0;
		return Math.log(value) / Math.log(2);
	}
	
	public BufferedImage image() { return image; }
	public String getPath() { return path; }
	public void setImage(BufferedImage image) { this.image = image; }
	public void setPath(String path) { this.path = path; }

	public BufferedImage getOriginal() {
		return original;
	}

	public void setOriginal(BufferedImage original) {
		this.original = original;
	}

}
