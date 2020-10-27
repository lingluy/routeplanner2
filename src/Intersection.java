
public class Intersection {
	private String name;
//	private String id;
	private double x;
	private double y;
	
	Intersection(String name, double x, double y){
		this.name=name;
		//this.id =id;
		this.x =x;
		this.y=y;
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	

}
