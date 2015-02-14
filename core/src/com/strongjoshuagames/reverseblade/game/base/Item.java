package com.strongjoshuagames.reverseblade.game.base;

import com.strongjoshuagames.reverseblade.ui.actors.RBActor;

public abstract class Item extends RBActor
{
	/**
	 * Item Types:<br>
	 *<b>
	 *Weapon<br>
	 *Potion<br>
	 *Jewel<br>
	 *Promoter
	 *</b>
	 *@author Jan
	 */
	public enum IType
	{
		Weapon, Potion, Jewel, Promoter
	}
	
	/**
	 * Checks if the given unit can equip the item.
	 * @return Returns true if the unit can equip the item, or false if the unit cannot, or if
	 * the item cannot itself be equipped.
	 */
	public abstract boolean isEquippable(RBClass u);

	/** Resets the sprite so saving of sprites is not necessary.*/
	public abstract void resetSprite();
	
	/** @return The type of item; designated by {@link Item.IType}.*/
	public abstract IType getItemType();
}