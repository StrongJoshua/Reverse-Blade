package com.strongjoshuagames.reverseblade.game.save;

import com.strongjoshuagames.reverseblade.game.base.Party;

public class MapState
{
	public Party foes;
	
	public MapState()
	{}
	
	public MapState(Party p)
	{
		foes = p;
	}
}