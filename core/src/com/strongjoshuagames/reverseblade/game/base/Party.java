package com.strongjoshuagames.reverseblade.game.base;

import com.badlogic.gdx.utils.Array;

public class Party
{
	private Array<RBClass> units;

	public Party()
	{
		units = new Array<RBClass>();
	}

	/**
	 * Creates a blank party and adds the given units to it.
	 * 
	 * @param us The units to be added.
	 */
	public Party(RBClass... us)
	{
		units.addAll(us);
	}

	public void addUnit(RBClass u)
	{
		units.add(u);
	}
	
	public Array<RBClass> getUnits()
	{
		return units;
	}
}