package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import main.Frame;
import main.FunctionSegment;
import main.GammaCorrectionFrame;
import main.HistogramFrame;
import main.ImageFrame;
import main.LUT;
import main.LinearAdjustmentFrame;
import main.LinearTranformationFrame;
import main.Node;
import main.MousePixelListener;
import panels.PixelColorPanel;
import panels.PropertiesFrame;

public class ImageUtils {

	public static final double NTSC_RED = 0.299;
	public static final double NTSC_GREEN = 0.587;
	public static final double NTSC_BLUE = 0.114;

	public static BufferedImage readImage(String file) {
		BufferedImage aux = null;
		try {
			aux = ImageIO.read(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return aux;
	}
	
	public static File openImage() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select an image");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and BMP images", "png", "bmp", "tif");
		jfc.addChoosableFileFilter(filter);
		int returnValue = jfc.showOpenDialog(null);
		File file = null;
		if (returnValue == JFileChooser.APPROVE_OPTION)
			file = jfc.getSelectedFile();
		return file;
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

	/**
	 * [0] = RED [1] = GREEN [2] = BLUE
	 * 
	 * @param color
	 * @return
	 */
	public static int[] intToRGB(int color) {
		Color aux = new Color(color);
		int[] rgba = new int[3];
		rgba[0] = aux.getRed();
		rgba[1] = aux.getGreen();
		rgba[2] = aux.getBlue();
		return rgba;
	}
	
	/**
	 * [0] = RED [1] = GREEN [2] = BLUE [3] = ALPHA
	 * 
	 * @param color
	 * @return
	 */
	public static int[] grayToRGB(int gray) {
		int[] rgb = new int[3];
		rgb[0] = (int) (gray / NTSC_RED);
		rgb[1] = (int) (gray / NTSC_GREEN);
		rgb[2] = (int) (gray / NTSC_BLUE);
		return rgb;
	}

	public static int rgbToInt(int red, int green, int blue) {
		return new Color(red, green, blue).getRGB();
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
		BufferedImage image = deepCopy(original);
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
	
	public static BufferedImage copyImage(BufferedImage original) {
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		return image;
	}

	public static BufferedImage rgbToGrayscaleCopyAuto(BufferedImage original) {
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		return image;
	}

	public static BufferedImage linearTransform(BufferedImage original, List<FunctionSegment> f) {
		BufferedImage image = rgbToGrayscaleCopyAuto(original);
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				int value = ImageUtils.intToRGB(image.getRGB(col, row))[0];
				for (FunctionSegment segment : f) {
					if (segment.getP1().getX() <= value) {
						value = ImageUtils.truncate((int) segment.f(value));
						break;
					}
				}
				
				image.setRGB(col, row, ImageUtils.rgbToInt(value, value, value));
			}
		}
		return image;
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static int dynamicRange(BufferedImage image) {
		LUT lut = new LUT(image);
		int[][] grays = lut.getGrayMatrix();
		List<Integer> aux = new ArrayList<Integer>();
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++)
				if(!aux.contains(grays[j][i]))
					aux.add(grays[j][i]);
		return aux.size();
	}

	public static boolean isGrayscale(BufferedImage image) {
		return false;
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

	public static List<FunctionSegment> returnSegments(List<Node> nodes) {
		Collections.sort(nodes);
		List<FunctionSegment> list = new ArrayList<FunctionSegment>();
		for (int i = 1; i < nodes.size(); i++) {
			Node node1 = nodes.get(i - 1);
			Node node2 = nodes.get(i);
			list.add(new FunctionSegment(node1.getCoordinates(), node2.getCoordinates()));
		}
		Collections.sort(list);
		return list;
	}

	public static Point getGrayRange(BufferedImage bi) {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		BufferedImage grayscale = bi;
		if (!(bi.getType() == BufferedImage.TYPE_BYTE_INDEXED))
			grayscale = ImageUtils.rgbToGrayscaleCopyAuto(bi);

		for (int i = 0; i < grayscale.getWidth(); i++)
			for (int j = 0; j < grayscale.getHeight(); j++) {
				Color color = new Color(grayscale.getRGB(i, j));
				if (color.getRed() > max)
					max = color.getRed();
				if (color.getRed() < min)
					min = color.getRed();
			}
		return new Point(min, max);
	}
	
	public static double entropy(BufferedImage bi) {
		
		return 0.0d;
	}

	public static BufferedImage changeBrightness(BufferedImage original, float val) {
		RescaleOp brighterOp = new RescaleOp(val, 0, null);
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		return brighterOp.filter(image, null);
	}

	public static BufferedImage changeContrastBrightness(BufferedImage image, int brightness, float contrast) {
		RescaleOp rescale = new RescaleOp(contrast, brightness, null);
		return rescale.filter(image, null);
	}

	public static double contrast(BufferedImage image) {

		int[] aux = new int[image.getWidth()*image.getHeight()];
		int k = 0;
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				aux[k++] = ImageUtils.brightness(image.getRGB(row, col));
			}
		}
		
