package engine;

public class Edge {

	private String id;
	private Vertex source;
	private Vertex destination;
	public static final int WEIGHT = 1;
	
	public Edge(Vertex source, Vertex destination) {
		setId(source, destination);
		setSource(source);
		setDestination(destination);
	}

	public String getId() {
		return id;
	}

	public void setId(Vertex source, Vertex destination) {
		this.id ="(" + source.getX() + ", " + source.getY() + ")"
				+ " -> (" + destination.getX() + ", " + destination.getY() + ")";
	}

	public Vertex getSource() {
		return source;
	}

	public void setSource(Vertex source) {
		this.source = source;
	}

	public Vertex getDestination() {
		return destination;
	}

	public void setDestination(Vertex destination) {
		this.destination = destination;
	}

}
