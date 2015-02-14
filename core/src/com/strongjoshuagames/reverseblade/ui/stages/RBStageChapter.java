package com.strongjoshuagames.reverseblade.ui.stages;

import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import com.strongjoshuagames.reverseblade.game.base.Chapter;
import com.strongjoshuagames.reverseblade.game.save.SaveState;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;
import com.strongjoshuagames.reverseblade.ui.base.RBResources.States.State;

public class RBStageChapter extends RBStage
{
	private SaveState state;
	private Chapter chapter;
	private Console console;

	public RBStageChapter(State s)
	{
		state = (SaveState) RBResources.readObject(SaveState.class, s);
		chapter = (Chapter) RBResources.classFromString("com.strongjoshuagames.reverseblade.game.chapters." + state.player.chapter);
	}

	@Override
	public void draw()
	{
		chapter.render();
		super.draw();
		console.draw();
	}

	@Override
	public void init()
	{
		chapter.setStage(this);
		chapter.init(state);
		console = new Console();
		console.setCommandExecutor(new Executor());
	}

	@Override
	public void dispose()
	{
		state.save();
		chapter.dispose();
		console.dispose();
		super.dispose();
	}
	
	public class Executor extends CommandExecutor {
		public void setHealthAll(int health) {
			chapter.getMap().setHealthAll(health);
		}
	}
}