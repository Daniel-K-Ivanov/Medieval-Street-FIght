package engine;

import java.util.ArrayList;
import java.util.List;

import units.Creature;

public class Vertex {

	private int x;
	private int y;
	private List<Creature> units = new ArrayList<Creature>();

	public Vertex(int x, int y,List<Creature> list) {
		this.setX(x);
		this.setY(y);
		this.setUnits(list);
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
	
	public List<Creature> getUnits() {
		return units;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setUnits(List units) {
		this.units = units;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (this.getClass() != obj.getClass()) { return false; }
		
		Vertex other = (Vertex) obj;
		if (this.getX() == other.getX() &&
				this.getY() == other.getY()) {
			return true;
		}
		
		return false;
	}
}
