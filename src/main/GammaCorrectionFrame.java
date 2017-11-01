package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.ImageUtils;

@SuppressWarnings("serial")
public class GammaCorrectionFrame extends Frame {
	
	public final static int MIN_GAMMA = 1;
	public final static int MAX_GAMMA = 799;

	private JSlider sliderGamma;
	private JLabel lbFormula;
	private JButton btnReset;
	private BufferedImage image;

	public GammaCorrectionFrame(Frame parent) {
		super(parent);
		setTitle("Gamma Correction: " + parent.getTitle());
		setResizable(false);

		image = ImageUtils.copyImage(((ImageFrame) parent).getImage());

		JPanel panel = new JPanel(new GridLayout(2, 0, 0, 0));

		setSliderGamma(new JSlider(JSlider.HORIZONTAL, MIN_GAMMA, MAX_GAMMA, 100));
		getSliderGamma().setBorder(BorderFactory.createTitledBorder("Gamma Correction"));
		panel.add(getSliderGamma());
		
		setLbFormula(new JLabel("<html>I' = 255 x (<sup>I </sup>&frasl;<sub> 255</sub>)<sup><sup>1 </sup>&frasl;<sub> &gamma</sub></sup></html>", SwingConstants.CENTER));
		getLbFormula().setBorder(BorderFactory.createTitledBorder(""));
		panel.add(getLbFormula());
		
		setBtnReset(new JButton("Reset"));
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(getBtnReset(), BorderLayout.SOUTH);

		getSliderGamma().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				double gamma = getSliderGamma().getValue()/100d;
				gamma(gamma);
				((ImageFrame) parent).getPanel().repaint();
			}
		});

		getBtnReset().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((ImageFrame) parent).setImage(ImageUtils.deepCopy(image));
				((ImageFrame) parent).getPanel().repaint();
				getSliderGamma().setValue(100);
			}
		});
		setMinimumSize(new Dimension(300, 50));
		pack();
	}
	
	private void gamma(double gamma) {
		for (int row = 0; row < image.getHeight(); row++)
			for (int col = 0; col < image.getWidth(); col++) {
				BufferedImage aux = ((ImageFrame) parent).getPanel().getImage();
				aux.setRGB(col, row, ImageUtils.gamma(image.getRGB(col, row), gamma));
			}
	}

	/**
	 * @return the sliderGamma
	 */
	public JSlider getSliderGamma() {
		return sliderGamma;
	}

	/**
	 * @return the lbFormula
	 */
	public JLabel getLbFormula() {
		return lbFormula;
	}

	/**
	 * @return the btnReset
	 */
	public JButton getBtnReset() {
		return btnReset;
	}

	/**
	 * @param sliderGamma the sliderGamma to set
	 */
	public void setSliderGamma(JSlider sliderGamma) {
		this.sliderGamma = sliderGamma;
	}

	/**
	 * @param lbFormula the lbFormula to set
	 */
	public void setLbFormula(JLabel lbFormula) {
		this.lbFormula = lbFormula;
	}

	/**
	 * @param btnReset the btnReset to set
	 */
	public void setBtnReset(JButton btnReset) {
		this.btnReset = btnReset;
	}

}
