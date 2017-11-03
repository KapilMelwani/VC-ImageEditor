package frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import panels.PixelColorPanel;
import panels.PropertiesFrame;
import utils.ImageUtils;

import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButtonMenuItem;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	// JMENU
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpen, mntmSave, mntmExit, mntmToGrayscale, mntmHistogram, mntmLinearTrans, mntmLinearAdjust, mntmGammaCorrect, mntmShowOriginal, mntmProperties;
	
	// OTHERS
	private JPanel contentPane;
	private JLabel lbCursorInfo;
	private PixelColorPanel pnMousePixelColor;

	private ImageFrame focusedFrame;
	private JMenu mnEdit, mntmColor, mnOrignal;
	//private JRadioButtonMenuItem rbmiRGB, rbmiGrayscale;
	
	public static List<Frame> frames = new ArrayList<Frame>();

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 450, 300);
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
				File selectedFile = ImageUtils.openImage();
				if(selectedFile == null)
					return;
				System.out.println("Opening " + selectedFile.getAbsolutePath());
				ImageUtils.createNewImageFrame(selectedFile.getAbsolutePath(), lbCursorInfo, pnMousePixelColor);

			}
		});
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Save...");
		mntmSave.setSelectedIcon(new ImageIcon(MainFrame.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				String format = focusedFrame.image.getFormat();
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				FileNameExtensionFilter filter = new FileNameExtensionFilter(format + " images", format);
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.addChoosableFileFilter(filter);
				int returnValue = jfc.showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					System.out.println("Saving to " + selectedFile.getAbsolutePath());
					try {
						ImageIO.write(focusedFrame.image.get(), format, new File(selectedFile.getAbsolutePath() + "." + format));
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
				ImageUtils.createNewImageFrame(focusedFrame.image.toGrayScale(), focusedFrame);
			}
		});
	    mntmColor.add(mntmToGrayscale);
	    
		mnEdit.add(mntmColor);
		
		mntmHistogram = new JMenuItem("Histogram");
		mntmHistogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				ImageUtils.launchFrame(new HistogramFrame(focusedFrame));
			}
		});	    
		mnEdit.add(mntmHistogram);
		
		mntmLinearTrans = new JMenuItem("Linear Transformation");
		mntmLinearTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				ImageUtils.launchFrame(new LinearTranformationFrame(focusedFrame));
			}
		});	    
		mnEdit.add(mntmLinearTrans);
		
		mntmLinearAdjust = new JMenuItem("Linear Adjustment");
		mntmLinearAdjust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				ImageUtils.launchFrame(new LinearAdjustmentFrame(focusedFrame));
				//ImageUtils.launchFrame(new LinearAdjustmentSliderFrame(focusedFrame));
			}
		});	    
		mnEdit.add(mntmLinearAdjust);
		
		mntmGammaCorrect = new JMenuItem("Gamma Correction");
		mntmGammaCorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				ImageUtils.launchFrame(new GammaCorrectionFrame(focusedFrame));
			}
		});	    
		mnEdit.add(mntmGammaCorrect);
		
		mntmProperties = new JMenuItem("Properties");
		mntmProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				ImageUtils.launchFrame(new PropertiesFrame(focusedFrame));
			}
		});	    
		mnEdit.add(mntmProperties);
		
		mnOrignal = new JMenu("Original");
		menuBar.add(mnOrignal);
		
		mntmShowOriginal = new JMenuItem("Show Original");
		mntmShowOriginal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(focusedFrame == null)
					return;
				focusedFrame.showParent();
			}
		});	    
		mnOrignal.add(mntmShowOriginal);
		
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
		btnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageUtils.createNewImageFrame("/Users/peter/Drive/VC/lena.png", lbCursorInfo, pnMousePixelColor);
			}
		});
		
		JButton btnCrop = new JButton("Crop");
		btnCrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((ImageFrame)focusedFrame).getPanel().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				((ImageFrame)focusedFrame).getPanel().setROI(true);
			}
		});
		JPanel pnButtons = new JPanel(new GridLayout(1, 2));
		pnButtons.add(btnCrop);
		pnButtons.add(btnImage);
		
		JPanel aux = new JPanel(new GridLayout(1, 2));
		pnMousePixelColor = new PixelColorPanel();
		aux.add(pnMousePixelColor);
		aux.add(pnButtons);
		pnInfo.add(aux);
		pack();
		//setSize(getWidth() * 2, getHeight());
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new FocusChangeListener());

	}

	class FocusChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			//Component oldComp = (Component) evt.getOldValue();
			Component newComp = (Component) evt.getNewValue();

			if(!(newComp instanceof ImageFrame))
				return;
			else if(newComp instanceof ImageFrame)
				focusedFrame = (ImageFrame) newComp;
				
			/*
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
			}*/
		}
	}

}