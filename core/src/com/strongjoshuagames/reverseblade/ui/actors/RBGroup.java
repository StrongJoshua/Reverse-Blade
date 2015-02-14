package com.strongjoshuagames.reverseblade.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

public class RBGroup extends Group
{
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if(this.getColor().a <= 0 && this.isVisible())
			this.setVisible(false);
		else if(this.getColor().a > 0 && !this.isVisible())
			this.setVisible(true);
		
		if(this.getColor().a < 1 && this.isTouchable())
			this.setTouchable(Touchable.disabled);
		else if(this.getColor().a == 1 && !this.isTouchable())
			this.setTouchable(Touchable.enabled);
	}
	
	@Override
	public void addActor(Actor a)
	{
		super.addActor(a);
		
		float bottom = Float.MAX_VALUE;
		float top = -Float.MAX_VALUE;
		float left = Float.MAX_VALUE;
		float right = -Float.MAX_VALUE;
		
		Array<Actor> actors = this.getChildren();
		
		for(Actor aa : actors)
		{
			if(aa.getX() < left)
				left = aa.getX();
			if(aa.getRight() > right)
				right = aa.getRight();
			if(aa.getY() < bottom)
				bottom = aa.getY();
			if(aa.getTop() > top)
				top = aa.getTop();
		}
		
		this.setWidth(right - left);
		this.setHeight(top - bottom);
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if(visible)
			this.getColor().a = 1;
		else
			this.getColor().a = 0;
	}
}