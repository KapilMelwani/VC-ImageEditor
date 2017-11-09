package object;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

import frames.Image;
import frames.ImageFrame;
import frames.ImageFrame.ImagePanel;
import panels.PixelColorPanel;
import utils.ImageUtils;

public class MousePixelListener implements MouseMotionListener {

	private JLabel label;
	private PixelColorPanel colorPanel;

	public MousePixelListener(JLabel label, PixelColorPanel colorPanel) {
		setLabel(label);
		setColorPanel(colorPanel);
	}
	
	public MousePixelListener(MousePixelListener mousePixelListener) {
		setLabel(mousePixelListener.getLabel());
		setColorPanel(mousePixelListener.getColorPanel());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		ImageFrame frame = ImageUtils.getImageFrame(e.getComponent());
		ImagePanel panel = frame.getPanel();
		Image image = frame.image;
		
		int x = (int) (e.getX() * panel.getScale());
		int y = (int) (e.getY() * panel.getScale());
		if(x > image.getWidth() || x < 0 || y > image.getHeight() || y < 0)
			return;
		RGB color = new RGB(image.get().getRGB(x, y));
		colorPanel.setColor(new Color(color.toInt()));
		label.setText(	"x = " + x +
						", y = " + y +
						", [" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + "] " +
						String.format("(#%02x%02x%02x)", color.getRed(), color.getGreen(), color.getBlue()) +
						", gray = " + color.gray());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		ImageFrame frame = ImageUtils.getImageFrame(e.getComponent());
		ImagePanel panel = frame.getPanel();
		Image image = frame.image;
		
		int x = (int) (e.getX() * panel.getScale());
		int y = (int) (e.getY() * panel.getScale());
		if(x > image.getWidth() || x < 0 || y > image.getHeight() || y < 0)
			return;
		RGB color = new RGB(image.get().getRGB(x, y));
		colorPanel.setColor(new Color(color.toInt()));
		label.setText(	"<html>x = " + x +
				", y = " + y +
				", [<font color='red'>" + color.getRed() + "</font>, <font color='green'>" + color.getGreen() + "</font>, <font color='blue'>" + color.getBlue() + "</font>] " +
				String.format("(#%02x%02x%02x)", color.getRed(), color.getGreen(), color.getBlue()) +
				"</font>, [<font color='gray'>" + color.gray() + "</font>]</html>");
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
