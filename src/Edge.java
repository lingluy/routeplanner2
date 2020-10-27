
public class Edge {
	String name;
	Node node1, node2;
	double weight;
	
	Edge (String name, Node node1, Node node2) {
		this.name=name;
		this.node1 = node1;
		this.node2 = node2;
		//double h = Math.pow(Math.sin((node2.latitude - node1.latitude)/2), 2);
		//double cos = Math.cos(node1.latitude) * Math.cos(node2.latitude);
		//double sinLong = Math.pow(Math.sin((node2.longitude - node1.longitude)/2), 2);
		//weight = 2 * 6356 * Math.asin(Math.sqrt(h + cos * sinLong));
		Haversine haversine = new Haversine();
		weight = haversine.distance(node1.latitude, node1.longitude, node2.latitude, node2.longitude);
	}
	
	public static void main(String[] args) {
		Edge ed = new Edge("lol", new Node("node1", 3, 0), new Node("node2", 0, 4));
		System.out.println(ed.weight);
	}
	
	public Node otherNode (Node node) {
		if (node == node1) {
			return node2;
		}
		else {
			return node1;
		}
	}
}
