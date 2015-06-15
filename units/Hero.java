package units;

import java.util.Random;

public class Hero extends Creature {

	public Hero() {
		super();
		setDamage(20);
		setDefense(10);
		setHealth(50);
		setMana(0);
		setStamina(0);
		setCritChance(15);
		setAttackRange(Integer.MAX_VALUE);
	}

	@Override
	public int getAttackDamage() {
		Random rand = new Random();
		int randNumber = rand.nextInt(100);
		return (randNumber <= this.getCritChance()) ? this.getDamage() * 2 : this.getDamage();
	}
}
