package com.strongjoshuagames.reverseblade.game.items.weapons;

import com.strongjoshuagames.reverseblade.game.base.RBClass.Classes;
import com.strongjoshuagames.reverseblade.game.items.Weapon;

public class WeaponClaw extends Weapon
{

	@Override
	public WType getWeaponType()
	{
		return WType.Other;
	}

	@Override
	public void resetSprite()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Classes[] getEquippableClasses()
	{
		return new Classes[] {Classes.Wolf};
	}
	
}