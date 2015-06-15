import java.util.List;

import engine.*;

public class Run {
	public static void main(String[] args) {
		Game game = new Game();
	}
	public static Vertex getVertexAt(int x, int y, List<Vertex> vertexes) {
		for (int i = 0; i < vertexes.size(); i++) {
			if (vertexes.get(i).getX() == x &&
					vertexes.get(i).getY() == y) {
				return vertexes.get(i);
			}
		}
		return null;
	}
}
