package com.strongjoshuagames.reverseblade.game.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.strongjoshuagames.reverseblade.extraFramework.FPoint;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;

public abstract class RBClass extends RBActor implements Serializable
{
	private int health, level, moves;
	private int[] stats, growthRates;
	private Allegiance alle;
	private RBTileInfo prev, current;
	private boolean hasMoved;
	private Inventory inventory;

	public enum Classes
	{
		Wolf;
		
		public String getClassDescription()
		{
			String[] ss = RBResources.text.getLines(RBResources.text.classDescriptions);
			
			return ss[this.ordinal()];
		}
	}
	
	public enum Stats
	{
		HP, STR, MAG, DEF, RES, SPD, SKL, LUK;
		
		public String getStatDescription()
		{
			String[] ss = RBResources.text.getLines(RBResources.text.statDescriptions);
			
			return ss[this.ordinal()];
		}
	}

	/**
	 * Allegiance is used to determine how a Unit interacts with characters. <br>
	 * There are 3 types:<br>
	 * <b>PLAYER<br>
	 * NPC<br>
	 * FOE</b>
	 * 
	 * @author Jan
	 * 
	 */
	public enum Allegiance
	{
		PLAYER, NPC, FOE
	}

	/**
	 * Creates a new Unit. Make sure to set the sprite/animation in the sub class!
	 * 
	 * @param name The unit's name.
	 * @param level The unit's starting level.
	 * @param baseStats The unit's base stats (the same for all generic classes and sometimes differentiates for characters).
	 * @param gRs The unit's growth rates (same variation as base stats).
	 * @param a The unit's allegiance (PLAYER, NPC, or FOE).
	 */
	protected RBClass(String name, int level, int[] baseStats, int[] gRs, Allegiance a, int moves)
	{
		int i = Stats.values().length;
		if(gRs.length != i)
			throw new IllegalArgumentException("Must have the " + i + " growth rates.");
		if(baseStats.length != i)
			throw new IllegalArgumentException("Must have the " + i + " base stats.");

		this.setName(name);
		this.level = 1;
		growthRates = gRs;
		stats = baseStats;
		alle = a;

		levelUp(level - 1);
		resetHealth();

		this.moves = moves;
		
		inventory = new Inventory();
	}

	public Allegiance getAllegiance()
	{
		return alle;
	}

	/**
	 * Levels up the unit by 1.
	 */
	public void levelUp()
	{
		for(int i = 0; i < stats.length; i++)
		{
			if(MathUtils.random(100) + 1 < growthRates[i])
				stats[i]++;
		}
		level++;
	}

	/**
	 * Levels up the unit by <i>i</i>.
	 * 
	 * @param i The amount of levels the unit will gain.
	 */
	public void levelUp(int i)
	{
		for(; i > 0; i--)
			levelUp();
	}

	/**
	 * @return The unit's current level.
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * @return The unit's current health.
	 */
	public int getHealth()
	{
		return health;
	}

	/**
	 * Resets the unit's health to full.
	 */
	public void resetHealth()
	{
		setHealth(stats[Stats.HP.ordinal()]);
	}

	/**
	 * Sets the health to the given amount.
	 * 
	 * @param i The health to set to. If greater than the unit's max health, the unit's health becomes its max health.
	 */
	public void setHealth(int i)
	{
		health = i;
	}

	/**
	 * Sets the level without affecting stats.
	 * 
	 * @param i The level the unit is to be set to.
	 */
	public void setLevel(int i)
	{
		level = i;
	}

	/**
	 * Sets the given stat to the given Value.
	 * 
	 * @param s The stat to change.
	 * @param i The Value to change the stat to.
	 */
	public void setStat(Stats s, int i)
	{
		stats[s.ordinal()] = i;
	}

	public int getStat(Stats s)
	{
		return stats[s.ordinal()];
	}

	public int[] getStats()
	{
		return stats;
	}

	/**
	 * Get this unit's experience modifier. To be used when killed to award exp. to killer.<br>
	 * Should be overriden by subclasses.
	 * 
	 * @return The modifier. Multiply this Value by the base exp.
	 */
	public float getExpMod()
	{
		return 1;
	}

	public int getMoves()
	{
		return moves;
	}

	@Override
	public void setSprite(String s)
	{
		super.setSprite("unitMaps/" + s);
	}

	/**
	 * A convenience method for setting the unit's tile previous. Does nothing except store that object.
	 * 
	 * @param i The tile.
	 */
	public void setPreviousTile(RBTileInfo i)
	{
		prev = i;
	}

	/**
	 * A convenience method for setting the unit's tile. Does nothing except store that object.
	 * 
	 * @param i The tile.
	 */
	public void setCurrentTile(RBTileInfo i)
	{
		current = i;
	}

	/**
	 * Sets the unit's current tile and stores the previous tile.<br>
	 * <b>Does not have any impact upon the Unit's physical location.</b>
	 * 
	 * @param x The x position of the new tile.
	 * @param y The y position of the new tile.
	 */
	/*
	 * public void setTile(RBTileInfo i) { if(currentTile != null) prevTile = currentTile; currentTile = i; if(prevTile != null)
	 * prevTile.setUnit(null); }
	 */

	/**
	 * Resets the unit's tile to it's recent previous tile (if it has one).<br>
	 * <b>Does not have any impact upon the Unit's physical location.</b>
	 */
	/*
	 * public void resetTile() { if(prevTile == null) return; currentTile.setUnit(null); currentTile = prevTile; }
	 */

	/**
	 * A convenience method for retrieving the unit's previous tile. Does nothing except store that object.
	 * 
	 * @return The tile.
	 */
	public RBTileInfo getPreviousTile()
	{
		return prev;
	}

	/**
	 * A convenience method for retrieving the unit's tile. Does nothing except store that object.
	 * 
	 * @return The tile.
	 */
	public RBTileInfo getCurrentTile()
	{
		return current;
	}
	
	public void setHasMoved(boolean b)
	{
		hasMoved = b;
		
		if(b)
			this.setColor(Color.GRAY);
		else
			this.setColor(Color.WHITE);
	}
	
	public boolean hasMoved()
	{
		return hasMoved;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}

	/**
	 * Sets and/or resets the unit's sprite. Should be called when adding the Unit to a Map.
	 */
	public abstract void resetSprite();

	/**
	 * @return The unit's class.
	 */
	public abstract Classes getUnitClass();

	@Override
	public void write(Json json)
	{
		json.writeObjectStart(this.getName());
		
		json.writeValue("Level", level);
		json.writeValue("Health", health);
		json.writeValue("Allegiance", alle);
		json.writeValue("Stats", stats);
		json.writeValue("Has Moved", hasMoved);
		json.writeValue("Position", new FPoint(this.getX(), this.getY()));
		
		json.writeObjectEnd();
	}
	
	@Override
	public void read(Json json, JsonValue jsonData)
	{
		level = json.readValue("Level", Integer.class, jsonData.child());
		health = json.readValue("Health", Integer.class, jsonData.child());
		alle = json.readValue("Allegiance", Allegiance.class, jsonData.child());
		stats = json.readValue("Stats", int[].class, jsonData.child());
		setHasMoved(json.readValue("Has Moved", Boolean.class, jsonData.child()));
		
		FPoint p = json.readValue("Position", FPoint.class, jsonData.child());
		this.setPosition(p.x, p.y);
	}

	public void setMaxHealth(int health) {
		stats[Stats.HP.ordinal()] = health;
	}
}