package com.strongjoshuagames.reverseblade.ui.stages;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;
import com.strongjoshuagames.reverseblade.ui.base.ReverseBlade;

public abstract class RBStage extends Stage
{
	@Override
	public void setViewport(Viewport v)
	{
		super.setViewport(v);

		Camera c = this.getCamera();
		c.position.set(c.viewportWidth / 2, c.viewportHeight / 2, 0);
		c.update();
		
		init();
	}
	
	public void playSound(Sound s)
	{
		if(RBResources.sounds.isSoundOn)
			s.play(RBResources.sounds.volume/100f);
	}

	/**
	 * Initializes all the actors.
	 */
	public abstract void init();

	/**
	 * Centers the given actor in the x, y, or both directions, on the screen.
	 * 
	 * @param s The stage the actor will be added to (only necessary if actor not yet added).
	 * @param a The actor.
	 * @param horizontal Center the actor horizontally.
	 * @param vertical Center the actor vertically.
	 * 
	 * @return The given actor, for chaining.
	 */
	public static Actor centerActorScreen(Stage s, Actor a, boolean horizontal, boolean vertical)
	{
		Stage tmp = a.getStage();

		if(tmp == null)
			tmp = s;

		Vector3 center = new Vector3(ReverseBlade.width / 2 - a.getWidth() / 2, ReverseBlade.height / 2 - a.getHeight() / 2, 0);
		
		center = tmp.getCamera().unproject(center);

		if(horizontal)
			a.setX(center.x);
		if(vertical)
			a.setY(center.y);
				
		return a;
	}

	/**
	 * Centers the given actor in the x, y, or both directions, in the world.
	 * 
	 * @param s The stage the actor will be added to (only necessary if actor not yet added).
	 * @param a The actor.
	 * @param x Center the actor on the x-axis.
	 * @param y Center the actor on the y-axis.
	 * 
	 * @return The given actor, for chaining.
	 */
	public static Actor centerActorStage(Stage s, Actor a, boolean x, boolean y)
	{
		Stage tmp = a.getStage();
		if(tmp == null)
			tmp = s;

		float xC = a.getX();
		float yC = a.getY();

		if(x)
			xC = tmp.getWidth() / 2 - a.getWidth() / 2;
		if(y)
			yC = tmp.getHeight() / 2 - a.getHeight() / 2;

		a.setPosition(xC, yC);

		return a;
	}
}