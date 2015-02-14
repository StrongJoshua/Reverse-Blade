package com.strongjoshuagames.reverseblade.game.chapters;

import com.strongjoshuagames.reverseblade.game.base.Chapter;
import com.strongjoshuagames.reverseblade.game.maps.RBMapPrologue;

public class ChapterPrologue extends Chapter
{
	public ChapterPrologue()
	{
		super(new RBMapPrologue());
	}
	
	@Override
	public String getName()
	{
		return "Prologue: The Escape";
	}
}