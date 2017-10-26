package utils;

import java.awt.BorderLayout;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import main.FunctionSegment;
import main.HistogramPanel;
import main.ImageFrame;
import main.LinearTranformationFrame;
import main.Node;
import main.NodeList;
import main.MousePixelListener;
import panels.PixelColorPanel;

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
	 * [0] = RED [1] = GREEN [2] = BLUE [3] = ALPHA
	 * 
	 * @param color
	 * @return
	 */
	public static int[] intToRGB(int color) {
		int[] rgba = new int[4];
		rgba[0] = (color) & 0xFF;
		rgba[1] = (color >> 8) & 0xFF;
		rgba[2] = (color >> 16) & 0xFF;
		rgba[3] = (color >> 24) & 0xFF;
		return rgba;
	}

	public static int rgbToInt(int Red, int Green, int Blue) {
		Red = (Red << 16) & 0x00FF0000; // Shift red 16-bits and mask out other stuff
		Green = (Green << 8) & 0x0000FF00; // Shift Green 8-bits and mask out other stuff
		Blue = Blue & 0x000000FF; // Mask out anything not blue.
		return 0xFF000000 | Red | Green | Blue; // 0xFF000000 for 100% Alpha. Bitwise OR everything together.
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
				int value = ImageUtils.intToRGB(image.getRGB(row, col))[0];
				for (FunctionSegment segment : f) {
					if (segment.getP1().getX() < value) {
						value = (int) segment.f(value);
						break;
					}
				}
				image.setRGB(row, col, ImageUtils.rgbToInt(value, value, value));
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
				int red = (int) (color.getRed() * NTSC_RED);
				int green = (int) (color.getGreen() * NTSC_GREEN);
				int blue = (int) (color.getBlue() * NTSC_BLUE);
				values[index++] = red + green + blue;
			}
		return values;
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
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void launchHistogramFrame(BufferedImage image) {
		JFrame frame = new JFrame("Histogram");
		frame.setLayout(new BorderLayout());
		frame.add(new JScrollPane(new HistogramPanel(image)));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void launchLinearTransFrame(BufferedImage image, ImageFrame parent) {
		LinearTranformationFrame frame = new LinearTranformationFrame(parent);
		frame.setVisible(true);
	}

}
