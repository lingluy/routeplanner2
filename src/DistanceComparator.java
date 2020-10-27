import java.util.Comparator;

public class DistanceComparator implements Comparator<Node> {
	public int compare(Node node1, Node node2) {
		if (node1.distance < node2.distance) {
			return -1;
		}
		else if (node1.distance == node2.distance) {
			return 0;
		}
		else {
			return 1;
		}
		
	}

}
