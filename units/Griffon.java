package units;

import java.util.Random;

public class Griffon extends Creature {

	public Griffon() {
		super();
		setDamage(15);
		setDefense(6);
		setHealth(20);
		setMana(0);
		setStamina(5);
		setCritChance(13);
		setAttackRange(2);
	}

	@Override
	public int getAttackDamage() {
		Random rand = new Random();
		int randNumber = rand.nextInt(100);
		return (randNumber <= this.getCritChance()) ? this.getDamage() * 2 : this.getDamage();
	}
}
