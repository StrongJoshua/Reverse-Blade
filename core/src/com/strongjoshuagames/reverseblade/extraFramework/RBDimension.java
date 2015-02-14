package com.strongjoshuagames.reverseblade.extraFramework;

public class RBDimension
{
	public int width, height;
	
	public RBDimension()
	{
		this(0, 0);
	}
	
	public RBDimension(RBDimension d)
	{
		this(d.width, d.height);
	}
	
	public RBDimension(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void set(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 
	 * @return A copy of this RBDimension.
	 */
	public RBDimension copy()
	{
		return new RBDimension(this);
	}
}
