import java.util.ArrayList;

public class Node {
	String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	ArrayList<Edge> adjacencyList = new ArrayList<>();
	double distance = 9999999;
	boolean visited = false;
	double latitude, longitude;	
	public Node previousNode=null;
	Node (String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}		
}
