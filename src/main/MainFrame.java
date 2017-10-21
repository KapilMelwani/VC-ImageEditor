package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import main.ImageFrame.ImagePanel;
import panels.PixelColorPanel;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;

public class MainFrame extends JFrame {
	
	// JMENU
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpen, mntmSave, mntmExit;
	
	// OTHERS
	private JPanel contentPane;
	private JLabel lbCursorInfo;
	private PixelColorPanel pnMousePixelColor;
	private JMenuItem mntmSave_1;
	
	

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
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and BMP images", "png", "bmp", "jpg");
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
		
		mntmSave_1 = new JMenuItem("Save...");
		mnFile.add(mntmSave_1);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.setSelectedIcon(new ImageIcon(MainFrame.class.getResource("/javax/swing/plaf/metal/icons/ocean/close-pressed.gif")));
		mnFile.add(mntmExit);
		
		
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
		btnImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					System.out.println(selectedFile.getAbsolutePath());
					createNewImageFrame(selectedFile.getAbsolutePath());
				}

			}
		});
		JPanel aux = new JPanel(new GridLayout(1, 2));
		pnMousePixelColor = new PixelColorPanel();
		aux.add(pnMousePixelColor);
		aux.add(btnImage);
		pnInfo.add(aux);
		pack();
		setSize(getWidth() * 3, getHeight());
	}

	private void createNewImageFrame(String file) {
		ImageFrame frame = new ImageFrame(file);
		frame.getPanel().addMouseMotionListener(new MousePixelListener(lbCursorInfo, pnMousePixelColor));
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
			label.setText(	"x = " + x +
					", y = " + y +
					", [" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + "] " +
					String.format("(#%02x%02x%02x)", color.getRed(), color.getGreen(), color.getBlue()));
		}
	}


}
