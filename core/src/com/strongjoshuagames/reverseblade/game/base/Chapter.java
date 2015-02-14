package com.strongjoshuagames.reverseblade.game.base;

import com.badlogic.gdx.utils.Disposable;
import com.strongjoshuagames.reverseblade.game.save.SaveState;
import com.strongjoshuagames.reverseblade.ui.stages.RBStage;

public abstract class Chapter implements Disposable
{
	private RBStage stage;
	private RBMap map;

	protected Chapter(RBMap m)
	{
		map = m;
	}

	public void init(SaveState s)
	{
		map.setStage(stage);
		map.init(s);
	}

	public void render()
	{
		map.render();
	}

	public void setStage(RBStage s)
	{
		stage = s;
	}

	/**
	 * @return The chapter's name.
	 */
	public abstract String getName();
	
	@Override
	public void dispose()
	{
		map.dispose();
	}

	public RBMap getMap() {
		return map;
	}
}