		double sum = 0.0, standardDeviation = 0.0;

        for(int num : aux) {
            sum += num;
        }

        double mean = sum/aux.length;

        for(int num: aux) {
            standardDeviation += Math.pow(num - mean, 2);
        }
		return Math.sqrt(standardDeviation/aux.length);
	}
	
	

	public static double brightness(BufferedImage image) {
		int sum = 0;
		int total = image.getWidth() * image.getHeight();
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				sum += ImageUtils.brightness(image.getRGB(row, col));
			}
		}
		return sum / total;
	}

	public static int brightness(Color c) {
		int r = (int) (c.getRed() * c.getRed() * NTSC_RED);
		int g = (int) (c.getGreen() * c.getGreen() * NTSC_GREEN);
		int b = (int) (c.getBlue() * c.getBlue() * NTSC_BLUE);
		return (int) Math.sqrt(r + g + b);
	}

	public static int brightness(int color) {
		int[] c = ImageUtils.intToRGB(color);
		int r = (int) (Math.pow(c[0], 2) * NTSC_RED);
		int g = (int) (Math.pow(c[1], 2) * NTSC_GREEN);
		int b = (int) (Math.pow(c[2], 2) * NTSC_BLUE);
		return (int) Math.sqrt(r + g + b);
	}
	
	public static int gamma(int color, double gamma) {
		int[] rgb = ImageUtils.intToRGB(color);
		double gammaCorrection = 1 / gamma;
		int r = (int) (255 * Math.pow((double)(rgb[0] / 255d), gammaCorrection));
		int g = (int) (255 * Math.pow((double)(rgb[1] / 255d), gammaCorrection));
		int b = (int) (255 * Math.pow((double)(rgb[2] / 255d), gammaCorrection));
		return ImageUtils.rgbToInt(r, g, b);
	}
	
	public static int brighten(int color, int offset) {
		int[] rgb = ImageUtils.intToRGB(color);
		rgb[0] = truncate(rgb[0] + offset);
		rgb[1] = truncate(rgb[1] + offset);
		rgb[2] = truncate(rgb[2] + offset);
		return ImageUtils.rgbToInt(rgb[0], rgb[1], rgb[2]);
	}

	public static int contrast(int color, float contrast) {
		float factor = (259 * (contrast + 255)) / (255 * (259 - contrast));
		int[] rgb = ImageUtils.intToRGB(color);
		rgb[0] = (int) truncate(factor * (rgb[0] - 128) + 128);
		rgb[1] = (int) truncate(factor * (rgb[1] - 128) + 128);
		rgb[2] = (int) truncate(factor * (rgb[2] - 128) + 128);
		return ImageUtils.rgbToInt(rgb[0], rgb[1], rgb[2]);
	}

	public static int truncate(int value) {
		if (value < 0)
			value = 0;
		else if (value > 255)
			value = 255;
		return value;
	}
	
	public static float truncate(float value) {
		if (value < 0)
			value = 0;
		else if (value > 255)
			value = 255;
		return value;
	}
	
	public static Map.Entry<Integer, Integer> grayRange(BufferedImage bi) {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		BufferedImage grayscale = rgbToGrayscaleCopyAuto(bi);

		for (int i = 0; i < grayscale.getWidth(); i++)
			for (int j = 0; j < grayscale.getHeight(); j++) {
				Color color = new Color(grayscale.getRGB(i, j));
				if (color.getRed() > max)
					max = color.getRed();
				if (color.getRed() < min)
					min = color.getRed();
			}
		return new AbstractMap.SimpleEntry<Integer, Integer>(min, max);
	}

	public static void createNewImageFrame(BufferedImage image, ImageFrame parent, JLabel lbCursorInfo,
			PixelColorPanel pnMousePixelColor) {
		ImageFrame frame = new ImageFrame(image, parent);
		frame.getPanel().addMouseMotionListener(new MousePixelListener(lbCursorInfo, pnMousePixelColor));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void createNewImageFrame(BufferedImage image, ImageFrame parent) {
		ImageFrame frame = new ImageFrame(image, parent);
		MousePixelListener aux = parent.getMousePixelListener();
		frame.getPanel().addMouseMotionListener(new MousePixelListener(aux.getLabel(), aux.getColorPanel()));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void createNewImageFrame(ImageFrame parent, JLabel lbCursorInfo, PixelColorPanel pnMousePixelColor) {
		ImageFrame frame = new ImageFrame(parent.getImage(), parent);
		frame.getPanel().addMouseMotionListener(new MousePixelListener(lbCursorInfo, pnMousePixelColor));
		frame.setVisible(true);
	}
	
	public static void launchFrame(Frame parent) {
		Frame frame = new Frame(parent);
		frame.setVisible(true);
	}

}
