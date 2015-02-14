package com.strongjoshuagames.reverseblade.game.classes;

import com.strongjoshuagames.reverseblade.game.base.RBClass;
import com.strongjoshuagames.reverseblade.game.items.weapons.WeaponClaw;

public class ClassWolf extends RBClass
{
	public static final int[] baseStats = new int[] {5, 4, 0, 2, 1, 5, 3, 1},
			growthRates = new int[] {50, 50, 5, 20, 15, 60, 30, 20};

	/**
	 * Creates a new Wolf unit with the default allegiance of <i>Allegiance.FOE</i> and default level 5.
	 */
	public ClassWolf()
	{
		this(RBClass.Allegiance.FOE);
	}

	/**
	 * Creates a new Wolf unit at level 5.
	 * 
	 * @param a The Wolf's allegiance.
	 */
	public ClassWolf(Allegiance a)
	{
		this(5, a);
	}

	/**
	 * Creates a new Wolf unit.
	 * 
	 * @param level The Wolf's level.
	 * @param a The Wolf's allegiance.
	 */
	public ClassWolf(int level, Allegiance a)
	{
		this("Wolf", level, a);
	}

	/**
	 * Creates a new Wolf unit.
	 * 
	 * @param name The Wolf's name.
	 * @param level The Wolf's level.
	 * @param a The Wolf's allegiance.
	 */
	public ClassWolf(String name, int level, Allegiance a)
	{
		super(name, level, baseStats, growthRates, a, 9);
		this.getInventory().addItem(new WeaponClaw());;
	}
	
	@Override
	public void resetSprite()
	{
		this.setSprite("TestWolf.png");
	}

	@Override
	public Classes getUnitClass()
	{
		return RBClass.Classes.Wolf;
	}
}