package panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import frames.Frame;
import frames.Image;
import frames.ImageFrame;

@SuppressWarnings("serial")
public class PropertiesFrame extends Frame {

	private JLabel categFormat, categResolution, categRange, categBrightness, categContrast, categDynRange, categEntropy;

	public PropertiesFrame(ImageFrame parent) {
		super(parent);
		setLayout(new GridLayout(7, 1));
		
		//BufferedImage image = parent.image.get();
		Image image = getParentFrame().image;
		
		categFormat = new JLabel(parent.image.getFormat());
		categFormat.setBorder(BorderFactory.createTitledBorder("Format"));
		categFormat.setHorizontalAlignment(SwingConstants.CENTER);
		categFormat.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categFormat);

		categResolution = new JLabel(parent.image.getResolution());
		categResolution.setBorder(BorderFactory.createTitledBorder("Resolution"));
		categResolution.setHorizontalAlignment(SwingConstants.CENTER);
		categResolution.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categResolution);

		Point range = image.grayRange();
		categRange = new JLabel("[" + range.x + " - " + range.y + "]");
		categRange.setBorder(BorderFactory.createTitledBorder("Value Range"));
		categRange.setHorizontalAlignment(SwingConstants.CENTER);
		categRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categRange);
		
		categBrightness = new JLabel(String.valueOf(image.brightness()));
		categBrightness.setBorder(BorderFactory.createTitledBorder("Brightness"));
		categBrightness.setHorizontalAlignment(SwingConstants.CENTER);
		categBrightness.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categBrightness);

		categContrast = new JLabel(String.valueOf(image.contrast()));
		categContrast.setBorder(BorderFactory.createTitledBorder("Contrast"));
		categContrast.setHorizontalAlignment(SwingConstants.CENTER);
		categContrast.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categContrast);
		
		categDynRange = new JLabel(String.valueOf(image.dynamicRange()));
		categDynRange.setBorder(BorderFactory.createTitledBorder("Dynamic Range"));
		categDynRange.setHorizontalAlignment(SwingConstants.CENTER);
		categDynRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categDynRange);
		
		categEntropy = new JLabel(String.valueOf(image.shannonEntropy()));
		categEntropy.setBorder(BorderFactory.createTitledBorder("Entropy"));
		categEntropy.setHorizontalAlignment(SwingConstants.CENTER);
		categEntropy.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categEntropy);
		
		pack();
	}

}
