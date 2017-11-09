package frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

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
				getParentFrame().image.linearTransform(ImageUtils.returnSegments(panel.getNodes().getList()));
				getParentFrame().getPanel().repaint();
				//((JFrame) SwingUtilities.getRoot((Component) e.getSource())).dispose();
			}
		});
		setParent(parent);
		getContentPane().add(getPanel(), BorderLayout.CENTER);
		JPanel aux = new JPanel(new BorderLayout());
		aux.add(info, BorderLayout.CENTER);
		aux.add(btnCreate, BorderLayout.SOUTH);
		getContentPane().add(aux, BorderLayout.SOUTH);
		pack();
		
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    		getParentFrame().image.setMiddleCopy();
		    }
		});
	}

	public LinearTransformationPanel getPanel() {
		return panel;
	}

	public InfoPanel getInfo() {
		return info;
	}

	public void setPanel(LinearTransformationPanel panel) {
		this.panel = panel;
	}

	public void setInfo(InfoPanel info) {
		this.info = info;
	}

}
