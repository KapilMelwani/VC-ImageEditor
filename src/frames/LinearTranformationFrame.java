package frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import panels.InfoPanel;
import panels.LinearTransformationPanel;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class LinearTranformationFrame extends Frame {
	
	private LinearTransformationPanel panel;
	private InfoPanel info;
	private JButton btnCreate;
	
	public LinearTranformationFrame(ImageFrame parent) {
		super(parent);
		info = new InfoPanel();
		setTitle("Linear Tranformation: " + parent.getTitle());
		setPanel(new LinearTransformationPanel());
		getPanel().getNodes().addObserver(info);
		btnCreate = new JButton("Transform Image");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				BufferedImage image = getParentFrame().image.linearTransform(ImageUtils.returnSegments(panel.getNodes().getList()));
				ImageUtils.createNewImageFrame(image, parent);
				((JFrame) SwingUtilities.getRoot((Component) e.getSource())).dispose();
			}
		});
		setParent(parent);
		getContentPane().add(getPanel(), BorderLayout.CENTER);
		JPanel aux = new JPanel(new BorderLayout());
		aux.add(info, BorderLayout.CENTER);
		aux.add(btnCreate, BorderLayout.SOUTH);
		getContentPane().add(aux, BorderLayout.SOUTH);
		pack();
	}

	public LinearTransformationPanel getPanel() {
		return panel;
	}

	public void setPanel(LinearTransformationPanel panel) {
		this.panel = panel;
	}

	public InfoPanel getInfo() {
		return info;
	}

	public void setInfo(InfoPanel info) {
		this.info = info;
	}
	
}
