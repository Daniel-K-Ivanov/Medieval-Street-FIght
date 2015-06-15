package engine;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import units.Creature;

public class Battlefield {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	public Vertex[][] battlefield = new Vertex[WIDTH][HEIGHT];
	public Battlefield() {
		for (int x = 0; x < HEIGHT; x++) {
			for (int y = 0; y < WIDTH; y++) {
				this.battlefield[x][y] = new Vertex(x,y, new ArrayList<Creature>());
			}
		}
	}
	
	public void printField(Player player, Player pc, Battlefield field) {
		PrintWriter writer;
		try {
			File file = new File("Game-Field.txt");
			writer = new PrintWriter(file, "UTF-8");
			
			writer.println("    0   1   2   3   4   5   6   7   8   9  ");
			for (int x = 0; x < WIDTH; x++) {
				writer.print(x + " |");
				for (int y = 0; y < HEIGHT; y++) {
					Vertex currentCell = field.battlefield[x][y];
					if (Game.isEmptyCell(currentCell)) {
						writer.print(" - |");
					}
					else {
						String type = Game.getType(currentCell);
						if (Player.isPlayerUnit(player, currentCell)) {
							writer.print(" " + type.charAt(0) + " |");
						}
						else {
							writer.print("E" + type.charAt(0) + " |");
						}
					}
				}
				writer.println("\n");
			}
			writer.println("P: " + player.getPeasants().size() +
				"\t\t\t\tEP:" + pc.getPeasants().size());
			writer.println("F: " + player.getFootmans().size() +
				"\t\t\t\tEF:" + pc.getFootmans().size());
			writer.println("A: " + player.getArchers().size() +
				"\t\t\t\tEA:" + pc.getArchers().size());
			writer.println("G: " + player.getGriffons().size() +
				"\t\t\t\tEG:" + pc.getGriffons().size());
			writer.close();
			Desktop desktop = Desktop.getDesktop();
	        if(file.exists()) desktop.open(file);
		} catch (FileNotFoundException e) {
			System.err.println("File not found!");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported Encoding!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO exception!");
		e.printStackTrace();
		}
	}
	
	
	
	public Graph generateGraph(Vertex currentPosition, Vertex destinationPosition) {
		List<Vertex> occupiedVertices = generateOccupiedVertices();
		List<Vertex> vertices = generateVertices(currentPosition, destinationPosition, occupiedVertices);
		List<Edge> edges = generateEdges(vertices); 
		
		return new Graph(vertices, edges);
	}

	private List<Edge> generateEdges(List<Vertex> vertices) {
		List<Edge> edges = new ArrayList<Edge>();
		for (int x = 0; x < HEIGHT; x++) {
			for (int y = 0; y < WIDTH; y++) {
				if (verticeExist(x, y, vertices)) {
					if (verticeExist(x - 1, y - 1, vertices))
						edges.add(new Edge(getVertexAt(x ,y , vertices), getVertexAt(x - 1, y - 1, vertices)));
					if (verticeExist(x - 1, y, vertices)) 
						edges.add(new Edge(getVertexAt(x ,y , vertices), getVertexAt(x - 1, y, vertices)));
					if (verticeExist(x - 1, y + 1, vertices))
						edges.add(new Edge(getVertexAt(x ,y , vertices), getVertexAt(x - 1, y + 1, vertices)));
					if (verticeExist(x, y - 1, vertices)) 
						edges.add(new Edge(getVertexAt(x ,y , vertices), getVertexAt(x, y - 1, vertices)));
					if (verticeExist(x, y + 1, vertices))
						edges.add(new Edge(getVertexAt(x ,y , vertices), getVertexAt(x, y + 1, vertices)));
					if (verticeExist(x + 1, y - 1, vertices)) 
						edges.add(new Edge(getVertexAt(x ,y , vertices), getVertexAt(x + 1, y - 1, vertices)));
					if (verticeExist(x + 1, y, vertices))
						edges.add(new Edge(getVertexAt(x ,y , vertices), getVertexAt(x + 1, y, vertices)));
					if (verticeExist(x + 1, y + 1, vertices)) 
						edges.add(new Edge(getVertexAt(x ,y , vertices), getVertexAt(x + 1, y + 1, vertices)));
				}
			}
		}
		
		return edges;
	}

	private boolean verticeExist(int x, int y, List<Vertex> vertices) {
		if (0 <= x && x < HEIGHT && 0 <= y && y < WIDTH ) {
			return vertices.contains(battlefield[x][y]);
		}
		return false;
		
	}

	private List<Vertex> generateOccupiedVertices() {
		List<Vertex> vertices = new ArrayList<Vertex>();
		for (int x = 0; x < battlefield.length; x++) {
			for (int y = 0; y < battlefield[0].length; y++) {
				if (battlefield[x][y].getUnits().size() > 0) {
					vertices.add(battlefield[x][y]);
				}
			}
		}
		return vertices;
	}
	
	private List<Vertex> generateVertices(Vertex currentPosition, Vertex destinationPosition, List<Vertex> occupiedVertices) {
		List<Vertex> vertices = new ArrayList<Vertex>();
		for (int x = 0; x < battlefield.length; x++) {
			for (int y = 0; y < battlefield[0].length; y++) {
				if (currentPosition.equals(battlefield[x][y])) {
					vertices.add(currentPosition); 
				}
				else if (destinationPosition.equals(battlefield[x][y])) {
					vertices.add(destinationPosition); 
				}
				else if (!occupiedVertices.contains(battlefield[x][y])) {
					vertices.add(battlefield[x][y]); 
				}
			}
		}
		return vertices;
	}
	
	private Vertex getVertexAt(int x, int y, List<Vertex> vertexes) {
		for (int i = 0; i < vertexes.size(); i++) {
			if (vertexes.get(i).getX() == x &&
					vertexes.get(i).getY() == y) {
				return vertexes.get(i);
			}
		}
		return null;
	}
}
