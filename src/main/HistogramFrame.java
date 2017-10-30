package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class HistogramFrame extends Frame {

	private HistogramPanel panel1, panel2, panel3;
	private JTabbedPane tabbedPane;
	private LUT lut;

	private JLabel lbColorValue, lbCount;

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
			getPanel1().newHistogramLayer(lut.redCount(), Color.GREEN, false, "Green");
			getPanel1().newHistogramLayer(lut.redCount(), Color.BLUE, false, "Blue");
		}

		getPanel1().newHistogramLayer(lut.grayCount(), Color.DARK_GRAY, true, "Gray");
		getPanel2().newHistogramLayer(lut.cumulativeCount(), Color.MAGENTA, true, "Cumulative");
		getPanel3().newHistogramLayer(lut.weightedCount(), Color.DARK_GRAY, true, "Weighted");

		getTabbedPane().addTab("Color", getPanel1());
		getTabbedPane().addTab("Cumulative", getPanel2());
		getTabbedPane().addTab("Weighted", getPanel3());
		add(getTabbedPane(), BorderLayout.CENTER);

		setLbColorValue(new JLabel("", SwingConstants.CENTER));
		getLbColorValue().setBorder(BorderFactory.createTitledBorder("Color Value"));
		//getLbColorValue().setHorizontalTextPosition(SwingConstants.CENTER);

		setLbCount(new JLabel("", SwingConstants.CENTER));
		getLbCount().setBorder(BorderFactory.createTitledBorder("Ocurrences"));
		//getLbCount().setHorizontalTextPosition(SwingConstants.CENTER);

		getPanel1().addMouseMotionListener(new MouseHistogramListener(getLbColorValue(), getLbCount()));
		getPanel2().addMouseMotionListener(new MouseHistogramListener(getLbColorValue(), getLbCount()));
		getPanel3().addMouseMotionListener(new MouseHistogramListener(getLbColorValue(), getLbCount()));

		JPanel aux = new JPanel(new GridLayout(1, 2));
		aux.add(lbColorValue);
		aux.add(lbCount);
		add(aux, BorderLayout.SOUTH);

		setLocationByPlatform(true);
		setResizable(false);
		pack();
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

}
