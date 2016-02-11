package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import units.*;
public class Game {
	
	public static Player player;
	public static Player enemy;
	public HashMap<Units, Integer> shop = new HashMap<Units, Integer>();
	
	public Game() {
		setShop();
		setPlayer();
		setEnemy();
		menu();
	}
	
	private void menu() {
		System.out.println("-------------------------");
		System.out.println("|          Menu         |");
		System.out.println("|-----------------------|");
		System.out.println("|	1. Start game  	|");
		System.out.println("|	2. Shop		|");
		System.out.println("|	3. Exit		|");
		System.out.println("-------------------------");
		
		readCommands();
	}
	
	private void readCommands() {
		Scanner scan = new Scanner(System.in);
		String inputLine = "";
		while (scan.hasNext()) {
			inputLine = scan.nextLine();
			switch (inputLine) {
			case "1":
				Battlefield field = generateBattleField(new Battlefield());
				startGame(field);
				break;
			case "2":
				shopMenu();
			case "3":
				System.exit(0);
			default:
				System.err.println("Invalid command!");
				break;
			}
		}
		scan.close();
	}

	private void shopMenu() {
		System.out.println("-----------------------------");
		System.out.println("|            Commands       |");
		System.out.println("|---------------------------|");
		System.out.println("|     buy <unit> <amount>   |");
		System.out.println("|  1 - Show prices	    |");
		System.out.println("|  2 - Show gold  	    |");
		System.out.println("|  3 - Back		    |");
		System.out.println("-----------------------------");
		
		readShopCommands();
	}
	
	private void readShopCommands() {
		Scanner scan = new Scanner(System.in);
		String inputLine = "";
		while (scan.hasNext()) {
			inputLine = scan.nextLine();
			switch (inputLine) {
			case "1":
				printShop();
				break;
			case "2":
				System.out.println("You have " + Game.player.getGold() + " gold");
				break;
			case "3":
				menu();
				break;
			default:
				if (validBuyCommand(inputLine)) {
					//buy units
					String[] lineArray = inputLine.split(" ");
					Units typeUnit = getType(lineArray[1]);
					int amount = Integer.parseInt(lineArray[2]);
					
					if (player.getGold() < amount * this.shop.get(typeUnit)) {
						
						int goldShort = amount * this.shop.get(typeUnit) - player.getGold();
						System.err.println("You dont have enough gold!");
						System.out.println("You are " + goldShort + " gold short");
						
					}
					else {
						player.addUnits(typeUnit, amount);
						player.setGold(player.getGold() - amount * this.shop.get(typeUnit));
						player.printArmy();
					}
				}
				else {
					System.err.println("Invalid command!");
				}
				break;
			}
		}
		scan.close();
	}
	private Units getType(String unit) {
		switch (unit.toLowerCase()) {
		case "peasant":
			return Units.Peasant;
		case "footman":
			return Units.Footman;
		case "archer":
			return Units.Archer;
		case "griffon":
			return Units.Griffon;
		}
		return null;
	}

