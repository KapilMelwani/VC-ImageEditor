package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.ImageUtils;

@SuppressWarnings("serial")
public class DifferenceFrame extends Frame {

	public final static int MIN_THRESHOLD = 0;
	public final static int MAX_THRESHOLD = 255;
	
	private JSpinner spinnerThreshold;
	
	private JTextField tfFilePath;
	private JButton btnFile, btnDifference, btnChangeMap;
	
	public DifferenceFrame(ImageFrame parent) {
		super(parent);
		setSpinnerThreshold(new JSpinner(new SpinnerNumberModel(128, MIN_THRESHOLD, MAX_THRESHOLD, 1)));
		getSpinnerThreshold().setBorder(BorderFactory.createTitledBorder("Threshold"));
		getSpinnerThreshold().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
			}
		});
		
		setLayout(new GridLayout(2,2));
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Image"));
		setTFFilePath(new JTextField("/Route/of/Image/For/Difference"));
		getTFFilePath().setEditable(false);
		setBtnFile(new JButton("Image..."));
		getBtnFile().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = ImageUtils.openImage();
				if(selectedFile == null)
					return;
				getTFFilePath().setText(selectedFile.getAbsolutePath());
			}
		});
		
		panel.add(getTFFilePath(), BorderLayout.CENTER);
		panel.add(getBtnFile(), BorderLayout.EAST);
		
		JPanel panel1 = new JPanel(new GridLayout(1, 2));
		setBtnDifference(new JButton("Difference"));
		setBtnChangeMap(new JButton("Change Map"));
		getBtnDifference().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getTFFilePath().getText() == null || getTFFilePath().getText().equals("/Route/of/Image/For/Difference"))
					return;
				Image image = getParentFrame().image.difference(new Image(getTFFilePath().getText()));
				ImageUtils.createNewImageFrame(image, getParentFrame());
			}
		});
		getBtnChangeMap().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getTFFilePath().getText() == null || getTFFilePath().getText().equals("/Route/of/Image/For/Difference"))
					return;
				Image image = getParentFrame().image.difference(new Image(getTFFilePath().getText()));
				BufferedImage aux = image.colorChangeMap((int) getSpinnerThreshold().getValue());
				ImageUtils.createNewImageFrame(aux, getParentFrame());
			}
		});
		
		panel1.add(getBtnDifference());
		panel1.add(getBtnChangeMap());
		
		add(panel);
		add(getSpinnerThreshold());
		add(new JPanel());
		add(panel1);
		
		pack();
	}

	public JSpinner getSpinnerThreshold() {
		return spinnerThreshold;
	}

	public void setSpinnerThreshold(JSpinner spinnerThreshold) {
		this.spinnerThreshold = spinnerThreshold;
	}

	public JTextField getTFFilePath() {
		return tfFilePath;
	}

	public void setTFFilePath(JTextField tfFilePath) {
		this.tfFilePath = tfFilePath;
	}

	public JButton getBtnFile() {
		return btnFile;
	}

	public void setBtnFile(JButton btnFile) {
		this.btnFile = btnFile;
	}

	public JButton getBtnDifference() {
		return btnDifference;
	}

	public void setBtnDifference(JButton btnDifference) {
		this.btnDifference = btnDifference;
	}

	public JButton getBtnChangeMap() {
		return btnChangeMap;
	}

	public void setBtnChangeMap(JButton btnChangeMap) {
		this.btnChangeMap = btnChangeMap;
	}

}
