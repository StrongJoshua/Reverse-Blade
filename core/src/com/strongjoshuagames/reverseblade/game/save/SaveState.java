package com.strongjoshuagames.reverseblade.game.save;

import com.strongjoshuagames.reverseblade.ui.base.RBResources;
import com.strongjoshuagames.reverseblade.ui.base.RBResources.States.State;

public class SaveState
{
	private State state;
	public PlayerState player;
	public MapState map;

	public SaveState()
	{};

	public SaveState(State state)
	{
		this.state = state;
		player = new PlayerState();
	}

	public State getState()
	{
		return state;
	}
	
	public void save()
	{
		RBResources.writeObject(state, this);
	}
}