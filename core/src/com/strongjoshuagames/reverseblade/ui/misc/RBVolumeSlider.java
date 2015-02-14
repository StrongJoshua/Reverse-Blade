package com.strongjoshuagames.reverseblade.ui.misc;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;
import com.strongjoshuagames.reverseblade.ui.stages.RBStage;

public class RBVolumeSlider extends Slider implements EventListener
{
	
	/**
	 * Creates a new slider using the game's skin.
	 * 
	 * @param min The minimum value for the slider.
	 * @param max The maximum value for the slider.
	 * @param interval How much each visual interval changes in the slider.
	 * @param vertical If the slider should be vertical.
	 */
	public RBVolumeSlider(float min, float max, float interval, boolean vertical)
	{
		super(min, max, interval, vertical, RBResources.styles.gameSkin);
		this.addListener(this);
	}

	@Override
	public boolean handle(Event event)
	{
		if(event instanceof ChangeListener.ChangeEvent)
		{
			RBResources.sounds.volume = this.getValue();
		}
		else if(event instanceof InputEvent)
		{
			if(((InputEvent)event).getType().equals(InputEvent.Type.touchUp))
				((RBStage)this.getStage()).playSound(RBResources.sounds.buttonPress);
		}
		return true;
	}
}