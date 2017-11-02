package panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import main.Frame;
import main.ImageFrame;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class PropertiesFrame extends Frame {

	private JLabel categFormat, categResolution, categRange, categBrightness, categContrast, categDynRange;

	public PropertiesFrame(Frame parent) {
		super(parent);
		setLayout(new GridLayout(6, 1));
		
		BufferedImage image = ((ImageFrame)parent).getPanel().getImage();
		ImageFrame frame = (ImageFrame)parent;
		
		categFormat = new JLabel(frame.getFormat());
		categFormat.setBorder(BorderFactory.createTitledBorder("Format"));
		categFormat.setHorizontalAlignment(SwingConstants.CENTER);
		categFormat.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categFormat);

		categResolution = new JLabel(frame.getResolution());
		categResolution.setBorder(BorderFactory.createTitledBorder("Resolution"));
		categResolution.setHorizontalAlignment(SwingConstants.CENTER);
		categResolution.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categResolution);

		Point range = ImageUtils.getGrayRange(image);
		categRange = new JLabel("[" + range.x + " - " + range.y + "]");
		categRange.setBorder(BorderFactory.createTitledBorder("Value Range"));
		categRange.setHorizontalAlignment(SwingConstants.CENTER);
		categRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categRange);
		
		categBrightness = new JLabel(String.valueOf(ImageUtils.brightness(image)));
		categBrightness.setBorder(BorderFactory.createTitledBorder("Brightness"));
		categBrightness.setHorizontalAlignment(SwingConstants.CENTER);
		categBrightness.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categBrightness);

		categContrast = new JLabel(String.valueOf(ImageUtils.contrast(image)));
		categContrast.setBorder(BorderFactory.createTitledBorder("Contrast"));
		categContrast.setHorizontalAlignment(SwingConstants.CENTER);
		categContrast.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categContrast);
		
		categDynRange = new JLabel(String.valueOf(ImageUtils.dynamicRange(image)));
		categDynRange.setBorder(BorderFactory.createTitledBorder("Dynamic Range"));
		categDynRange.setHorizontalAlignment(SwingConstants.CENTER);
		categDynRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categDynRange);
		// TODO Auto-generated constructor stub
		pack();
	}

}
