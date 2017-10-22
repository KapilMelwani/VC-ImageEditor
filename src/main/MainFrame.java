package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import main.ImageFrame.ImagePanel;
import panels.PixelColorPanel;
import utils.ImageUtils;

import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {
	
	// JMENU
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpen, mntmSave, mntmExit, mntmToGrayscale, mntmHistogram;
	
	// OTHERS
	private JPanel contentPane;
	private JLabel lbCursorInfo;
	private PixelColorPanel pnMousePixelColor;

	private ImageFrame focusedFrame;
	private JMenu mnEdit, mntmColor;
	private JRadioButtonMenuItem rbmiRGB, rbmiGrayscale;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		focusedFrame = null;
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open...");
		mntmOpen.setSelectedIcon(new ImageIcon(MainFrame.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Select an image");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and BMP images", "png", "bmp");
				jfc.addChoosableFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					System.out.println("Opening " + selectedFile.getAbsolutePath());
					createNewImageFrame(selectedFile.getAbsolutePath());
				}

			}
		});
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Save...");
		mntmSave.setSelectedIcon(new ImageIcon(MainFrame.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				String format = focusedFrame.getFormat();
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				FileNameExtensionFilter filter = new FileNameExtensionFilter(format + " images", format);
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.addChoosableFileFilter(filter);
				int returnValue = jfc.showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					System.out.println("Saving to " + selectedFile.getAbsolutePath());
					try {
						ImageIO.write(focusedFrame.getImage(), format, new File(selectedFile.getAbsolutePath() + "." + format));
					} catch (IOException e1) { e1.printStackTrace(); }
				}

			}
		});
		mnFile.add(mntmSave);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.setSelectedIcon(new ImageIcon(MainFrame.class.getResource("/javax/swing/plaf/metal/icons/ocean/close-pressed.gif")));
		mnFile.add(mntmExit);
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmColor = new JMenu("Color");
		// ButtonGroup for radio buttons
	    ButtonGroup directionGroup = new ButtonGroup();

	    // Edit->Options->Forward, F - Mnemonic, in group
	    JRadioButtonMenuItem rbmiRGB = new JRadioButtonMenuItem("RGB", true);
	    mntmColor.add(rbmiRGB);
	    directionGroup.add(rbmiRGB);

	    // Edit->Options->Backward, B - Mnemonic, in group
	    JRadioButtonMenuItem rbmiGrayscale = new JRadioButtonMenuItem("Grayscale");
	    mntmColor.add(rbmiGrayscale);
	    directionGroup.add(rbmiGrayscale);
	    
	    mntmToGrayscale = new JMenuItem("Convert to Grayscale");
	    mntmToGrayscale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				createNewImageFrame(ImageUtils.rgbToGrayscaleCopy(focusedFrame.getImage()));
			}
		});
	    mntmColor.add(mntmToGrayscale);
	    
		mnEdit.add(mntmColor);
		
		mntmHistogram = new JMenuItem("Histogram");
		mntmHistogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				launchHistogramFrame(focusedFrame.getImage());
			}
		});	    
		mnEdit.add(mntmHistogram);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JPanel pnInfo = new JPanel();
		contentPane.add(pnInfo, BorderLayout.CENTER);
		pnInfo.setLayout(new GridLayout(0, 2, 0, 0));

		lbCursorInfo = new JLabel();
		pnInfo.add(lbCursorInfo);

		JButton btnImage = new JButton("Image");
		JPanel aux = new JPanel(new GridLayout(1, 2));
		pnMousePixelColor = new PixelColorPanel();
		aux.add(pnMousePixelColor);
		aux.add(btnImage);
		pnInfo.add(aux);
		pack();
		setSize(getWidth() * 2, getHeight());
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new FocusChangeListener());

	}

	private void createNewImageFrame(String file) {
		ImageFrame frame = new ImageFrame(file);
		frame.getPanel().addMouseMotionListener(new MousePixelListener(lbCursorInfo, pnMousePixelColor));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void createNewImageFrame(BufferedImage image) {
		ImageFrame frame = new ImageFrame(image, focusedFrame);
		frame.getPanel().addMouseMotionListener(new MousePixelListener(lbCursorInfo, pnMousePixelColor));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void launchHistogramFrame(BufferedImage image) {
		JFrame frame = new JFrame("Histogram");
		frame.setLayout(new BorderLayout());
		frame.add(new JScrollPane(new HistogramPanel(image)));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public class MousePixelListener implements MouseMotionListener {

		private JLabel label;
		private PixelColorPanel colorPanel;

		public MousePixelListener(JLabel label, PixelColorPanel colorPanel) {
			this.label = label;
			this.colorPanel = colorPanel;
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
	}

	class FocusChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			Component oldComp = (Component) evt.getOldValue();
			Component newComp = (Component) evt.getNewValue();

			if(!(newComp instanceof ImageFrame))
				return;
			else if(newComp instanceof ImageFrame)
				focusedFrame = (ImageFrame) newComp;
				
			
			if ("focusOwner".equals(evt.getPropertyName())) {
				if (oldComp == null) {
					System.out.println(newComp.getName());
				} else {
					System.out.println(oldComp.getName());
				}
			} else if ("focusedWindow".equals(evt.getPropertyName())) {
				if (oldComp == null) {
					System.out.println(newComp.getName());
				} else {
					System.out.println(oldComp.getName());
				}
			}
		}
	}

}