package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.ImageUtils;

@SuppressWarnings("serial")
public class DigitalizationFrame extends Frame {
	
	private JSpinner sampleSpinner, colorSpinner;
	private JButton btnReset;
	
	private BufferedImage copy;

	public DigitalizationFrame(ImageFrame parent) {
		super(parent);
		setSampleSpinner(new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)));
		getSampleSpinner().setBorder(BorderFactory.createTitledBorder("Samples"));
		
		setColorSpinner(new JSpinner(new SpinnerNumberModel(8, 1, 8, 1)));
		getColorSpinner().setBorder(BorderFactory.createTitledBorder("Color"));

		copy = ImageUtils.copyImage(parent.getImage());
		
		JPanel panel = new JPanel(new GridLayout(1,2));
		panel.add(getSampleSpinner());
		panel.add(getColorSpinner());
		
		setBtnReset(new JButton("Reset"));
		
		add(panel, BorderLayout.CENTER);
		add(getBtnReset(), BorderLayout.SOUTH);
		
		getSampleSpinner().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				getParentFrame().image.downsample((Integer) getSampleSpinner().getValue());
				getParentFrame().getPanel().repaint();
			}
		});
		
		getColorSpinner().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				getParentFrame().image.changeColorDepth2((Integer) getColorSpinner().getValue());
				getParentFrame().getPanel().repaint();
			}
		});
		
		getBtnReset().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.image.reset(copy);
				getSampleSpinner().setValue(1);
				getColorSpinner().setValue(8);
			}
		});
		setPreferredSize(new Dimension(300,100));
		pack();
	}

	public JSpinner getSampleSpinner() { return sampleSpinner; }
	public JSpinner getColorSpinner() { return colorSpinner; }
	public void setSampleSpinner(JSpinner sampleSpinner) { this.sampleSpinner = sampleSpinner; }
	public void setColorSpinner(JSpinner colorSpinner) { this.colorSpinner = colorSpinner; }
	public JButton getBtnReset() { return btnReset; }
	public void setBtnReset(JButton btnReset) { this.btnReset = btnReset; }

}
