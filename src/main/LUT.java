package main;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import utils.HistogramUtils;

public class LUT {
	public static final int N_COLOR_VALUES = 256;

	private RGB[][] lut;
	private boolean isGrayscale;

	public LUT(RGB[][] lut) {
		setLut(lut);
		setGrayscale();
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

	public int[] weightedCount() {
		int[] aux = grayCount();
		int total = Arrays.stream(aux).sum();
		for(int i = 0; i < aux.length; i++) {
			System.out.println(aux[i]);
			aux[i] = (aux[i] / total) * 100;
		}
		return aux;
	}

}
