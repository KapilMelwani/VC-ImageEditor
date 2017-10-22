package panels;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.FunctionSegment;

public class LTInfoPanel extends JPanel {
	
	private List<FunctionSegment> segments;

	public LTInfoPanel(List<FunctionSegment> segments) {
		this.segments = segments;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.removeAll();
		setLayout(new GridLayout(segments.size()-1, 1));
		for(FunctionSegment segment : segments)
			add(new JLabel(segment.toString()));
	}

}
