package com.strongjoshuagames.reverseblade.ui.misc;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.strongjoshuagames.reverseblade.extraFramework.FPoint;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor.Direction;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;

public class RBMultiButton extends Table
{
	private TextButtonStyle top, middle, bottom;
	private ClickListener currentListener;
	private Array<TextButton> buttonArray;
	private boolean justCanceled;
	private int tileX, tileY;

	public RBMultiButton()
	{
		top = RBResources.styles.gameSkin.get("multiButtonTop", TextButtonStyle.class);
		middle = RBResources.styles.gameSkin.get("multiButtonMiddle", TextButtonStyle.class);
		bottom = RBResources.styles.gameSkin.get("multiButtonBottom", TextButtonStyle.class);
		
		buttonArray = new Array<TextButton>();
	}
	
	public RBMultiButton(ClickListener l)
	{
		this();
		currentListener = l;
	}

	public RBMultiButton(ClickListener l, String... buttons)
	{
		this();
		setButtons(l, buttons);
	}

	/**
	 * Sets the buttons in the RBMultiButton.
	 * 
	 * @param l The listener for the buttons.
	 * @param buttons The text of the buttons.
	 */
	public void setButtons(ClickListener l, String... buttons)
	{
		currentListener = l;
		setButtons(buttons);
	}

	/**
	 * Sets the buttons using the last given listener (do not use if you never sent the RBMultiButton a listener).
	 * 
	 * @param buttons The text of the buttons.
	 */
	public void setButtons(String... buttons)
	{
		buttonArray.clear();
		this.clear();
		this.pad(0);
		
		for(int i = 0; i < buttons.length; i++)
		{
			String s = buttons[i];

			TextButtonStyle tbs;
			if(i == 0)
				tbs = top;
			else if(i == buttons.length - 1)
				tbs = bottom;
			else
				tbs = middle;

			TextButton t = new TextButton(s, tbs);
			t.addListener(currentListener);
			this.add(t).expand().fill().row();
			buttonArray.add(t);
		}

		this.setSize(this.getPrefWidth(), this.getPrefHeight());
	}

	public void resetPosition(Direction d, Direction d2, int tileX, int tileY, int tileSX, int tileSY)
	{
		if(!(d == Direction.LEFT || d == Direction.RIGHT))
			throw new IllegalArgumentException("First direction must be either left or right!");
		if(!(d2 == Direction.UP || d2 == Direction.DOWN))
			throw new IllegalArgumentException("Second direction must be either up or down!");

		this.tileX = tileX;
		this.tileY = tileY;
		
		float x = tileX * tileSX;
		float y = tileY * tileSY;

		if(d == Direction.LEFT)
			x -= this.getWidth();
		else
			x += tileSX;

		if(d2 == Direction.UP)
			y += tileSY;
		else
			y -= this.getHeight();

		this.setPosition(x, y);
	}
	
	public String [] getButtonTexts()
	{
		String [] ss = new String[buttonArray.size];
		
		for(int i = 0; i < buttonArray.size; i++)
		{
			ss[i] = buttonArray.get(i).getText().toString();
		}
		
		return ss;
	}
	
	public int getCheckedButton()
	{
		for(int i = 0; i < buttonArray.size; i++)
		{
			if(buttonArray.get(i).isChecked())
				return i;
		}
		return -1;
	}
	
	public void uncheckAll()
	{
		for(TextButton b : buttonArray)
			b.setChecked(false);
	}
	
	public void cancel()
	{
		justCanceled = true;
		this.setVisible(false);
	}
	
	public boolean justCanceled()
	{
		boolean tmp = justCanceled;
		justCanceled = false;
		
		return tmp;
	}
	
	public FPoint getTile()
	{
		return new FPoint(tileX, tileY);
	}
}