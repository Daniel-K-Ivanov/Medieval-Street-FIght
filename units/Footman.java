package units;

import java.util.Random;

public class Footman extends Creature {

	public Footman() {
		super();
		setDamage(15);
		setDefense(4);
		setHealth(18);
		setMana(0);
		setStamina(4);
		setCritChance(7);
		setAttackRange(1);
	}

	@Override
	public int getAttackDamage() {
		Random rand = new Random();
		int randNumber = rand.nextInt(100);
		return (randNumber <= this.getCritChance()) ? this.getDamage() * 2 : this.getDamage();
	}
}
