package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.ImageUtils;

public class LinearAdjustmentFrame extends Frame {

	public final static float MIN_BRIGHTNESS = 0;
	public final static float MAX_BRIGHTNESS = 255;
	public final static float MIN_CONTRAST = 0;
	public final static float MAX_CONTRAST = 2;

	private JSlider sliderBrightness, sliderContrast;
	private JButton btnTransform;
	private int brightness;
	private float contrast;

	public LinearAdjustmentFrame(Frame parent, float brightness, float contrast) {
		super(parent);
		setTitle("Linear Adjustment: " + parent.getTitle());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		BufferedImage image = ((ImageFrame) parent).getImage();
		JPanel panel = new JPanel(new GridLayout(2, 0, 0, 0));
		sliderBrightness = new JSlider(JSlider.HORIZONTAL, (int) MIN_BRIGHTNESS, (int) MAX_BRIGHTNESS,
				(int) brightness);
		sliderBrightness.setBorder(BorderFactory.createTitledBorder("Brightness"));
		panel.add(sliderBrightness);

		sliderContrast = new JSlider(JSlider.HORIZONTAL, (int) MIN_CONTRAST * 100, (int) MAX_CONTRAST * 100,
				(int) contrast * 100);
		sliderContrast.setBorder(BorderFactory.createTitledBorder("Contrast"));
		panel.add(sliderContrast);
		
		btnTransform = new JButton("Transform Image");
		btnTransform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				BufferedImage image = ImageUtils.changeContrastBrightness(((ImageFrame)parent).getImage(), getBrightness(), getContrast());
				ImageUtils.createNewImageFrame(image, (ImageFrame)parent);
				((JFrame) SwingUtilities.getRoot((Component) e.getSource())).dispose();
			}
		});
		
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(btnTransform, BorderLayout.SOUTH);
		
		sliderBrightness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				setBrightness(sliderBrightness.getValue());
			}
		});
		
		sliderBrightness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				setContrast(sliderContrast.getValue()/100);
			}
		});
		
		btnTransform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage image = ImageUtils.changeContrastBrightness(((ImageFrame)parent).getImage(), getBrightness(), getContrast());
				ImageUtils.createNewImageFrame(image, (ImageFrame)parent);
				((JFrame) SwingUtilities.getRoot((Component) e.getSource())).dispose();
			}
		});
	}

	/**
	 * @return the brightness
	 */
	public int getBrightness() {
		return brightness;
	}

	/**
	 * @param brightness the brightness to set
	 */
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	/**
	 * @return the contrast
	 */
	public float getContrast() {
		return contrast;
	}

	/**
	 * @param contrast the contrast to set
	 */
	public void setContrast(float contrast) {
		this.contrast = contrast;
	}

}
