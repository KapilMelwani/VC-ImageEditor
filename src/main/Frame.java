package main;

import javax.swing.JFrame;

public class Frame extends JFrame {
	
	private Frame parent;
	
	public Frame(Frame parent) {
		setParent(parent);
	}

	/**
	 * @return the parent
	 */
	public Frame getParentFrame() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Frame parent) {
		this.parent = parent;
	}

}
