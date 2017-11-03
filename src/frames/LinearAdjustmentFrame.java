package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class LinearAdjustmentFrame extends Frame {

	public final static int MIN_BRIGHTNESS = -255;
	public final static int MAX_BRIGHTNESS = 255;
	public final static int MIN_CONTRAST = -255;
	public final static int MAX_CONTRAST = 255;

	private JSlider sliderBrightness, sliderContrast;
	private JButton btnReset;

	public LinearAdjustmentFrame(ImageFrame parent) {
		super(parent);
		setTitle("Linear Adjustment: " + parent.getTitle());

		JPanel panel = new JPanel(new GridLayout(2, 0, 0, 0));

		sliderBrightness = new JSlider(JSlider.HORIZONTAL, MIN_BRIGHTNESS, MAX_BRIGHTNESS, 0);
		sliderBrightness.setBorder(BorderFactory.createTitledBorder("Brightness"));
		panel.add(sliderBrightness);
		
		sliderContrast = new JSlider(JSlider.HORIZONTAL, MIN_CONTRAST, MAX_CONTRAST, 0);
		sliderContrast.setBorder(BorderFactory.createTitledBorder("Contrast"));
		panel.add(sliderContrast);

		btnReset = new JButton("Reset");
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(btnReset, BorderLayout.SOUTH);

		sliderBrightness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				refreshBrCn();
			}
		});
		
		sliderContrast.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				refreshBrCn();
			}
		});

		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.resetImage();
				sliderContrast.setValue(0);
				sliderBrightness.setValue(0);
			}
		});
		setMinimumSize(new Dimension(300, 50));
		pack();
	}
	
	private void refreshBrCn() {
		int contrast = sliderContrast.getValue();
		int offset = sliderBrightness.getValue();
		getParentFrame().image.adjustment(offset, contrast);
		getParentFrame().getPanel().repaint();
	}

}
