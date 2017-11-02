package object;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

import frames.ImageFrame.ImagePanel;
import panels.PixelColorPanel;

public class MousePixelListener implements MouseMotionListener {

	private JLabel label;
	private PixelColorPanel colorPanel;

	public MousePixelListener(JLabel label, PixelColorPanel colorPanel) {
		setLabel(label);
		setColorPanel(colorPanel);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		ImagePanel aux = (ImagePanel)e.getComponent();
		int x = (int) (e.getX() * aux.getScale());
		int y = (int) (e.getY() * aux.getScale());
		if(x > aux.getImage().getWidth() || x < 0 || y > aux.getImage().getHeight() || y < 0)
			return;
		Color color = new Color(aux.getImage().getRGB(x, y));
		colorPanel.setColor(color);
		label.setText(	"x = " + x +
						", y = " + y +
						", [" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + "] " +
						String.format("(#%02x%02x%02x)", color.getRed(), color.getGreen(), color.getBlue()));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		ImagePanel aux = (ImagePanel)e.getComponent();
		int x = (int) (e.getX() * aux.getScale());
		int y = (int) (e.getY() * aux.getScale());
		if(x > aux.getImage().getWidth() || x < 0 || y > aux.getImage().getHeight() || y < 0)
			return;
		Color color = new Color(aux.getImage().getRGB(x, y));
		colorPanel.setColor(color);
		label.setText(	"<html>x = " + x +
				", y = " + y +
				", [<font color='red'>" + color.getRed() + "</font>, <font color='green'>" + color.getGreen() + "</font>, <font color='blue'>" + color.getBlue() + "</font>] " +
				String.format("(#%02x%02x%02x)", color.getRed(), color.getGreen(), color.getBlue()) + "</html>");
	}

	/**
	 * @return the label
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * @return the colorPanel
	 */
	public PixelColorPanel getColorPanel() {
		return colorPanel;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(JLabel label) {
		this.label = label;
	}

	/**
	 * @param colorPanel the colorPanel to set
	 */
	public void setColorPanel(PixelColorPanel colorPanel) {
		this.colorPanel = colorPanel;
	}
}
