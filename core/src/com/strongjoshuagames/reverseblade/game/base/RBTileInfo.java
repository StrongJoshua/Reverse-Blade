package com.strongjoshuagames.reverseblade.game.base;

import com.strongjoshuagames.reverseblade.extraFramework.MovementPath;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor;

public class RBTileInfo extends RBActor
{
	public enum Tint
	{
		RED, GREEN, BLUE, BLACK
	}

	private Tint tint;
	private RBClass unit;
	private int move;
	private MovementPath path;

	/**
	 * Creates a new RBTileInfo object for storage of tile info in the map grid. Tint is the default; black.
	 * 
	 * @param s The tile's name.
	 * @param m The tile's movement cost.
	 */
	public RBTileInfo(String s, int m)
	{
		this(s, m, Tint.BLACK);
	}

	/**
	 * Creates a new RBTileInfo object for storage of tile info in the map grid.
	 * 
	 * @param s The tile's name.
	 * @param m The tile's movement cost.
	 * @param t The tile's tint, to be modified later for movement phases.
	 */
	public RBTileInfo(String s, int m, Tint t)
	{
		this.setSprite("images/TileTinter.png");
		setTint(t);
		unit = null;
		this.setName(s);
		move = m;
	}

	public void setTint(Tint t)
	{
		int r = 0;
		int g = 0;
		int b = 0;

		if(t.equals(Tint.RED))
			r = 1;
		else if(t.equals(Tint.GREEN))
			g = 1;
		else if(t.equals(Tint.BLUE))
			b = 1;

		this.setColor(r, g, b, .5f);

		tint = t;
	}

	public Tint getTint()
	{
		return tint;
	}

	/**
	 * Sets a unit onto this tile.
	 * 
	 * @param u The unit.
	 */
	public void setUnit(RBClass u)
	{
		unit = u;
		if(unit != null)
		{
			u.setPosition(this.getX(), this.getY());
			u.setCurrentTile(this);
		}
	}
	
	public RBClass getUnit()
	{
		return unit;
	}
	
	public int getMovementCost()
	{
		return move;
	}
	
	/**
	 * 
	 * @param m The new path.
	 * @return True if the path was changed.
	 */
	public boolean setPath(MovementPath m)
	{
		if(m == null)
		{
			path = null;
			return true;
		}
		
		if(path == null)
		{
			path = m;
			return true;
		}
			
		int c = path.compareTo(m);
		if(c < 0)
		{
			path = m;
			return true;
		}
		
		return false;
	}
	
	public MovementPath getPath()
	{
		return path;
	}
}