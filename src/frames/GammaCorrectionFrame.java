package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class GammaCorrectionFrame extends Frame {
	
	public final static double MIN_GAMMA = 0.01;
	public final static double MAX_GAMMA = 40.00;

	private JSpinner spinnerGamma;
	private JLabel lbFormula;
	private JButton btnReset;

	public GammaCorrectionFrame(ImageFrame parent) {
		super(parent);
		super.setTitle("Gamma Correction: " + parent.getTitle());

		JPanel panel = new JPanel(new GridLayout(2, 0, 0, 0));

		setSpinnerGamma(new JSpinner(new SpinnerNumberModel(1.00, MIN_GAMMA, MAX_GAMMA, 0.01)));
		getSpinnerGamma().setBorder(BorderFactory.createTitledBorder("Gamma Correction"));
		panel.add(getSpinnerGamma());
		
		setLbFormula(new JLabel("<html>I' = 255 x (<sup>I </sup>&frasl;<sub> 255</sub>)<sup><sup>1 </sup>&frasl;<sub> &gamma</sub></sup></html>", SwingConstants.CENTER));
		getLbFormula().setBorder(BorderFactory.createTitledBorder(""));
		panel.add(getLbFormula());
		
		setBtnReset(new JButton("Reset"));
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(getBtnReset(), BorderLayout.SOUTH);

		getSpinnerGamma().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				double gamma = (Double)getSpinnerGamma().getValue();
				getParentFrame().image.gamma(gamma);
				parent.getPanel().repaint();
			}
		});

		getBtnReset().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.resetImage();
				getSpinnerGamma().setValue(100);
			}
		});
		setMinimumSize(new Dimension(300, 50));
		pack();
	}
	/*
	private void gamma(double gamma) {
		for (int row = 0; row < image.getHeight(); row++)
			for (int col = 0; col < image.getWidth(); col++) {
				BufferedImage aux = parent.getImage();
				aux.setRGB(col, row, ImageUtils.gamma(image.getRGB(col, row), gamma));
			}
	}
*/
	public JLabel getLbFormula() { return lbFormula; }
	public JButton getBtnReset() { return btnReset; }
	public void setLbFormula(JLabel lbFormula) { this.lbFormula = lbFormula; }
	public void setBtnReset(JButton btnReset) { this.btnReset = btnReset; }
	public JSpinner getSpinnerGamma() { return spinnerGamma; }
	public void setSpinnerGamma(JSpinner spinnerGamma) { this.spinnerGamma = spinnerGamma; }

}