	private boolean validBuyCommand(String inputLine) {
		String[] lineArray = inputLine.split(" ");
		if (lineArray.length != 3) {
			return false;
		}
		else {
			//Check if lineArray matches "buy <unit> <amount>"
			if (lineArray[0].equalsIgnoreCase("buy") &&
				(lineArray[1].equalsIgnoreCase("peasant") ||
				lineArray[1].equalsIgnoreCase("footman") ||
				lineArray[1].equalsIgnoreCase("archer") ||
				lineArray[1].equalsIgnoreCase("griffon")) &&
				isInteger(lineArray[2])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	private void printShop() {
		System.out.println("Prices:");
		System.out.println("Peasant: " +this.shop.get(Units.Peasant) + " gold");
		System.out.println("Footman: " +this.shop.get(Units.Footman) + " gold");
		System.out.println("Archer : " +this.shop.get(Units.Archer) + " gold");
		System.out.println("Griffon: " +this.shop.get(Units.Griffon) + " gold");
	}

	private void startGame(Battlefield field) {
		
		boolean playerHasArmy = player.hasArmy(player, field);
		boolean enemyHasArmy = enemy.hasArmy(enemy, field);
		
		while (playerHasArmy && enemyHasArmy) {
			if (player.isOnTurn()) {
				field.printField(player, enemy, field);
				readGameCommands(field);
			} 
			else {
				enemy.play(player, enemy, field);
				field.printField(player, enemy, field);
				player.setOnTurn(true);
				player.setPeasantUsed(false);
				player.setFootmanUsed(false);
				player.setArcherUsed(false);
				player.setGriffonsUsed(false);
			}
			playerHasArmy = player.hasArmy(player, field);
			enemyHasArmy = enemy.hasArmy(enemy, field);
		}
		
		if (playerHasArmy) {
			System.out.println("-----------------------------");
			System.out.println("|          YOU WON!         |");
			System.out.println("| THE MEDIEVAL STREET FIGHT |");
			System.out.println("-----------------------------");
			System.out.println("You will get 200 gold from the battle.");
			player.setGold(player.getGold() + 200);
		}
		else {
			printLost();
		}
	}

	public static void printLost() {
		System.out.println("------------------------------");
		System.out.println("|          YOU LOST!         |");
		System.out.println("| THE MEDIEVAL STREET FIGHT! |");
		System.out.println("------------------------------");
		System.out.println("You will lose 100 gold from the battle.");
		player.setGold(player.getGold() - 100);
	}

	private void readGameCommands(Battlefield field) {

		//reading Commands 
		Scanner scan = new Scanner(System.in);
		String inputLine = "";
		while (scan.hasNext()) {
			inputLine = scan.nextLine();
			switch (inputLine.toLowerCase()) {
			case "print":
				System.out.println("Printing");
				field.printField(player, enemy, field);
				break;
			case "end":
				player.setOnTurn(false);
				System.out.println("You ended your turn!\nThe enemy is on turn.");
				startGame(field);
			case "exit":
				menu();
				break;
			default:
				
				String[] lineArray = inputLine.split(" ");
				if (lineArray.length == 3) {
					Vertex currentPosition = getVertex(lineArray[1], field);
					Vertex destinationPosition = getVertex(lineArray[2], field);
					
					if (!isEmptyCell(currentPosition) &&
							!isUsedThisTurn(currentPosition) &&
							Player.isPlayerUnit(player, currentPosition)) {
						//Move Command
						if (lineArray[0].equalsIgnoreCase("move")) { 
							move(currentPosition, destinationPosition, field);
							field.printField(player, enemy, field);
						}
						//Attack Command
						else if (lineArray[0].equalsIgnoreCase("attack")) { 
							attack(player, currentPosition, destinationPosition, field);
							field.printField(player, enemy, field);
						}
						else { System.err.println("Invalid command!"); }
					}
					else if(isEmptyCell(currentPosition)){
						System.err.println("Invalid coordinates!"); 
					}
					else {
						System.err.println("This unit made his turn");
					}
				}
				else {
					System.err.println("Invalid command!");
				}
				
				break;
			}
		}
		
		scan.close();

	}

	private boolean isUsedThisTurn(Vertex currentPosition) {
		String typeOfUnit = currentPosition.getUnits().get(0).getClass().getSimpleName();
		
		switch (typeOfUnit) {
		case "Peasant":
			return player.isPeasantUsed();
		case "Footman":
			return player.isFootmanUsed();
		case "Archer":
			return player.isArcherUsed();
		case "Griffon":
			return player.isGriffonsUsed();
		default:
			System.err.println("This should not print!");
			return false;
		}
	}
	

	public static void attack(Player player, Vertex currentPosition, Vertex attackedUnitPosition, Battlefield field) {
		
		int groupDamage;
		
		//Validate coordinates
		if (!isEmptyCell(currentPosition) &&
				!isEmptyCell(attackedUnitPosition) && 
				!currentPosition.equals(attackedUnitPosition) &&
				Player.isPlayerUnit(player, currentPosition)) {
			
			LinkedList<Vertex> path = generatePath(currentPosition, attackedUnitPosition, field);
			String type = getType(currentPosition);
			
			//Archer and Hero have max range
			if (!type.equals("Archer") && !type.equals("Hero")) {
				int attackRange = getCreature(type).getAttackRange();
				
				for (int i = 0; i < attackRange; i++) {
					if (path.pollLast() == null) { break; }
				}
				
				//Move to the target before Attack
				Vertex destinationPoint = path.peekLast();
				if (destinationPoint != null &&
						!destinationPoint.equals(currentPosition)) {
					move(currentPosition, destinationPoint, field);
					if (!isEmptyCell(currentPosition) || isEmptyCell(destinationPoint)) {
						return;
					}
				}
				
				Vertex positionBeforeAttack = destinationPoint;
				groupDamage = getGroupDamage(field, type, positionBeforeAttack,attackedUnitPosition);				
			}
			else {
				groupDamage = getGroupDamage(field, type, currentPosition, attackedUnitPosition); 
			}
			
			int targetX = attackedUnitPosition.getX();
			int targetY = attackedUnitPosition.getY();
			List<Creature> targetUnits = field.battlefield[targetX][targetY].getUnits();
			int damagePerUnit = groupDamage / targetUnits.size();
			
			for (int i = 0; i < targetUnits.size(); i++) {
				Creature target = targetUnits.get(i);
				int healthAfterAttack = target.getHealth() - damagePerUnit;;
				target.setHealth(healthAfterAttack);
				if (target.isDead()) { targetUnits.remove(i); }
			}
			
			setUnitUsed(type);
		}
		else {
			System.err.println(isEmptyCell(currentPosition) ? 
					"You can`t attack with empty cell." : "You can`t attack empty cell.");
		}
		
	}

	public static String getType(Vertex currentPosition) {
		return currentPosition.getUnits().get(0).getClass().getSimpleName();
	}

	private static int getGroupDamage(Battlefield field, String type, Vertex unitsPosition, Vertex attackedUnitPosition) {
		
		int x = unitsPosition.getX();
		int y = unitsPosition.getY();
		List<Creature> attackUnits = field.battlefield[x][y].getUnits();
		
		int groupDamage = 0;
		for (int i = 0; i < attackUnits.size(); i++) {
			groupDamage += attackUnits.get(i).getAttackDamage() - attackedUnitPosition.getUnits().get(0).getDefense(); //Calculates critical chance;
		}
		
		return groupDamage;
	}

	private Battlefield generateBattleField(Battlefield field) {
		
		field.battlefield[3][0].setUnits(player.getPeasants());
		field.battlefield[4][0].setUnits(player.getFootmans());
		field.battlefield[5][0].setUnits(player.getArchers());
		field.battlefield[6][0].setUnits(player.getGriffons());
		
		field.battlefield[3][9].setUnits(enemy.getPeasants());
		field.battlefield[4][9].setUnits(enemy.getFootmans());
		field.battlefield[5][9].setUnits(enemy.getArchers());
		field.battlefield[6][9].setUnits(enemy.getGriffons());
		
		return field;
	}

	public static void move(Vertex currentPosition, Vertex destinationPosition, Battlefield field) {
		if (!isEmptyCell(currentPosition) &&
				isEmptyCell(destinationPosition)) {
			
			LinkedList<Vertex> path = generatePath(currentPosition, destinationPosition, field);
			
			int currX = currentPosition.getX();
			int currY = currentPosition.getY();
			int destX = destinationPosition.getX();
			int destY= destinationPosition.getY();
			
			String typeOfUnit = currentPosition.getUnits().get(0).getClass().getSimpleName();
			int unitStamina = getCreature(typeOfUnit).getStamina();
			
			if (path.size() -1 <= unitStamina) {
				List<Creature> unitsToMove = field.battlefield[currX][currY].getUnits();
				field.battlefield[destX][destY].setUnits(unitsToMove);
				List<Creature> emptyList = new ArrayList<Creature>();
				field.battlefield[currX][currY].setUnits(emptyList);
				setUnitUsed(typeOfUnit);
			}
			else { 
				System.err.println("Not enough stamina!");
			}
		}
		else {
			System.err.println("Invalid coordinates!");
		}
		
	}

	private static void setUnitUsed(String type) {
		switch (type) {
		case "Peasant":
			player.setPeasantUsed(true);
			break;
		case "Footman":
			player.setFootmanUsed(true);
			break;
		case "Archer":
			player.setArcherUsed(true);
			break;
		case "Griffon":
			player.setGriffonsUsed(true);
			break;
		default:
			System.err.println("This should not print!");
			break;
		}		
	}

	public static LinkedList<Vertex> generatePath(Vertex currentPosition,
			Vertex destinationPosition, Battlefield field) {
		Graph graph = field.generateGraph(currentPosition, destinationPosition);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(currentPosition);
		LinkedList<Vertex> path = dijkstra.getPath(destinationPosition);
		return path;
	}

	private static Creature getCreature(String type) {
		switch (type) {
		case "Peasant":
			return new Peasant();
		case "Footman":
			return new Footman();
		case "Archer":
			return new Archer();
		case "Griffon":
			return new Griffon();
		default:
			System.err.println("This should not be printed!\nInvalid type of Creature.");
			break;
		}
		return null;
	}

	public static boolean isEmptyCell(Vertex currentPosition) {
		return (currentPosition.getUnits().size() > 0) ? false : true;
	}

	private Vertex getVertex(String string, Battlefield field) {
		int x;
		int y;
		try {
			x = Integer.parseInt(string.substring(1, 2));
			y = Integer.parseInt(string.substring(3, 4));
			return field.battlefield[x][y];
		} catch (NumberFormatException e) {
			System.err.println("Invalid coordinates!");
			startGame(field);
		} catch (Exception e) {
			System.err.println("Invalid coordinates!");
			startGame(field);
		}
		//This should not execute!
		System.out.println("This should not print!");
		return new Vertex(0, 0, null);
	}

	private void setPlayer() {
		player = new Player();
	}

	private void setEnemy() {
		enemy = new Player();
		enemy.setOnTurn(false);
		
		enemy.addUnits(Units.Peasant, 4);
		enemy.addUnits(Units.Footman, 1);
		enemy.addUnits(Units.Archer, 2);
		enemy.addUnits(Units.Griffon, 1);
	}
	
	private void setShop() {
		this.shop.put(Units.Peasant, 30);
		this.shop.put(Units.Footman, 90);
		this.shop.put(Units.Archer, 50);
		this.shop.put(Units.Griffon, 150);
	}
}
