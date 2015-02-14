package com.strongjoshuagames.reverseblade.game.save;

import com.strongjoshuagames.reverseblade.game.base.Party;

public class PlayerState
{
	public String chapter;
	public Party party;

	public PlayerState()
	{
		party = new Party();
		chapter = "ChapterPrologue";
	}
}