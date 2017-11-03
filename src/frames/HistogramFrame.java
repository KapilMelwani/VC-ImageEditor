package frames;

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

import object.LUT;
import object.MouseHistogramListener;
import panels.HistogramPanel;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class HistogramFrame extends Frame {

	private HistogramPanel panel1, panel2, panel3;
	private JTabbedPane tabbedPane;
	private LUT lut;

	private JLabel lbColorValue, lbCount;
	private JButton btnSpecify, btnEqualize, btnEqualizeRGB;

	public HistogramFrame(ImageFrame parent) {
		super(parent);
		setTitle("Histogram: " + parent.getTitle());
		setLut(new LUT(parent.getImage()));
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
				//System.out.println("Tab: " + tabbedPane.getSelectedIndex());
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
				Image desired = new Image(selectedFile.getAbsolutePath());
				double[] sk = new LUT(getParentFrame().getImage()).cumulativeNormalizedCount();
				double[] zk = new LUT(desired.get()).cumulativeNormalizedCount();
				double[] z = new double[sk.length];
				int[] n = new int[sk.length];
				int zki = 0, ski = 0, zi = 0, ni = 0;
				while(zki < zk.length) {
					double value = zk[zki] - sk[ski];
					while(value < 0) {
						value = zk[zki] - sk[++ski];
					}
					z[zi] = value;
					n[ni++] = zki++;
				}
				BufferedImage aux = new Image(ImageUtils.copyImage(parent.getImage())).toGrayScale();
				for(int row = 0; row < aux.getHeight(); row++)
					for(int col = 0; col < aux.getWidth(); col++) {
						int value = new Color(aux.getRGB(col, row)).getRed();
						int newColor = new Color(n[value], n[value], n[value]).getRGB();
						aux.setRGB(col, row, newColor);
					}
				ImageUtils.createNewImageFrame(aux, getParentFrame());	
			}
		});
		
		setBtnEqualize(new JButton("Equalize"));
		getBtnEqualize().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage aux = ImageUtils.copyImage(parent.getImage());
				int[][] gray  = lut.getGrayMatrix();
				int[] grayAcc = lut.cumulativeCount();
				int[][] result = equalizeMatrix(grayAcc, gray);
				for(int i = 0; i < result.length; i++)
		        	for(int j = 0; j < result[i].length; j++)
		        		aux.setRGB(i, j, new Color(result[i][j],result[i][j],result[i][j]).getRGB());
		        ImageUtils.createNewImageFrame(aux, (ImageFrame)parent);
			}
		});
		
		setBtnEqualizeRGB(new JButton("Equalize RGB"));
		getBtnEqualizeRGB().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage aux = ImageUtils.copyImage(parent.getImage());
				int[][] red  = lut.getRedMatrix();
				int[][] green  = lut.getGreenMatrix();
				int[][] blue  = lut.getBlueMatrix();
				int[] redAcc = lut.cumulativeRedCount();
				int[] greenAcc = lut.cumulativeGreenCount();
				int[] blueAcc = lut.cumulativeBlueCount();
				int[][] resultR = equalizeMatrix(redAcc, red);
				int[][] resultG = equalizeMatrix(greenAcc, green);
				int[][] resultB = equalizeMatrix(blueAcc, blue);
				for(int i = 0; i < resultR.length; i++)
		        	for(int j = 0; j < resultR[i].length; j++)
		        		aux.setRGB(i, j, new Color(resultR[i][j],resultG[i][j],resultB[i][j]).getRGB());
		        ImageUtils.createNewImageFrame(aux, (ImageFrame)parent);
			}
		});

		getPanel1().addMouseMotionListener(new MouseHistogramListener(getLbColorValue(), getLbCount()));
		getPanel2().addMouseMotionListener(new MouseHistogramListener(getLbColorValue(), getLbCount()));

		JPanel aux = new JPanel(new GridLayout(1, 2));
		JPanel auxSub1 = new JPanel(new GridLayout(1, 2));
		JPanel auxSub2 = null;
		if(getParentFrame().image.isGrayscale())
			auxSub2 = new JPanel(new GridLayout(1, 2));
		else
			auxSub2 = new JPanel(new GridLayout(1, 3));
		aux.add(auxSub1);
		aux.add(auxSub2);
		
		auxSub1.add(lbColorValue);
		auxSub1.add(lbCount);
		auxSub2.add(btnSpecify);
		auxSub2.add(btnEqualize);
		if(!getParentFrame().image.isGrayscale())
			auxSub2.add(btnEqualizeRGB);
		
		add(aux, BorderLayout.SOUTH);
		pack();
	}
	
	public int[][] equalizeMatrix(int[] cumulative, int[][] color) {
		int L = cumulative.length;
		int a[] = new int[L];
        for(int i = 0; i < L; i++)
            a[i] = (int)Math.floor(((L-1)*cumulative[i])/(color[0].length * color.length));
        
        for(int i = 0; i < color.length; i++)
        	for(int j = 0; j < color[i].length; j++)
        		color[i][j] = a[color[i][j]];
        return color;
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

	public JButton getBtnEqualizeRGB() {
		return btnEqualizeRGB;
	}

	public void setBtnEqualizeRGB(JButton btnEqualizeRGB) {
		this.btnEqualizeRGB = btnEqualizeRGB;
	}

}