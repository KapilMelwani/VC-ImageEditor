package main;

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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import utils.ImageUtils;

public class ImageFrame extends JFrame {

	// JMENU
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmSave, mntmExit;

	// OTHERS
	private BufferedImage image;
	private ImageFrame parent;
	private Dimension imageScaledDim;
	private ImagePanel panel;
	private String path;

	public ImageFrame(String file) {
		this(ImageUtils.readImage(file), null);
		setPath(file);
		setTitle(getFileName());
	}

	public ImageFrame(BufferedImage image, ImageFrame parent) {
		setImage(image);
		setParent(parent);
		setPanel(new ImagePanel(image));
		if (parent != null)
			setPath(parent.path);
		setImageScaledDim(ImageUtils.scaleImage(getImageDimension()));

		add(getPanel());
		getPanel().setPreferredSize(getImageScaledDim());
		setResizable(false);
		pack();
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public ImagePanel getPanel() {
		return panel;
	}

	public void setPanel(ImagePanel panel) {
		this.panel = panel;
	}

	public ImageFrame getParent() {
		return parent;
	}

	public void setParent(ImageFrame parent) {
		this.parent = parent;
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

	public Dimension getImageDimension() {
		return new Dimension(getImage().getWidth(), getImage().getHeight());
	}

	public String getFileName() {
		return ImageUtils.getNameFromPath(getPath());
	}

	public String getFormat() {
		return ImageUtils.getExtFromName(getFileName());
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
