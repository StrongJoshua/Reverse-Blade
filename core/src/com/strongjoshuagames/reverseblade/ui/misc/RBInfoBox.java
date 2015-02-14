package com.strongjoshuagames.reverseblade.ui.misc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor.Direction;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;

public class RBInfoBox extends Table
{
	public RBInfoBox(String... info)
	{
		NinePatch n = RBResources.styles.gameSkin.getPatch("infoBox");
		this.setBackground(new NinePatchDrawable(n));

		this.setText(info);
	}

	public void setText(String... info)
	{
		this.clear();

		for(String s : info)
		{
			this.add(new Label(s, RBResources.styles.infoBoxLabelStyle)).expand().fill().row();
		}
		this.setSize(this.getPrefWidth(), this.getPrefHeight());
	}

	public void resetPosition(Direction d, Camera c)
	{
		if(!(d == Direction.LEFT || d == Direction.RIGHT))
			throw new IllegalArgumentException("Direction must be either left or right!");

		if(d == Direction.LEFT)
			this.setPosition(c.position.x - c.viewportWidth / 2, c.position.y - c.viewportHeight / 2);
		else
			this.setPosition(c.position.x + c.viewportWidth / 2 - this.getWidth(), c.position.y - c.viewportHeight / 2);
	}
}