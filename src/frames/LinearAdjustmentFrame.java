package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class LinearAdjustmentFrame extends Frame {

	public final static int MIN_BRIGHTNESS = -255;
	public final static int MAX_BRIGHTNESS = 510;
	public final static int MIN_CONTRAST = -255;
	public final static int MAX_CONTRAST = 510;

	private JSpinner spinnerBrightness, spinerContrast;
	private JButton btnReset;
	
	private double brightness, contrast;

	public LinearAdjustmentFrame(ImageFrame parent) {
		super(parent);
		setTitle("Linear Adjustment: " + parent.getTitle());

		brightness = parent.image.brightness();
		contrast = parent.image.contrast();
		
		JPanel panel = new JPanel(new GridLayout(2, 0, 0, 0));

		spinnerBrightness = new JSpinner(new SpinnerNumberModel(brightness, MIN_BRIGHTNESS, MAX_BRIGHTNESS, 1));
		spinnerBrightness.setBorder(BorderFactory.createTitledBorder("Brightness"));
		panel.add(spinnerBrightness);
		
		spinerContrast = new JSpinner(new SpinnerNumberModel(contrast, MIN_CONTRAST, MAX_CONTRAST, 1));
		spinerContrast.setBorder(BorderFactory.createTitledBorder("Contrast"));
		panel.add(spinerContrast);

		btnReset = new JButton("Reset");
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(btnReset, BorderLayout.SOUTH);

		spinnerBrightness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				refreshBrCn();
			}
		});
		
		spinerContrast.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				refreshBrCn();
			}
		});

		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.image.resetMiddle();
				spinnerBrightness.setValue(brightness);
				spinerContrast.setValue(contrast);
			}
		});
		setMinimumSize(new Dimension(300, 50));
		pack();
		
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    		getParentFrame().image.setMiddleCopy();
		    }
		});
	}
	
	private void refreshBrCn() {
		double contrast = (Double)spinerContrast.getValue();
		double offset = (Double)spinnerBrightness.getValue();
		getParentFrame().image.adjustment2(offset, contrast);
		getParentFrame().getPanel().repaint();
	}

}