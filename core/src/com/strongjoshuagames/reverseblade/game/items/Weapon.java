package com.strongjoshuagames.reverseblade.game.items;

import com.strongjoshuagames.reverseblade.game.base.Item;
import com.strongjoshuagames.reverseblade.game.base.RBClass;
import com.strongjoshuagames.reverseblade.game.base.RBClass.Classes;

public abstract class Weapon extends Item implements Comparable<Weapon>
{
	/**
	 * Weapon Types:<br>
	 * <b>
	 * Sword<br>
	 * Lance<br>
	 * Axe<br>
	 * Bow<br>
	 * Pole<br>
	 * Fists<br>
	 * Other
	 * </b>
	 * @author Jan
	 */
	public enum WType
	{
		Sword, Lance, Axe, Bow, Pole, Fists, Other
	}
	
	@Override
	public IType getItemType()
	{
		return Item.IType.Weapon;
	}
	
	@Override
	public boolean isEquippable(RBClass u)
	{
		Classes[] cs = getEquippableClasses();
		
		for(Classes c : cs)
			if(c.equals(u.getUnitClass()))
				return true;
		return false;
	}
	
	public abstract WType getWeaponType();
	public abstract Classes[] getEquippableClasses();
	
	@Override
	public int compareTo(Weapon w)
	{
		WType t1 = this.getWeaponType();
		WType t2 = w.getWeaponType();
		
		if(t1.equals(WType.Sword))
		{
			if(t2.equals(WType.Lance))
				return -1;
			else if(t2.equals(WType.Axe))
				return 1;
		}
		else if(t1.equals(WType.Lance))
		{
			if(t2.equals(WType.Axe))
				return -1;
			else if(t2.equals(WType.Sword))
				return 1;
		}
		else if(t1.equals(WType.Axe))
		{
			if(t2.equals(WType.Sword))
				return -1;
			else if(t2.equals(WType.Lance))
				return 1;
		}
		else if(t1.equals(WType.Pole))
		{
			if(t2.equals(WType.Fists) || t2.equals(WType.Sword) || t2.equals(WType.Lance) || t2.equals(WType.Axe))
				return 1;
			else if(t2.equals(WType.Pole))
				return 0;
			else
				return -1;
		}
		else if(t1.equals(WType.Fists))
		{
			if(t2.equals(WType.Bow))
				return 1;
			else
				return -1;
		}
		
		return 0;
	}
}