package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.ImageUtils;

@SuppressWarnings("serial")
public class HistogramFrame extends Frame {

	private HistogramPanel panel1, panel2, panel3;
	private JTabbedPane tabbedPane;
	private LUT lut;

	private JLabel lbColorValue, lbCount;
	private JButton btnSpecify, btnEqualize;

	public HistogramFrame(Frame parent) {
		super(parent);
		setTitle("Histogram: " + parent.getTitle());
		setLut(new LUT(((ImageFrame) parent).getImage()));
		setTabbedPane(new JTabbedPane(JTabbedPane.TOP));

		setPanel1(new HistogramPanel());
		setPanel2(new HistogramPanel());
		setPanel3(new HistogramPanel());

		if (!lut.isGrayscale()) {
			getPanel1().newHistogramLayer(lut.redCount(), Color.RED, false, "Red");
			getPanel1().newHistogramLayer(lut.greenCount(), Color.GREEN, false, "Green");
			getPanel1().newHistogramLayer(lut.blueCount(), Color.BLUE, false, "Blue");
		}

		getPanel1().newHistogramLayer(lut.grayCount(), Color.DARK_GRAY, true, "Gray");
		getPanel2().newHistogramLayer(lut.cumulativeCount(), Color.MAGENTA, true, "Cumulative");

		getTabbedPane().addTab("Color", getPanel1());
		getTabbedPane().addTab("Cumulative", getPanel2());
		getTabbedPane().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				System.out.println("Tab: " + tabbedPane.getSelectedIndex());
				pack();
			}
		});
		add(getTabbedPane(), BorderLayout.CENTER);

		setLbColorValue(new JLabel("0", SwingConstants.CENTER));
		getLbColorValue().setBorder(BorderFactory.createTitledBorder("Color Value"));

		setLbCount(new JLabel("0", SwingConstants.CENTER));
		getLbCount().setBorder(BorderFactory.createTitledBorder("Ocurrences"));
		
		setBtnSpecify(new JButton("Specify Histogram"));
		getBtnSpecify().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = ImageUtils.openImage();
				if(selectedFile == null)
					return;
			}
		});
		
		setBtnEqualize(new JButton("Equalize"));
		getBtnEqualize().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] acc = lut.cumulativeCount();
				int L = acc.length;
				int a[] = new int[L];
		        for(int i = 0; i < L; i++) {
		            a[i] = (int)Math.floor(((L-1)*acc[i])/(lut.getHeight() * lut.getWidth()));
		        }
		        BufferedImage aux = ImageUtils.copyImage(((ImageFrame)parent).getPanel().getImage());
		        LUT newlut = new LUT(aux);
		        int[][] gray = new int[aux.getWidth()][aux.getHeight()];
		        for(int i = 0; i < aux.getHeight(); i++)
		        	for(int j = 0; j < aux.getWidth(); j++)
		        		gray[j][i] = newlut.getGray(j, i);
		        
		        for(int i = 0; i < aux.getHeight(); i++)
		        	for(int j = 0; j < aux.getWidth(); j++)
		        		gray[j][i] = a[gray[j][i]];
		        
		        for(int i = 0; i < aux.getHeight(); i++)
		        	for(int j = 0; j < aux.getWidth(); j++)
		        		aux.setRGB(j, i, new Color(gray[j][i],gray[j][i],gray[j][i]).getRGB());
		        ImageUtils.createNewImageFrame(aux, (ImageFrame)parent);
			}
		});

		getPanel1().addMouseMotionListener(new MouseHistogramListener(getLbColorValue(), getLbCount()));
		getPanel2().addMouseMotionListener(new MouseHistogramListener(getLbColorValue(), getLbCount()));

		JPanel aux = new JPanel(new GridLayout(1, 4));
		aux.add(lbColorValue);
		aux.add(lbCount);
		aux.add(btnSpecify);
		aux.add(btnEqualize);
		add(aux, BorderLayout.SOUTH);

		setLocationByPlatform(true);
		setResizable(false);
		pack();
	}
	
	public int[] normalize(double[] norm, int height) {
		int[] aux = new int[norm.length];
		for(int i = 0; i < norm.length; i++)
			aux[i] = (int) norm[i] * height;
		return aux;
	}

	public HistogramPanel getPanel1() {
		return panel1;
	}

	public void setPanel1(HistogramPanel panel1) {
		this.panel1 = panel1;
	}

	public HistogramPanel getPanel2() {
		return panel2;
	}

	public void setPanel2(HistogramPanel panel2) {
		this.panel2 = panel2;
	}

	public HistogramPanel getPanel3() {
		return panel3;
	}

	public void setPanel3(HistogramPanel panel3) {
		this.panel3 = panel3;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public LUT getLut() {
		return lut;
	}

	public void setLut(LUT lut) {
		this.lut = lut;
	}

	public JLabel getLbColorValue() {
		return lbColorValue;
	}

	public void setLbColorValue(JLabel lbColorValue) {
		this.lbColorValue = lbColorValue;
	}

	public JLabel getLbCount() {
		return lbCount;
	}

	public void setLbCount(JLabel lbCount) {
		this.lbCount = lbCount;
	}

	public JButton getBtnSpecify() {
		return btnSpecify;
	}

	public void setBtnSpecify(JButton btnSpecify) {
		this.btnSpecify = btnSpecify;
	}

	public JButton getBtnEqualize() {
		return btnEqualize;
	}

	public void setBtnEqualize(JButton btnEqualize) {
		this.btnEqualize = btnEqualize;
	}

}
