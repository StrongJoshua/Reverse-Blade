package com.strongjoshuagames.reverseblade.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

public class RBActor extends Actor
{
	public enum Fade
	{
		fadeNot, fadeIn, fadeOut
	}
	
	public enum Direction
	{
		LEFT, RIGHT, UP, DOWN
	}

	public static final float FADE_FAST = .3f, FADE_SLOW = 1;
	private Sprite sprite;
	private RepeatAction fadeLoop;

	public RBActor()
	{}

	public RBActor(float x, float y)
	{
		this.setPosition(x, y);
	}

	public RBActor(float x, float y, String sprite)
	{
		this(x, y);
		this.setSprite(sprite);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
	}

	@Override
	public void draw(Batch b, float parentAlpha)
	{
		if(sprite == null)
			return;

		Color c = this.getColor();

		Color p = new Color(1, 1, 1, 1);
		if(this.hasParent())
			p = this.getParent().getColor();

		if(p.a <= 0)
			return;

		b.setColor(c.r * p.r, c.g * p.g, c.b * p.b, c.a * parentAlpha);
		b.draw(sprite, getX(), getY());
		b.setColor(1, 1, 1, 1);
	}

	/**
	 * Sets the actors sprite.
	 * 
	 * @param s The sprite to be displayed.
	 */
	public void setSprite(Sprite s)
	{
		this.sprite = s;
		this.setSize(sprite.getWidth(), sprite.getHeight());
	}

	/**
	 * Sets the actors sprite according to the passed file.
	 * 
	 * @param fh The image file to this actor's sprite.
	 */
	public void setSprite(FileHandle fh)
	{
		this.setSprite(new Sprite(new Texture(fh)));
	}

	/**
	 * Sets the sprite to the sprite at the given path.
	 * 
	 * @param s The sprite's image path.
	 */
	public void setSprite(String s)
	{
		this.setSprite(Gdx.files.internal(s));
		//spriteStr = s;
	}

	/**
	 * 
	 * @return The actor's sprite, for debugging purposes.
	 */
	public Sprite getSprite()
	{
		return sprite;
	}
	
	/*public String getSpriteStr()
	{
		return spriteStr;
	}*/

	public void voidSprite()
	{
		sprite = null;
	}
	
	public void setLoopingFade(float time)
	{
		fadeLoop = Actions.forever(Actions.sequence(Actions.delay(time), Actions.fadeOut(time), Actions.fadeIn(time)));
		
		this.addAction(fadeLoop);
	}
	
	public void stopLoopingFade()
	{
		fadeLoop.reset();
		this.setColor(1, 1, 1, 1);
	}
	
	public void restartLoop()
	{
		fadeLoop.restart();
		this.setColor(1, 1, 1, 1);
	}
}