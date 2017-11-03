package frames;


import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	public ImageFrame parent;

	public Frame(ImageFrame parent) {
		setParent(parent);
		setLocationRelativeTo(null);
		if(getParentFrame() != null)
			setTitle(getParentFrame().getTitle());
	}

	public ImageFrame getParentFrame() { return parent; }

	public void setParent(ImageFrame parent) { this.parent = parent; }

	public void showParent() {
		parent.setVisible(true);
	}
	
	
}
