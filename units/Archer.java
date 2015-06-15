package units;

import java.util.Random;

public class Archer extends Creature {

	public Archer() {
		super();
		setDamage(13);
		setDefense(3);
		setHealth(13);
		setMana(0);
		setStamina(3);
		setCritChance(10);
		setAttackRange(Integer.MAX_VALUE);
	}
	
	@Override
	public int getAttackDamage() {
		Random rand = new Random();
		int randNumber = rand.nextInt(100);
		return (randNumber <= this.getCritChance()) ? this.getDamage() * 2 : this.getDamage();
	}
}
