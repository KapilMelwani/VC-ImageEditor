package main;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	public int[] normalizedCount() {
		int[] gray = grayCount();
		double[] aux = new double[gray.length];
		double total = lut.length * lut[0].length;
		
		System.out.println(total);
		for(int i = 0; i < aux.length; i++) {
			aux[i] = gray[i] / total;
		}
		double min = minNotZero(aux);
		String text = Double.toString(Math.abs(min));
		int integerPlaces = text.indexOf('.');
		int decimalPlaces = text.length() - integerPlaces - 1;
		int factor = (int) Math.pow(10, decimalPlaces);
		System.out.println(factor);
		for(int i = 0; i < aux.length; i++) {
			double x = aux[i] * factor;
			gray[i] = (int) (aux[i] * factor);
			System.out.print(x + " - ");
		}
		return gray;
	}
	
	private double minNotZero(double[] values) {
		double min = Double.MAX_VALUE;
		for(double i : values)
			if(i != 0.0 && i < min)
				min = i;
		return min;
	}

}
