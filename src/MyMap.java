import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

/*
 * This class is going to draw map using input data file
 * 
 * 
 */
public class MyMap extends JFrame {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, Intersection> intersections = new HashMap<String, Intersection>();
	private HashMap<String, Road> roads = new HashMap<String, Road>();
	double minLantitude = 0;
	double maxLantitude = 0;
	double minlongitude = 0;
	double maxlongitude = 0;

	MyMap(String fileName, StreetMap g,int jFrameWidth, int jFrameHeight, ArrayList<String> roadsUsed){
		initUI(fileName, g, jFrameWidth, jFrameHeight,roadsUsed);
		pack();
		setResizable(true);
		setVisible(true);

	}
	private void initUI(String fileName, StreetMap g,int jFrameWidth, int jFrameHeight, ArrayList<String> path) {
		String[] str = fileName.split(Pattern.quote(File.separator));
		String title = str[str.length-1];
	 	setSize(jFrameWidth, jFrameHeight);
		roads=makeRoads(fileName, getWidth(), getHeight());
		ArrayList<String> roadsUsed=convertToRoad(g,path);
		final MyPanel comp = new MyPanel(roads, roadsUsed);
	
		comp.setPreferredSize(new Dimension(getWidth(), getHeight()));
		this.getContentPane().add(comp, BorderLayout.CENTER);
		setTitle(fileName);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		    	roads=makeRoads(fileName, getWidth(), getHeight());
		    	getContentPane().removeAll();
		    	final MyPanel comp = new MyPanel(roads, roadsUsed);
				//comp.setPreferredSize(new Dimension(jFrameWidth, jFrameHeight));
				comp.setPreferredSize(new Dimension(getWidth(), getHeight()));
				getContentPane().add(comp, BorderLayout.CENTER);
		    	getContentPane().validate();
		 	    getContentPane().repaint();
		 	    
		    }
		});
		
	}

	
	
	/*
	 * This method creates two HashMaps: one holds interactions, another hold roads
	 * use googleMapProjection class to convert lantitude and logitutude to x and y
	 */
	private HashMap<String, Road> makeRoads(String fileName, int width, int height) {
		
		StringBuilder sb = new StringBuilder();
		//this object is use to convert lantitude and logititude to x and y
		CoordinateConverter converter = new CoordinateConverter();

		int zoom = 15;
		// figure out min and max in dataset to calucate scalingfactor
		
		boolean firstTime = true;
		

		try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {

			// read line by line
			String line;
			while ((line = br.readLine()) != null) {

				String[] array = line.split("\\s");
				if (array[0].equals("i")) // intersection
				{
					if(firstTime)
					{  
						saveIntersections(array,  true, converter,  zoom);
					    firstTime=false;
				    }
				    else 
				    {	saveIntersections(array,  false, converter,  zoom);
				    } 
			  }
			   else if (array[0].equals("r")) { // assume intersections are all created before roads
					saveRoads(array,width, height);
				}
			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

		// printIntersections(intersections);
		// printRoads(roads);
		
		return roads;
	}

	/*
	 * This method creates two HashMaps: one holds interactions, another hold roads
	 * use googleMapProjection class to convert lantitude and logitutude to x and y
	 */
	

	private  void saveIntersections(String[] array, boolean firstTime, CoordinateConverter converter, int zoom) {
		double lan = Double.parseDouble(array[2]);
		double longitude = Double.parseDouble(array[3]);

		PointXY point = converter.fromLatLngToPoint(lan, longitude, zoom);
		Intersection i = new Intersection(array[1], point.x, point.y);
		// caluculate max and min lantitude and logitutude. This is used to create
		// scalling factor

		if (firstTime) {
			minLantitude = point.x;
			maxLantitude = point.x;
			minlongitude = point.y;
			maxlongitude = point.y;

			firstTime = false;
		} else {
			if (point.x > maxLantitude)
				maxLantitude = point.x;
			else if (point.x < minLantitude)
				minLantitude = point.x;
			if (point.y > maxlongitude)
				maxlongitude = point.y;
			else if (longitude < minlongitude)
				minlongitude = point.y;
		}

		intersections.put(array[1], i);
	}
	
   private void saveRoads(String[] array, int jFrameWidth, int jFrameHeight) {
	// printIntersections(intersections);
	   int width=jFrameWidth;
	   int height=jFrameHeight;
		
		double lanScalingFactor=getLanScalingFactor(width, maxLantitude, minLantitude);
		double longScalingFactor=getLongScalingFactor(height,maxlongitude,minlongitude);
		double scalingFactor =  Math.min(lanScalingFactor, longScalingFactor);
		 // now we need to readjust the padding to ensure the map is always drawn on the center of the given image dimension
       double heightPadding = (height - (scalingFactor* maxlongitude)) / 2;
       double widthPadding = (width - (scalingFactor* maxLantitude)) / 2;

		String name = array[1];
		Intersection i = (Intersection) intersections.get(array[2]);
		Intersection j = (Intersection) intersections.get(array[3]);
		double x1 =0;
		double x2=0;
		double y1=0;
		double y2=0;
		if(lanScalingFactor>longScalingFactor)
		{
			x1 = (i.getX()- minLantitude) * scalingFactor;
	        y1 =(i.getY()-minlongitude) * scalingFactor;
	        x2 = (j.getX()- minLantitude) * scalingFactor;
	        y2 =(j.getY()-minlongitude) * scalingFactor;
		}
		else {
			
	        
	        x1 = (i.getX()- minLantitude) * scalingFactor;
	        y1 =(i.getY()-minlongitude) * scalingFactor+height/2;
	        x2 = (j.getX()- minLantitude) * scalingFactor;
	        y2 =(j.getY()-minlongitude) * scalingFactor+height/2;
		}
		
		roads.put(name, new Road(name, x1, y1, x2, y2));
	
   }
	
   static ArrayList<String> convertToRoad(StreetMap g, ArrayList<String> path) {
	   
	   ArrayList<String> roadNames = new ArrayList<String>();
	   for(int i=0; i<path.size()-1; ) {
		   Node start = g.map.get(path.get(i));
		   i++;
		   Node end = g.map.get(path.get(i));
		  
		   for (int j = 0; j < start.adjacencyList.size(); j++) {
				Edge edge = start.adjacencyList.get(j);
				Node otherNode = edge.otherNode(start);
				if (otherNode.name.equals(end.name))
				{
					roadNames.add(edge.name);
					break;
				}
		   }
		   
				
		   
	   }
	    return roadNames;
	   
   }
	static double getLanScalingFactor(int width, double maxLantitude, double minLantitude) {
		return width / (maxLantitude - minLantitude);
		
	}
	static double getLongScalingFactor(int height, double maxlongitude, double minlongitude) {
		return height / (maxlongitude - minlongitude);
		//
	}
	
	//utility method for validation
	static private void printIntersections(HashMap i) {
		Iterator<?> it = i.entrySet().iterator();
		 while (it.hasNext()) {
		
			Map.Entry pair = (Map.Entry) it.next();
			// System.out.println(pair.getKey() + " = " + pair.getValue());
			Intersection r = (Intersection) pair.getValue();
			it.remove(); // avoids a ConcurrentModificationException
			int x1 = (int) r.getX();
			int y1 = (int) r.getY();

			System.out.println(r.getName() + " " + x1 + " " + y1);

		}

	}
	//utility method for validation
	static private void printRoads(HashMap roads) {
		Iterator it = roads.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();
			// System.out.println(pair.getKey() + " = " + pair.getValue());
			Road r = (Road) pair.getValue();
			// it.remove(); // avoids a ConcurrentModificationException
			int x1 = (int) r.getX1();
			int y1 = (int) r.getY1();
			int x2 = (int) r.getX2();
			int y2 = (int) r.getY2();
			System.out.println(r.getName() + " " + x1 + " " + y1 + " " + x2 + " " + y2);

		}

	}
}
