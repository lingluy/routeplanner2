
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MyPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MyPanel() {

	}

	private HashMap<String, Road> roads;
	private  ArrayList<String>  roadsUsed;

	public MyPanel(HashMap<String, Road> roads, ArrayList<String> roadsUsed) {

		super();
		this.roads = roads;
		this.roadsUsed = roadsUsed;

	}

	void drawLines(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Iterator it = roads.entrySet().iterator();

		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();
			// System.out.println(pair.getKey() + " = " + pair.getValue());
			Road r = (Road) pair.getValue();
			it.remove(); // avoids a ConcurrentModificationException
			int x1 = (int) r.getX1();
			int y1 = (int) r.getY1();
			int x2 = (int) r.getX2();
			int y2 = (int) r.getY2();

            for( String roadName : roadsUsed) {
            	
            	if(r.getName().equalsIgnoreCase(roadName))
            	{  
            		g2d.setStroke(new BasicStroke(3));
                     g2d.setColor(Color.YELLOW);
            	}
            
			
		//	g2d.drawLine(x1, y1, x2, y2);
		// 	g2d.setColor(Color.BLACK);
            }
          
            g2d.drawLine(x1, y1, x2, y2);
            g2d.setStroke(new BasicStroke(1));
			g2d.setColor(Color.BLACK);
		}
		// g2d.drawLine(24, 45, 100, 102);
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		drawLines(g);

	}

}
