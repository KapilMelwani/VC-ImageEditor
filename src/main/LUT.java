package main;

import java.awt.image.BufferedImage;

import utils.HistogramUtils;

public class LUT {
	public static final int N_COLOR_VALUES = 256;

	private RGB[][] lut;
	private boolean isGrayscale;

	public LUT(RGB[][] lut) {
		setLut(lut);
		setGrayscale();
	}
	
	public LUT(LUT lut) {
		this(lut.getLut());
	}
	
	public LUT() {
		this((RGB[][])null);
	}

	public LUT(BufferedImage image) {
		this(HistogramUtils.getLUT(image));
	}

	public RGB[][] getLut() {
		return lut;
	}

	public void setLut(RGB[][] lut) {
		this.lut = lut;
	}

	private void setGrayscale() {
		for (int i = 0; i < lut.length; i++)
			for (int j = 0; j < lut[i].length; j++)
				if (!lut[i][j].isGrayscale()) {
					isGrayscale = false;
					return;
				}
		isGrayscale = true;
	}
	
	public RGB get(int i, int j) {
		return lut[i][j];
	}
	
	public int getGray(int i, int j) {
		return lut[i][j].gray();
	}
	
	public int getWidth() {
		return lut.length;
	}
	
	public int getHeight() {
		return lut[0].length;
	}

	public boolean isGrayscale() {
		return this.isGrayscale;
	}

	public int[] redCount() {
		int[] aux = new int[N_COLOR_VALUES];
		for (int i = 0; i < aux.length; i++)
			aux[i] = 0;
		for (int i = 0; i < lut.length; i++)
			for (int j = 0; j < lut[i].length; j++)
				aux[lut[i][j].getRed()]++;
		return aux;
	}

	public int[] greenCount() {
		int[] aux = new int[N_COLOR_VALUES];
		for (int i = 0; i < aux.length; i++)
			aux[i] = 0;
		for (int i = 0; i < lut.length; i++)
			for (int j = 0; j < lut[i].length; j++)
				aux[lut[i][j].getGreen()]++;
		return aux;
	}

	public int[] blueCount() {
		int[] aux = new int[N_COLOR_VALUES];
		for (int i = 0; i < aux.length; i++)
			aux[i] = 0;
		for (int i = 0; i < lut.length; i++)
			for (int j = 0; j < lut[i].length; j++)
				aux[lut[i][j].getBlue()]++;
		return aux;
	}

	public int[] grayCount() {
		int[] aux = new int[N_COLOR_VALUES];
		for (int i = 0; i < aux.length; i++)
			aux[i] = 0;
		if (isGrayscale()) {
			aux = redCount();
		} else {
			for (int i = 0; i < lut.length; i++)
				for (int j = 0; j < lut[i].length; j++)
					aux[lut[i][j].gray()]++;
		}
		return aux;
	}

	public int[] cumulativeCount() {
		int[] aux = grayCount();
		for(int i = 1; i < aux.length; i++)
			aux[i] = aux[i] + aux[i-1];
		return aux;
	}

	public double[] normalizedCount() {
		int[] gray = grayCount();
		double[] aux = new double[gray.length];
		double total = lut.length * lut[0].length;
		
		System.out.println(total);
		for(int i = 0; i < aux.length; i++) {
			aux[i] = gray[i] / total;
		}
		return aux;
	}
	
	public int[][] specify(BufferedImage image) {
		double[] norm = new LUT(HistogramUtils.getLUT(image)).normalizedCount();
		int height = image.getHeight();
		int width = image.getWidth();
		int[][] aux = new int[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				if(isGrayscale()) {
					double n = norm[lut[i][j].getRed()];
					aux[i][j] = (int) (n * height * width);
				}
				else {
					double n = norm[lut[i][j].gray()];
					aux[i][j] = (int) (n * height * width);
				}
			}
		return aux;
	}

}
