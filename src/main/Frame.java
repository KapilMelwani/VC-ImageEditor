package main;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	
	protected Frame parent;
	
	public Frame(Frame parent) {
		setParent(parent);
		setLocationRelativeTo(null);
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
