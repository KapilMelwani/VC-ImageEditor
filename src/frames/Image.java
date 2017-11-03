package frames;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import utils.ColorUtils;
import utils.ImageUtils;

public class Image {
	
	private BufferedImage image, original;
	private String path;
	
	public Image(String path) {
		setPath(path);
		setImage(ImageUtils.readImage(getPath()));
		setOriginal(ImageUtils.readImage(getPath()));
		System.out.println("A --> " + (image == original));
	}
	
	public Image(BufferedImage image) {
		setPath(null);
		setImage(ImageUtils.copyImage(image));
		setOriginal(ImageUtils.copyImage(image));
		System.out.println("B --> " + (image == original));
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
