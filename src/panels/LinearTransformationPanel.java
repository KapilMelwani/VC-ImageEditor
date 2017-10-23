package panels;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import main.FunctionSegment;
import main.LinearTranformationFrame;


public class LinearTransformationPanel extends JPanel {

	private static final int MAX_SCORE = 256;
	private static final int PREF_W = 532;
	private static final int PREF_H = 532;
	private static final int BORDER_GAP = 30;
	private static final Color GRAPH_COLOR = Color.BLUE;
	private static final Color GRAPH_POINT_COLOR = Color.GREEN;
	private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
	private static final int GRAPH_POINT_WIDTH = 12;
	private static final int Y_HATCH_CNT = 10;
	private List<Node> nodes;
	private boolean drag;
	private Node dragNode;
	
	private JPanel info;

	public LinearTransformationPanel() {
		this.nodes = new ArrayList<Node>();
		Node start = new Node(0, 0, false, false, false);
		Node end = new Node(255, 255, false, false, false);
		nodes.add(start);
		nodes.add(end);
		drag = false;
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				Point p = magnetCoordinates(me.getX(), me.getY());
				if(!isWithinBounds(p))
					return;
				
				Node node = getNode(p.x, p.y);
				if(node != null && node.isMoveable()) {
					drag = true;
					dragNode = node;
				}
				repaint();
			}
			public void mouseClicked(MouseEvent me) {
				Point p = magnetCoordinates(me.getX(), me.getY());
				if(!isWithinBounds(p))
					return;
				
				Node node = getNode(p.x, p.y);
				if(SwingUtilities.isRightMouseButton(me) && node != null && node.isDeleteable())
					deleteNode(node);
				else if(SwingUtilities.isRightMouseButton(me))
					return;
				else if (node == null)
					addNode(p.x, p.y);
				repaint();
			}
			public void mouseReleased(MouseEvent me) {
				drag = false;
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {
				Point p = magnetCoordinates(me.getX(), me.getY());
				if(!isWithinBounds(p))
					return;
				if(drag)
					moveNode(dragNode, p.x, p.y);
				repaint();
			}
		});
		info = new JPanel();
		info.setLayout(new GridLayout(nodes.size()-1, 1));
		for(FunctionSegment segment : returnSegments()) {
			info.add(new JLabel(segment.toString()), BorderLayout.SOUTH);
			info.repaint();
		}
		add(info, BorderLayout.SOUTH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// create x and y axes
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

		// create hatch marks for y axis.
		for (int i = 0; i < Y_HATCH_CNT; i++) {
			int x0 = BORDER_GAP;
			int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
			int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
			int y1 = y0;
			g2.drawLine(x0, y0, x1, y1);
		}

		// and for x axis
		for (int i = 0; i < nodes.size() - 1; i++) {
			int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (nodes.size() - 1) + BORDER_GAP;
			int x1 = x0;
			int y0 = getHeight() - BORDER_GAP;
			int y1 = y0 - GRAPH_POINT_WIDTH;
			g2.drawLine(x0, y0, x1, y1);
		}

		if (nodes.isEmpty())
			return;

		double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
		double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

		List<Point> graphPoints = new ArrayList<Point>();
		for (Node node : nodes) {
			int x1 = (int) (node.getX() * xScale + BORDER_GAP);
			int y1 = (int) ((MAX_SCORE - node.getY()) * yScale + BORDER_GAP);
			graphPoints.add(new Point(x1, y1));
		}
		graphPoints.sort(new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return 		o1.x < o2.x ? -1
				       : 	o1.x > o2.x ? 1
				       : 	0;
			}
		});
		
		Stroke oldStroke = g2.getStroke();
		g2.setColor(GRAPH_COLOR);
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < graphPoints.size() - 1; i++) {
			int x1 = graphPoints.get(i).x;
			int y1 = graphPoints.get(i).y;
			int x2 = graphPoints.get(i + 1).x;
			int y2 = graphPoints.get(i + 1).y;
			g2.drawLine(x1, y1, x2, y2);
		}

		g2.setStroke(oldStroke);
		g2.setColor(GRAPH_POINT_COLOR);
		for (int i = 0; i < graphPoints.size(); i++) {
			int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
			int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);
		}
		info.removeAll();
		info.setLayout(new GridLayout(nodes.size()-1, 1));
		for(FunctionSegment segment : returnSegments()) {
			System.out.println(segment);
			JPanel aux = new JPanel(new GridLayout(1,2));
			JLabel lb1 = new JLabel(segment.stringFunction());
			JLabel lb2 = new JLabel(segment.stringPoints());
			lb1.setHorizontalAlignment(SwingConstants.CENTER);
			lb2.setHorizontalAlignment(SwingConstants.CENTER);
			aux.add(lb1);
			aux.add(lb2);
			info.add(aux);
			info.repaint();
		}
		((JFrame) SwingUtilities.getWindowAncestor(this)).pack();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}
	
	public boolean isWithinBounds(Point p) {
		return isWithinBounds(p.x, p.y);
	}
	
	public boolean isWithinBounds(int x, int y) {
		if(x >= MAX_SCORE || y >= MAX_SCORE || x < 0 || y < 0)
			return false;
		return true;
	}

	public Point translateCoordinatesDoubleToInt(int x, int y) {
		double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
		double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
		int xF = (int) ((x - BORDER_GAP) / xScale);
		int yF = (int) (MAX_SCORE - ((y - BORDER_GAP) / yScale));
		return new Point(xF, yF);
	}
	
	public Point magnetCoordinates(int x, int y) {
		double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
		double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
		double xF = (x - BORDER_GAP) / xScale;
		double yF = MAX_SCORE - ((y - BORDER_GAP) / yScale);
		int i = (int)(xF);
		int j = (int)(yF);
		if((xF - (int)(xF)) > 0.5)
			i++;
		if((yF - (int)(yF)) > 0.5)
			j++;
		return new Point(i, j);
	}
	
	public Point translateCoordinatesIntToDouble(int x, int y) {
		double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
		double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
		int xF = (int) (x * xScale + BORDER_GAP);
		int yF = (int) ((MAX_SCORE - y) * yScale + BORDER_GAP);
		return new Point(xF, yF);
	}

	public void addNode(int x, int y) {
		nodes.add(new Node(x, y));
		Collections.sort(nodes);
	}

	public void moveNode(Node node, int x, int y) {
		if(!node.isMoveable() ||x >= MAX_SCORE || y >= MAX_SCORE || x < 0 || y < 0)
			return;
		node.setCoordinates(x, y);
		Collections.sort(nodes);
	}
	
	public void deleteNode(Node node) {
		nodes.remove(node);
	}

	private Node getNode(int x, int y) {
		for (Node node : nodes) {
			double dist = Math.hypot((node.getX() - x), (node.getY() - y));
			if (dist <= GRAPH_POINT_WIDTH / 2)
				return node;
		}
		return null;
	}
	
	public List<FunctionSegment> returnSegments() {
		List<FunctionSegment> list = new ArrayList<FunctionSegment>();
		//System.out.println(nodes);
		for(int i = 1; i < nodes.size(); i++) {
			Node node1 = nodes.get(i-1);
			Node node2 = nodes.get(i);
			//System.out.println("Node1 = " + node1);
			//System.out.println("Node2 = " + node2);
			list.add(new FunctionSegment(node1.getCoordinates(), node2.getCoordinates()));
		}
		//System.out.println("FIN");
		return list;
	}
	
	public List<Node> getNodes() { 
		return this.nodes;
	}

	private class Node implements Comparable<Object> {
		private int x, y;
		private boolean isSelected, isMoveable, isDeleteable;

		public Node(int x, int y, boolean selected, boolean moveable, boolean deleteable) {
			setCoordinates(x, y);
			setSelected(selected);
			setMoveable(moveable);
			setDeleteable(deleteable);
		}

		public Node(int x, int y) {
			this(x, y, true, true, true);
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public boolean isMoveable() {
			return isMoveable;
		}

		public void setMoveable(boolean isMoveable) {
			this.isMoveable = isMoveable;
		}

		public boolean isDeleteable() {
			return isDeleteable;
		}

		public void setDeleteable(boolean isDeleteable) {
			this.isDeleteable = isDeleteable;
		}

		public void setCoordinates(int x, int y) {
			setX(x);
			setY(y);
		}
		
		public Point getCoordinates() {
			return new Point(getX(), getY());
		}
		
		public String toString() {
			return "Node (" + this.hashCode() + ") = (" + getX() + ", " + getY() + ")";
		}

		@Override
		public int compareTo(Object arg0) {
			return getX();
		}

	}

}