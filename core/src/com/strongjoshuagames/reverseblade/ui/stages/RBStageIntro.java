package com.strongjoshuagames.reverseblade.ui.stages;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.strongjoshuagames.reverseblade.game.save.SaveState;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;
import com.strongjoshuagames.reverseblade.ui.base.RBResources.States.State;
import com.strongjoshuagames.reverseblade.ui.base.ReverseBlade;

public class RBStageIntro extends RBStage
{
	private Group[] parts;
	private final float timePerPart = 10;
	private float time;
	private int part;
	private State state;
	private RBActor bg;
	
	public RBStageIntro(State s)
	{
		state =  s;
	}

	@Override
	public void init()
	{
		bg = new RBActor(0, 0, RBResources.images.backgroundIntro);
		part = 0;
		time = timePerPart;

		this.addActor(bg);

		String[] ss = RBResources.text.getLines(RBResources.text.intro);
		parts = new Group[ss.length];

		for(int i = 0; i < ss.length; i++)
		{
			parts[i] = new Group();
			String[] tmp = ss[i].split("\\*");
			for(int j = 0; j < tmp.length; j++)
			{
				Label t = new Label(tmp[j], RBResources.styles.gameSkin);
				t.setPosition(0, 300 - 35 * j);
				parts[i].addActor(RBStage.centerActorStage(this, t, true, false));
			}
			parts[i].setColor(1, 1, 1, 0);
		}

		for(Group g : parts)
			this.addActor(g);
		parts[0].addAction(Actions.fadeIn(.5f));

		TextButton rba = new TextButton("Skip", RBResources.styles.textButtonStyle);
		rba.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f), Actions.fadeIn(1f))));
		rba.setPosition(ReverseBlade.GAME_WIDTH - rba.getWidth() - 10, 10);
		rba.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent e, float x, float y)
			{
				playSound(RBResources.sounds.buttonPress);
				createSave();
				ReverseBlade.transition(new RBStageChapter(state));
			}
		});
		
		this.addActor(rba);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		time -= delta;
		if(time <= 0 && part < parts.length - 1)
		{
			parts[part].addAction(Actions.fadeOut(RBActor.FADE_FAST));
			part++;
			parts[part].addAction(Actions.fadeIn(RBActor.FADE_FAST));

			time = timePerPart;
		}
		else if(time <= 0 && part == parts.length - 1)
		{
			createSave();
			ReverseBlade.transition(new RBStageChapter(state));
		}
	}
	
	private void createSave()
	{
		RBResources.writeObject(state, new SaveState(state));
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
}
