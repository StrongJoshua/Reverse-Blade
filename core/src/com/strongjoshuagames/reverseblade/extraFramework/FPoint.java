package com.strongjoshuagames.reverseblade.extraFramework;

public class FPoint
{
	public float x, y;

	/**
	 * Creates a new FPoint with arguments of <b>0, 0</b>.
	 */
	public FPoint()
	{
		this(0, 0);
	}

	/**
	 * Creates a new FPoint.
	 * 
	 * @param x The x-coordinate of the FPoint.
	 * @param y The y-coordinate of the FPoint.
	 */
	public FPoint(float x, float y)
	{
		setLocation(x, y);
	}

	/**
	 * Sets the FPoint to a new location. Does the same thing as creating a new FPoint.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void setLocation(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Moves the point by the given amounts.
	 * 
	 * @param x Amount to move in the x-direction.
	 * @param y Amount to move in the y-direction.
	 */
	public void translate(float x, float y)
	{
		this.x += x;
		this.y += y;
	}

	/**
	 * 
	 * @param p The given FPoint.
	 * @return The distance from this point to the given FPoint.
	 */
	public double getDistance(FPoint p)
	{
		return Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));
	}

	@Override
	public String toString()
	{
		return "[" + x + "," + y + "]";
	}
}