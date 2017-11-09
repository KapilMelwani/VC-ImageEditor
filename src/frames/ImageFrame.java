package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import object.FunctionSegment;
import object.MousePixelListener;
import object.RGB;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class ImageFrame extends Frame {

	private JLabel lbInfo;
	private ImagePanel panel;

	public Image image;
	// private BufferedImage image;
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
		getPanel().setFocusable(true);
		getPanel().setPreferredSize(getImageScaledDim());
		add(getLbInfo(), BorderLayout.NORTH);
		getLbInfo().setFocusable(false);
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
		getPanel().setFocusable(true);
		getPanel().setPreferredSize(getImageScaledDim());
		add(getLbInfo(), BorderLayout.NORTH);
		getLbInfo().setFocusable(false);
		pack();
	}

	public BufferedImage getImage() {
		return image.get();
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
		DataBuffer buff = image.get().getRaster().getDataBuffer();
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
		private boolean cs;

		private Point start, end;
		private Point p1, p2;

		public ImagePanel() {
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (!cs)
						return;
					if (SwingUtilities.isRightMouseButton(e) && p2 != null)
						p2 = null;
					else if (SwingUtilities.isRightMouseButton(e) && p1 != null)
						p1 = null;
					else if (p1 == null)
						p1 = e.getPoint();
					else if (p2 == null)
						p2 = e.getPoint();

					repaint();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					start = e.getPoint();
					drag = true;
					repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (!drag || end == null || start == null)
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
					if (drag) {
						if (e.getX() < 0 || e.getY() < 0 || e.getX() > getWidth() || e.getY() > getHeight())
							return;
						end = e.getPoint();
						repaint();
					}
				}
			});

			addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER && cs && p1 != null && p2 != null) {
						FunctionSegment aux = new FunctionSegment(p1, p2);
						int minX = Math.min(p1.x, p2.x);
						int maxX = Math.max(p1.x, p2.x);
						RGB[] pixels = new RGB[maxX-minX];
						int k = 0;
						for(int x = minX; x < maxX; x++) {
							int y = (int) aux.f(x);
							pixels[k++] = new RGB(image.get().getRGB(x, y));
						}
						ImageUtils.launchFrame(new HistogramFrame(pixels, ImageUtils.getImageFrame(e.getComponent())));
						
					}
				}

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image.get(), 0, 0, getWidth(), getHeight(), this);
			if (start != null && end != null && roi) {
				int width = start.x - end.x;
				int height = start.y - end.y;
				int x = Math.min(start.x, end.x);
				int y = Math.min(start.y, end.y);
				g.drawRect(x, y, Math.abs(width), Math.abs(height));
			}
			
			if ((p1 != null || p2 != null) && cs) {
				g.setColor(Color.BLUE);
				if(p1 != null && p2 != null)
					g.drawLine(p1.x, p1.y, p2.x, p2.y);
				g.setColor(Color.GREEN);
				if(p1 != null)
					g.fillOval(p1.x-3, p1.y-3, 6, 6);
				if(p2 != null)
					g.fillOval(p2.x-3, p2.y-3, 6, 6);
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

		public boolean isCS() {
			return cs;
		}

		public void setCS(boolean cs) {
			this.cs = cs;
		}
	}

}
