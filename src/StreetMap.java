import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;
/*
 * 
 * 
 * @author Kevin Yang
 * @version 3.4
 * @since 2019-05-02
 */
public class StreetMap {		
	HashMap<String, Node> map = new HashMap<>();
	
	public static void main(String[] args) {
		/* 
		 * The following code uses the File and Scanner classes to read the input file. Each intersection is placed
		 * in a map with its name as its key. Each road is placed in the adjacency lists of both of its connected
		 * vertices.
		 */
		StreetMap graph = new StreetMap();
		
		File file = new File(args[0]);
		Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String[] line = sc.nextLine().split("\t");
				if (line[0].equals("i")) {
					Node node = new Node(line[1], Double.parseDouble(line[2]), Double.parseDouble(line[3]));
					graph.map.put(line[1], node);
				}
				if (line[0].equals("r")) {
					Node node0 = graph.map.get(line[2]);
					Node node1 = graph.map.get(line[3]);
					Edge edge = new Edge(line[1], node0, node1);
					node0.adjacencyList.add(edge);
					node1.adjacencyList.add(edge);
				}
			}
		} catch (Exception E) {
			E.printStackTrace();
		}
		
		/*
		 * The following while loop reads the command, detects --directions, and executes the appropriate algorithm.
		 * It runs runs shortestPath, which uses Dijkstra's algorithm to find out the shortest path and prints it. 
		 * If there is no shortest path, it says so.
		 */
		int i = 1;
		String startIntersection = "";
		String endIntersection = "";
		ArrayList<String> path = new ArrayList<String>();
		while(i < args.length)
		{
		    if(args[i].startsWith("--directions")) {
		    	startIntersection = args[++i];
		    	endIntersection=args[++i];
		    	Node startNode=graph.map.get(startIntersection);
		    	Node endNode= graph.map.get(endIntersection);
		    	path = shortestPath(graph, startNode, endNode);
		    	String sp = "";
		    	if(path.size() == 1) { //no road connected two intersections
		    		System.out.println("There are no roads connecting " + startNode.name + " to " + endNode.name);
		    		break;
		    	}
		    	System.out.printf("Shortest route found from %s to %s \n\n", startNode.name, endNode.name);
		    	for(int j = path.size()-1; j >= 0; j--) {
		    		if(j > 0) {
		    			sp += path.get(j) + " --> ";
		    		}
		    		else {
		    			sp += path.get(j);
		    		}
		    	}
		    	sp += " \nThe distance between the two intersections is " + endNode.distance + " kilometers";
		    	System.out.print(sp);
		    	break;
		    }
			i++;
		}
		/*
		 * The following code is used to display the map using Java graphics. The shortest path is highlighted.
		 */
		i = 1;
		while(i < args.length) {
		    if(args[i].startsWith("--show")) {
	    	    drawMap(args[0], graph, path);
		    }
		    i++;
		}
	}
	
	/*
	 * The shortest path method is separated from the Dijkstra method because Dijkstra's algorithm finds the shortest paths to
	 * all nodes from a single source node, but we only want one specific path given two nodes. We first run Dijkstra's algorithm
	 * on the source node. Then we compile and return an arraylist of the names of all the intersections by reading backwards the
	 * tree, starting from the destination node.
	 */
	static ArrayList<String> shortestPath(StreetMap graph, Node source, Node destination) {
		source.distance = 0;
		PriorityQueue<Node> distQueue = new PriorityQueue<>(100, new DistanceComparator()); //this priority queue stores all unvisited nodes that are adjacent to visited nodes, with priority to nodes with the smallest distance
		Dijsktra(source, distQueue);
		while (distQueue.size() != 0) {
			Node nextNode = distQueue.poll();
			Dijsktra(nextNode, distQueue);
			nextNode = null;
		}
		ArrayList<String> names = new ArrayList<>();
		Node n = destination;
		names.add(n.name);
		while(n.previousNode != null) {
			names.add(n.previousNode.name);
			if(n.previousNode.name.equals(source.name)) {
				break;
			}
			n = n.previousNode;
		}
		return names;
	}
	
	/*
	 * Note that to handle larger maps with thousands of intersections, I limit the size of the priority queue to 100.
	 */
	static void Dijsktra (Node source, PriorityQueue<Node> distQueue) {
		if(distQueue.size() > 100) {
			return;
		}
		Edge edge = null;
		Node otherNode = null;
		source.visited = true;
		for (int i = 0; i < source.adjacencyList.size(); i++) {
			edge = source.adjacencyList.get(i);
			otherNode = edge.otherNode(source);
			if (!otherNode.visited) {
				if (source.distance + edge.weight < otherNode.distance) {
					otherNode.distance = source.distance + edge.weight;
					otherNode.previousNode = source;
				}
				distQueue.add(otherNode); //adds non-visited nodes to priority queue by distance
			}
		}
		return;
	}
	
	/*
	 * My drawMap method uses Java graphics to draw the map. It also takes in an arrayList, which is a list of roads.
	 * That way, it can print the shortest path.
	 */
	static void drawMap(String fileName, StreetMap g, ArrayList<String> names) {
		MyMap ex = new MyMap(fileName, g, 800, 800, names);
	}
}
