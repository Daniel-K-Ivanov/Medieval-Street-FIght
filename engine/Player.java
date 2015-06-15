package engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import engine.Battlefield;
import units.*;

public class Player {
	int gold;
	private List<Peasant> peasants;
	private boolean isPeasantUsed;
	private List<Footman> footmans;
	private boolean isFootmanUsed;
	private List<Archer> archers;
	private boolean isArcherUsed;
	private List<Griffon> griffons;
	private boolean isGriffonsUsed;
	private boolean isOnTurn;

	public Player() {
		setGold(300);
		setPeasants(new ArrayList<Peasant>());
		setFootmans(new ArrayList<Footman>());
		setArchers(new ArrayList<Archer>());
		setGriffons(new ArrayList<Griffon>());
		
		setPeasantUsed(false);
		setFootmanUsed(false);
		setArcherUsed(false);
		setGriffonsUsed(false); 
		
		setOnTurn(true);
	}
	
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}

	public List<Peasant> getPeasants() {
		return peasants;
	}

	public void setPeasants(List<Peasant> peasant) {
		this.peasants = peasant;
	}

	public List<Footman> getFootmans() {
		return footmans;
	}

	public void setFootmans(List<Footman> footmans) {
		this.footmans = footmans;
	}

	public List<Archer> getArchers() {
		return archers;
	}

	public void setArchers(List<Archer> archers) {
		this.archers = archers;
	}

	public List<Griffon> getGriffons() {
		return griffons;
	}

	public void setGriffons(List<Griffon> griffons) {
		this.griffons = griffons;
	}
	
	public boolean isPeasantUsed() {
		return isPeasantUsed;
	}

	public void setPeasantUsed(boolean isPeasantUsed) {
		this.isPeasantUsed = isPeasantUsed;
	}

	public boolean isFootmanUsed() {
		return isFootmanUsed;
	}

	public void setFootmanUsed(boolean isFootmanUsed) {
		this.isFootmanUsed = isFootmanUsed;
	}

	public boolean isArcherUsed() {
		return isArcherUsed;
	}

	public void setArcherUsed(boolean isArcherUsed) {
		this.isArcherUsed = isArcherUsed;
	}

	public boolean isGriffonsUsed() {
		return isGriffonsUsed;
	}

	public void setGriffonsUsed(boolean isGriffonsUsed) {
		this.isGriffonsUsed = isGriffonsUsed;
	}

	public boolean isOnTurn() {
		return isOnTurn;
	}

	public void setOnTurn(boolean isOnTurn) {
		this.isOnTurn = isOnTurn;
	}
	
	public void addUnits(Units units, int amount) {
		switch (units) {
		case Peasant:
			this.addPeasants(amount);
			break;
		case Footman:
			this.addFootmans(amount);
			break;
		case Archer:
			this.addArchers(amount);
			break;
		case Griffon:
			this.addGriffons(amount);
			break;
		default:
			break;
		}
	}
	
	
	public void addPeasants(int amount) {
		for (int i = 0; i < amount; i++) {
			this.peasants.add(new Peasant());
		}
	}
	
	public void addFootmans(int amount) {
		for (int i = 0; i < amount; i++) {
			this.footmans.add(new Footman());
		}
	}
	
	public void addArchers(int amount) {
		for (int i = 0; i < amount; i++) {
			this.archers.add(new Archer());
		}
	}
	
	public void addGriffons(int amount) {
		for (int i = 0; i < amount; i++) {
			this.griffons.add(new Griffon());
		}
	}

	public void printArmy() {
		System.out.println("{Peasants: " + this.peasants.size() +
				", Footmans: " + this.footmans.size() +
				", Archers: " + this.archers.size() +
				", Griffons: " + this.griffons.size() + "}");
	}

	public void play(Player player,Player pc, Battlefield field) {
		
		
		List<Vertex> pcUnits = getUnits(pc, field);
		
		for (Vertex pcUnit : pcUnits) {
			List<Vertex> playerUnits = getUnits(player, field);	
			if (playerUnits.size() == 0) {
				Game.printLost();
				break;
			}
			String typeOfUnit = pcUnit.getUnits().get(0).getClass().getSimpleName();
			LinkedList<Vertex> currPath = new LinkedList<Vertex>();
			LinkedList<Vertex> minPath = Game.generatePath(pcUnit, playerUnits.get(0), field);
		
			for (Vertex playerUnit : playerUnits) {
				currPath = Game.generatePath(pcUnit, playerUnit, field);
				if (currPath.size() <= minPath.size()) {
					if (currPath.size() == minPath.size()) {
						Vertex enemyToAttack = getWeakestEnemy(playerUnits);
						minPath = Game.generatePath(pcUnit, enemyToAttack, field);
					}
					else { minPath = currPath; }
				}
			}
			
			if (typeOfUnit.equals("Archer") || typeOfUnit.equals("Hero")) {
				Game.attack(Game.enemy, pcUnit, minPath.peekLast(), field);
				System.out.println("Enemy`s " + typeOfUnit + "s attacked (" +
				minPath.peekLast().getX() + ", " + minPath.peekLast().getY() + ")");
			}
			else {
				int unitStamina = pcUnit.getUnits().get(0).getStamina();
				int unitRange = pcUnit.getUnits().get(0).getAttackRange();
				if (minPath.size() - 1 <= unitStamina + unitRange) {
					Game.attack(Game.enemy, pcUnit, minPath.peekLast(), field);
					System.out.println("Enemy`s " + typeOfUnit + "s attacked (" +
							minPath.peekLast().getX() + ", " + minPath.peekLast().getY() + ")");
				}
				else {
					int rangeDiff = minPath.size() - 1 - pcUnit.getUnits().get(0).getStamina();
					for (int i = 0; i < rangeDiff; i++) {
						minPath.pollLast();
					}
					Game.move(pcUnit, minPath.peekLast(), field);
				}
			}			
		}
	}

	private Vertex getWeakestEnemy(List<Vertex> playerUnits) {
		Vertex weakestUnits = null;
		int minHealth = Integer.MAX_VALUE;
		for (Vertex vertex : playerUnits) {
			int currHealth = 0;
			int unitsNumber = vertex.getUnits().size();
			for (int i = 0; i < unitsNumber; i++) {
				currHealth += vertex.getUnits().get(i).getHealth();
			}
			
			if (currHealth <= minHealth) {
				minHealth = currHealth;
				weakestUnits = vertex;
			}
		}
		return weakestUnits;
	}

	public List<Vertex> getUnits(Player player, Battlefield field) {
		List<Vertex> unitsOnField = new ArrayList<Vertex>();
		for (int x = 0; x< Battlefield.HEIGHT; x++) {
			for (int y = 0; y < Battlefield.WIDTH; y++) {
				if (field.battlefield[x][y].getUnits().size() != 0) {
					unitsOnField.add(field.battlefield[x][y]);
				}
			}
		}
		
		List<Vertex> playerUnits = new ArrayList<Vertex>();
		for (Vertex vertex : unitsOnField) {
			if (isPlayerUnit(player, vertex)) {
				playerUnits.add(vertex);
			} 
		}
		return playerUnits;
	}

	public static boolean isPlayerUnit(Player player, Vertex vertex) {
		return vertex.getUnits().equals(player.getPeasants()) ||
			vertex.getUnits().equals(player.getFootmans()) ||
			vertex.getUnits().equals(player.getArchers()) ||
			vertex.getUnits().equals(player.getGriffons());
	}

	public boolean hasArmy(Player player, Battlefield field) {
		return getUnits(player, field).size() != 0 ? true : false; 
	}
}
