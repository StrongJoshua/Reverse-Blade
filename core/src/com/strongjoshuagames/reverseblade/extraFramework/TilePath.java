package com.strongjoshuagames.reverseblade.extraFramework;

public class TilePath
{
	private FPoint[] path;
	private int point;

	public TilePath(FPoint... fPoints)
	{
		set(fPoints);
	}

	public void set(FPoint... fPoints)
	{
		path = fPoints;
		point = 0;
	}

	public int length()
	{
		return path.length;
	}

	public void goTo(int i)
	{
		if(i < 0 || i >= length())
			throw new IllegalArgumentException("Cannot go to: " + i + ". goTo must be between 0 and " + length()
					+ ", inclusive, exclusive, respectively.");
		point = i;
	}

	/**
	 * Goes to the next point on the path.
	 * 
	 * @return False if there is no next point.
	 */
	public boolean next()
	{
		point++;
		if(point >= length())
			return false;
		return true;
	}

	/**
	 * @return The current point on the path or null if {@link #next()} had previously returned false.
	 */
	public FPoint current()
	{
		if(point < length())
			return path[point];
		return null;
	}
	
	public FPoint[] getPoints()
	{
		return path;
	}
	
	/**
	 * @param p The TilePath to compare to.
	 * @return -1, 0, or 1 if this path's length is greater than, equal to, or less than the given path's length.
	 */
	public int compareTo(TilePath p)
	{
		int l1 = this.length();
		int l2 = p.length();

		if(l1 > l2)
			return -1;
		if(l1 == l2)
			return 0;
		
		return 1;
	}
}