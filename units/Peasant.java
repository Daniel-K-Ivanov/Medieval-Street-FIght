package units;

import java.util.Random;

public class Peasant extends Creature {

	public Peasant() {
		setDamage(7);	
		setDefense(2);
		setHealth(7);
		setMana(0);
		setStamina(3);
		setCritChance(5);
		setAttackRange(1);
	}

	@Override
	public int getAttackDamage() {
		Random rand = new Random();
		int randNumber = rand.nextInt(100);
		return (randNumber <= this.getCritChance()) ? this.getDamage() * 2 : this.getDamage();
	}
}
