package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import object.FunctionSegment;
import object.LUT;
import object.RGB;
import utils.ColorUtils;
import utils.ImageUtils;

public class Image {
	
	private BufferedImage image, original, middleCopy;
	private String path;
	
	public Image(String path) {
		setPath(path);
		setImage(ImageUtils.readImage(getPath()));
		setOriginal(ImageUtils.readImage(getPath()));
		setMiddleCopy();
	}
	
	public Image(BufferedImage image) {
		setPath(null);
		setImage(ImageUtils.copyImage(image));
		setOriginal(ImageUtils.copyImage(image));
		setMiddleCopy();
	}
	
	public Dimension getImageDimension() {
		return new Dimension(get().getWidth(), get().getHeight());
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
		return get().getWidth() + "x" + get().getHeight();
	}
	
	public BufferedImage getSubimage(int x, int y, int w, int h) {
		return get().getSubimage(x, y, w, h);
	}
	
	public BufferedImage toGrayScale() {
		return ImageUtils.rgbToGrayscaleCopy(image);
	}
	
	public void reset() {
		setImage(ImageUtils.copyImage(getOriginal()));
	}
	
	public void resetMiddle() {
		setImage(ImageUtils.copyImage(getMiddleCopy()));
	}
	
	public void reset(BufferedImage image) {
		setImage(ImageUtils.copyImage(image));
		original = ImageUtils.copyImage(image);
	}
	
	public int getWidth() {
		return get().getWidth();
	}
	
