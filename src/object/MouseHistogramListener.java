package object;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JLabel;

import panels.HistogramPanel;

public class MouseHistogramListener implements MouseMotionListener {
	
	private JLabel lbValue, lbCount;
	
	public MouseHistogramListener(JLabel value, JLabel count) {
		setLbValue(value);
		setLbCount(count);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int x = arg0.getX();
		List<HistogramLayer> aux = ((HistogramPanel)arg0.getComponent()).getLayers();
		int[] values = aux.get(aux.size()-1).getValues();
		if(x > 5 && x < (5+256*4)-1) {
			int value = (x / 4)-1;
			getLbValue().setText(String.valueOf(value));
			getLbCount().setText(String.valueOf(values[value]));
		}
		
	}

	public JLabel getLbValue() {
		return lbValue;
	}

	public void setLbValue(JLabel lbValue) {
		this.lbValue = lbValue;
	}

	public JLabel getLbCount() {
		return lbCount;
	}

	public void setLbCount(JLabel lbCount) {
		this.lbCount = lbCount;
	}

}
