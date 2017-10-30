package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.PixelGrabber;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import utils.ImageUtils;

public class ImageFrame extends Frame {

	private JLabel lbInfo;
	private MousePixelListener mousePixelListener;

	// OTHERS
	private BufferedImage image;
	private Dimension imageScaledDim;
	private ImagePanel panel;
	private String path;

	public ImageFrame(String file) {
		this(ImageUtils.readImage(file), null);
		setPath(file);
		setTitle(getFileName());
	}

	public ImageFrame(BufferedImage image, Frame parent) {
		super(parent);
		setImage(image);
		setParent(parent);
		setPanel(new ImagePanel(image));
		setLbInfo(new JLabel());
		refreshImageInfo();
		setImageScaledDim(ImageUtils.scaleImage(getImageDimension()));
		if (parent != null) {
			setPath(((ImageFrame)parent).path);
			//setMousePixelListener(((ImageFrame)parent).getMousePixelListener());
		}

		add(getPanel(), BorderLayout.CENTER);
		getPanel().setPreferredSize(getImageScaledDim());
		add(getLbInfo(), BorderLayout.NORTH);
		setResizable(false);
		pack();
		System.out.println(getFileSize());
		System.out.println(ImageUtils.brightness(image));
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		if(getPanel() != null) {
			getPanel().setImage(getImage());
			getPanel().repaint();
		}
	}

	public ImagePanel getPanel() {
		return panel;
	}

	public void setPanel(ImagePanel panel) {
		this.panel = panel;
	}

	public Dimension getImageScaledDim() {
		return imageScaledDim;
	}

	public void setImageScaledDim(Dimension imageScaledDim) {
		this.imageScaledDim = imageScaledDim;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public JLabel getLbInfo() {
		return lbInfo;
	}

	public void setLbInfo(JLabel lbInfo) {
		this.lbInfo = lbInfo;
	}

	/**
	 * @return the mousePixelListener
	 */
	public MousePixelListener getMousePixelListener() {
		return mousePixelListener;
	}

	/**
	 * @param mousePixelListener the mousePixelListener to set
	 */
	public void setMousePixelListener(MousePixelListener mousePixelListener) {
		this.mousePixelListener = mousePixelListener;
	}

	public Dimension getImageDimension() {
		return new Dimension(getImage().getWidth(), getImage().getHeight());
	}

	public String getFileName() {
		return ImageUtils.getNameFromPath(getPath());
	}

	public String getFormat() {
		return ImageUtils.getExtFromName(getFileName());
	}

	public String getResolution() {
		return getImage().getWidth() + "x" + getImage().getHeight();
	}

	public void refreshImageInfo() {
		getLbInfo().setText(getResolution() + " pixels; " + getFileSize());
	}

	public String getFileSize() {
		DataBuffer buff = getImage().getRaster().getDataBuffer();
		int bytes = buff.getSize() * DataBuffer.getDataTypeSize(buff.getDataType()) / 8;

		if (bytes <= 0)
			return "0";
		final String[] units = new String[] { "B", "KiB", "MiB", "GiB", "TiB" };
		int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public void addMousePixelListener(MousePixelListener listener) {
		setMousePixelListener(listener);
		panel.addMouseMotionListener(listener);
	}

	public class ImagePanel extends JPanel {
		private BufferedImage image;

		public ImagePanel(BufferedImage image) {
			setImage(image);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(getImage(), 0, 0, getWidth(), getHeight(), this);
		}

		public BufferedImage getImage() {
			return image;
		}

		public void setImage(BufferedImage image) {
			this.image = image;
		}

		public double getScale() {
			return (double) ((double) image.getWidth() / (double) getWidth());
		}
	}

}
