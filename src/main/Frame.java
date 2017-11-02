package main;


import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	public Frame parent;

	public Frame(Frame parent) {
		setParent(parent);
		setLocationRelativeTo(null);
	}

	public Frame getParentFrame() { return parent; }

	public void setParent(Frame parent) { this.parent = parent; }

	public void showParent() {
		parent.setVisible(true);
	}
	
	
}