	public int getHeight() {
		return get().getHeight();
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
	
	public void adjustment2(double brightness, double contrast) {
		double A = contrast / ImageUtils.contrast(getMiddleCopy());
		double B = brightness - ImageUtils.brightness(getMiddleCopy()) * A;
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				int[] rgb = ColorUtils.intToRGB(getMiddleCopy().getRGB(col, row));
				rgb[0] = ImageUtils.truncate((int) (rgb[0] * A + B));
				rgb[1] = ImageUtils.truncate((int) (rgb[1] * A + B));
				rgb[2] = ImageUtils.truncate((int) (rgb[2] * A + B));
				image.setRGB(col, row, ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
	}
	
	public double brightness() {
		int sum = 0;
		int total = image.getWidth() * image.getHeight();
		for (int row = 0; row < image.getHeight(); row++)
			for (int col = 0; col < image.getWidth(); col++)
				sum += ImageUtils.brightness(image.getRGB(col, row));
		return (double)sum / (double)total;
	}
	
	public double contrast() {
		int[] aux = new int[image.getWidth()*image.getHeight()];
		int k = 0;
		for (int row = 0; row < image.getHeight(); row++)
			for (int col = 0; col < image.getWidth(); col++)
				aux[k++] = ImageUtils.brightness(image.getRGB(col, row));
		
		double sum = 0.0, standardDeviation = 0.0;
        for(int num : aux)
            sum += num;

        double mean = (double)sum/(double)aux.length;
        for(int num: aux)
            standardDeviation += Math.pow(num - mean, 2);
        
		return Math.sqrt((double)standardDeviation/(double)aux.length);
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
		//BufferedImage aux = ImageUtils.copyImage(get());
		for (int row = 0; row < get().getHeight(); row++) {
			for (int col = 0; col < get().getWidth(); col++) {
				int[] rgb = ColorUtils.intToRGB(getMiddleCopy().getRGB(col, row));
				for(int i = 0; i <rgb.length; i++) {
					for (FunctionSegment segment : f) {
						if (segment.contains(rgb[i])) {
							rgb[i] = ImageUtils.truncate((int) segment.f(rgb[i]));
							break;
						}
					}
				}
				get().setRGB(col, row, ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
		return null;
	}
	
	public Image difference(Image newImage) {
		if(!this.getResolution().equals(newImage.getResolution()))
			return null;
		BufferedImage result = ImageUtils.copyImage(newImage.get());
		for (int row = 0; row < result.getHeight(); row++) {
			for (int col = 0; col < result.getWidth(); col++) {
				RGB color = get(col, row).absDifference(newImage.get(col, row));
				result.setRGB(col, row, color.toInt());
			}
		}
		Image aux = new Image(result);
		aux.setPath(getPath());
		return aux;
	}
	
	public BufferedImage difference(BufferedImage newImage) {
		if(getWidth() != newImage.getWidth() || getHeight() != newImage.getHeight())
			return null;
		BufferedImage result = ImageUtils.copyImage(newImage);
		for (int row = 0; row < result.getHeight(); row++) {
			for (int col = 0; col < result.getWidth(); col++) {
				RGB a = new RGB(newImage.getRGB(col, row));
				RGB color = get(col, row).absDifference(a);
				result.setRGB(col, row, color.toInt());
			}
		}
		return result;
	}
	
	public RGB get(int x, int y) {
		return new RGB(get().getRGB(x, y));
	}
	
	public double shannonEntropy() {
		double[] normalized = new LUT(get()).normalizedCount();
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
	
	public BufferedImage downsample(int sampleSize) {
		BufferedImage aux = ImageUtils.copyImage(getMiddleCopy());
		
		for (int row = 0; row < aux.getHeight(); row += sampleSize) {
			for (int col = 0; col < aux.getWidth(); col += sampleSize) {
				RGB[][] sample = null;
				int width = 0, height = 0;
				if(row+sampleSize >= aux.getHeight())
					width = Math.abs(sampleSize - (aux.getHeight() - row+sampleSize));
				else
					width = sampleSize;
				if(col+sampleSize >= aux.getWidth())
					height = Math.abs(sampleSize - (aux.getWidth() - col+sampleSize));
				else
					height = sampleSize;
				int side = Math.min(width, height);
				sample = new RGB[side][side];
					
				for (int i = 0; i < side; i++) {
					for (int j = 0; j < side; j++) {
						sample[i][j] = new RGB(aux.getRGB(col+i, row+j));
					}
				}
				RGB average = RGB.average(sample);
				for (int i = col; i < col+side; i++) {
					for (int j = row; j < row+side; j++) {
						get().setRGB(i, j, average.toInt());
					}
				}
			}
		}
		return aux;
	}
	
	public void changeColorDepth(int bits) {
		int colors = (int) Math.pow(2, bits);
		int length = 256/colors;
		for (int row = 0; row < getMiddleCopy().getHeight(); row++) {
			for (int col = 0; col < getMiddleCopy().getWidth(); col++) {
				int preColor = getMiddleCopy().getRGB(col, row);
				RGB preRGB = new RGB(preColor);
				preRGB.approxMod(length);
				image.setRGB(col, row, preRGB.toInt());
			}
		}
	}
	
	public void gamma(double gamma) {
		RGB[][] lut = new LUT(getMiddleCopy()).getLut();
		for (int row = 0; row < get().getHeight(); row++)
			for (int col = 0; col < get().getWidth(); col++) {
				RGB i = lut[col][row].gamma(gamma);
				get().setRGB(col, row, i.toInt());
			}
	}
	
	public int dynamicRange() {
		RGB[][] lut = new LUT(get()).getLut();
		List<Integer> aux = new ArrayList<Integer>();
		for (int i = 0; i < get().getWidth(); i++)
			for (int j = 0; j < get().getHeight(); j++)
				if(!aux.contains(lut[i][j].gray()))
					aux.add(lut[i][j].gray());
		return aux.size();
	}
	
	public Point grayRange() {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		RGB[][] lut = new LUT(get()).getLut();
		for (int i = 0; i < get().getWidth(); i++)
			for (int j = 0; j < get().getHeight(); j++) {
				int gray = lut[i][j].gray();
				if (gray > max)
					max = gray;
				if (gray < min)
					min = gray;
			}
		return new Point(min, max);
	}
	
	public BufferedImage colorChangeMap(int threshold) {
		BufferedImage aux = ImageUtils.copyImage(get());
		RGB[][] rgb = new LUT(aux).getLut();
		for (int row = 0; row < get().getHeight(); row++)
			for (int col = 0; col < get().getWidth(); col++)
				if(rgb[col][row].gray() >= threshold)
					aux.setRGB(col, row, Color.RED.getRGB());
		return aux;
	}
	
	public BufferedImage get() { return image; }
	public String getPath() { return path; }
	public BufferedImage getOriginal() { return original; }
	public BufferedImage getMiddleCopy() { return middleCopy; }
	public void setImage(BufferedImage image) { this.image = image; }
	public void setPath(String path) { this.path = path; }
	public void setOriginal(BufferedImage original) { this.original = original; }
	public void setMiddleCopy(BufferedImage middleCopy) { this.middleCopy = ImageUtils.copyImage(middleCopy); }
	public void setMiddleCopy() { this.middleCopy = ImageUtils.copyImage(get()); }

}
