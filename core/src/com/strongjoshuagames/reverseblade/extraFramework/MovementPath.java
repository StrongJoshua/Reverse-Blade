package com.strongjoshuagames.reverseblade.extraFramework;

/***
 * Used to optimize the recursive search for mobile to accommodate the slower CPUs.
 * @author Jan
 *
 */
public class MovementPath extends TilePath
{
	private int movesLeft;

	public MovementPath(int movesLeft, FPoint... fPoints)
	{
		super(fPoints);
		this.movesLeft = movesLeft;
	}

	public int getMovesLeft()
	{
		return movesLeft;
	}

	/**
	 * @param m The MovementPath to compare this one to.
	 * @return -1, 0, or 1 if the this path's moves left are less than, equal to, or greater than the given path's moves left.
	 */
	public int compareTo(MovementPath m)
	{
		int m1 = this.getMovesLeft();
		int m2 = m.getMovesLeft();

		if(m1 < m2)
			return -1;
		else if(m1 == m2)
			return 0;

		return 1;
	}
}