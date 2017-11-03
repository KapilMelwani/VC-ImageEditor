package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import object.MousePixelListener;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class ImageFrame extends Frame {

	private JLabel lbInfo;
	private ImagePanel panel;

	public Image image;
	//private BufferedImage image;
	private Dimension imageScaledDim;
	private MousePixelListener mousePixelListener;

	public ImageFrame(String file) {
		super(null);
		this.image = new Image(file);
		
		setTitle(image.getFileName());
		setPanel(this.new ImagePanel());
		setLbInfo(new JLabel());
		refreshImageInfo();
		setImageScaledDim(ImageUtils.scaleImage(this.image.getImageDimension()));

		// AWT STUFF
		add(getPanel(), BorderLayout.CENTER);
		getPanel().setPreferredSize(getImageScaledDim());
		add(getLbInfo(), BorderLayout.NORTH);
		pack();
	}

	public ImageFrame(BufferedImage image, ImageFrame parent) {
		super(parent);
		this.image = new Image(image);
		this.image.setPath(parent.image.getPath());
		
		setPanel(this.new ImagePanel());
		setLbInfo(new JLabel());
		refreshImageInfo();
		setImageScaledDim(ImageUtils.scaleImage(this.image.getImageDimension()));

		// AWT STUFF
		add(getPanel(), BorderLayout.CENTER);
		getPanel().setPreferredSize(getImageScaledDim());
		add(getLbInfo(), BorderLayout.NORTH);
		pack();
	}
	
	public BufferedImage getImage() {
		return image.image();
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

	public JLabel getLbInfo() {
		return lbInfo;
	}

	public void setLbInfo(JLabel lbInfo) {
		this.lbInfo = lbInfo;
	}

	public MousePixelListener getMousePixelListener() {
		return mousePixelListener;
	}

	public void setMousePixelListener(MousePixelListener mousePixelListener) {
		this.mousePixelListener = mousePixelListener;
	}
	
	public void resetImage() {
		this.image.reset();
		getPanel().repaint();
	}

	public void refreshImageInfo() {
		getLbInfo().setText(image.getResolution() + " pixels; " + getFileSize());
	}

	public String getFileSize() {
		DataBuffer buff = image.image().getRaster().getDataBuffer();
		int bytes = buff.getSize() * DataBuffer.getDataTypeSize(buff.getDataType()) / 8;

		if (bytes <= 0)
			return "0";
		final String[] units = new String[] { "B", "KiB", "MiB", "GiB", "TiB" };
		int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public void addMousePixelListener(MousePixelListener listener) {
		setMousePixelListener(listener);
		getPanel().addMouseMotionListener(listener);
	}

	public class ImagePanel extends JPanel {
		private boolean drag;
		private boolean roi;
		
		private Point start, end;

		public ImagePanel() {
			addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					start = e.getPoint();
					drag = true;
					repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if(!drag || end == null || start == null)
						return;
					drag = false;
					int width = start.x - end.x;
					int height = start.y - end.y;
					int x = Math.min(start.x, end.x);
					int y = Math.min(start.y, end.y);
					ImageFrame frame = ImageUtils.getImageFrame(e.getComponent());
					width = (int) (Math.abs(width) * getScale());
					height = (int) (Math.abs(height) * getScale());
					ImageUtils.createNewImageFrame(image.getSubimage(x, y, width, height), frame);
					start = null;
					end = null;
					roi = false;
					repaint();
				}
			});
			
			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if(drag) {
						if(e.getX() < 0 || e.getY() < 0 || e.getX() > getWidth() || e.getY() > getHeight())
							return;
						end = e.getPoint();
						repaint();
					}
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image.image(), 0, 0, getWidth(), getHeight(), this);
			if(start != null && end != null && roi) {
				int width = start.x - end.x;
				int height = start.y - end.y;
				int x = Math.min(start.x, end.x);
				int y = Math.min(start.y, end.y);
				g.drawRect(x, y, Math.abs(width), Math.abs(height));
			}
		}
		
		public double getScale() {
			return (double) ((double) image.getWidth() / (double) getWidth());
		}
		public boolean isDrag() {
			return drag;
		}
		public void setDrag(boolean drag) {
			this.drag = drag;
		}
		public boolean isROI() {
			return roi;
		}
		public void setROI(boolean roi) {
			this.roi = roi;
		}
	}

}
