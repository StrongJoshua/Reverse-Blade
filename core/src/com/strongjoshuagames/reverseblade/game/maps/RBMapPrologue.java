package com.strongjoshuagames.reverseblade.game.maps;

import com.strongjoshuagames.reverseblade.game.base.Party;
import com.strongjoshuagames.reverseblade.game.base.RBMap;
import com.strongjoshuagames.reverseblade.game.classes.ClassWolf;
import com.strongjoshuagames.reverseblade.game.save.MapState;

public class RBMapPrologue extends RBMap
{
	public RBMapPrologue()
	{
		super("Test.tmx");
	}
	
	@Override
	public void setUpMapState()
	{
		Party foes = new Party();
		ClassWolf u = new ClassWolf();
		
		u.setPosition(grid.convertToPixelsX(5), grid.convertToPixelsY(5));
		
		foes.addUnit(u);
		
		state.map = new MapState(foes);
	}
}