package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import panels.LTInfoPanel;
import panels.LinearTransformationPanel;

public class LinearTranformationFrame extends JFrame {
	
	private ImageFrame parent;
	private LinearTransformationPanel panel;
	
	public LinearTranformationFrame(ImageFrame parent) {
		setPanel(new LinearTransformationPanel());
		setParent(parent);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(getPanel(), BorderLayout.CENTER);
		getContentPane().add(new LTInfoPanel(getPanel().returnSegments()), BorderLayout.SOUTH);
		setLocationByPlatform(true);
		setResizable(false);
		pack();
	}

	public ImageFrame getParent() {
		return parent;
	}

	public void setParent(ImageFrame parent) {
		this.parent = parent;
	}

	public LinearTransformationPanel getPanel() {
		return panel;
	}

	public void setPanel(LinearTransformationPanel panel) {
		this.panel = panel;
	}
	
}
