package com.strongjoshuagames.reverseblade.ui.base;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor;
import com.strongjoshuagames.reverseblade.ui.stages.RBStage;
import com.strongjoshuagames.reverseblade.ui.stages.RBStageMainMenu;

public class ReverseBlade extends ApplicationAdapter
{
	public static final int GAME_WIDTH = 1136, GAME_HEIGHT = 640;
	public static int width, height;
	public static RBStage currentStage, nextStage;
	private static StretchViewport view;
	private static boolean transitioning;
	private static float transitionTime;
	private static InputProcessor previousProcessor;

	@Override
	public void create()
	{
		// set up frame/////////
		Gdx.graphics.setTitle("Reverse Blade - In Development");

		if(Gdx.app.getType().equals(ApplicationType.Desktop))
		{
			width = GAME_WIDTH;
			height = GAME_HEIGHT;

			Gdx.app.getGraphics().setDisplayMode(width, height, false);
		}
		else
		{
			width = Gdx.app.getGraphics().getWidth();
			height = Gdx.app.getGraphics().getHeight();

			Gdx.app.getGraphics().setDisplayMode(width, height, true);
		}
		// //////////////////////

		RBResources.initResources();
		view = new StretchViewport(GAME_WIDTH, GAME_HEIGHT);
		view.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		setStage(new RBStageMainMenu());
	}

	/**
	 * Transitions to the next stage by fading out the current one and fading in the next.
	 * 
	 * @param s The next stage.
	 */
	public static void transition(RBStage s)
	{
		nextStage = s;
		transitioning = true;
		transitionTime = RBActor.FADE_SLOW;
		Gdx.input.setInputProcessor(null);
	}

	private static void checkTransition(float delta)
	{
		if(nextStage != null)
		{
			transitionTime -= delta;
			currentStage.getRoot().setColor(1, 1, 1, transitionTime / RBActor.FADE_SLOW);
			if(currentStage.getRoot().getColor().a <= 0)
			{
				setStage(nextStage);
				nextStage = null;
				transitionTime = 0;
				currentStage.getRoot().setColor(1, 1, 1, 0);
				previousProcessor = Gdx.input.getInputProcessor();
				Gdx.input.setInputProcessor(null);
			}
		}
		else
		{
			transitionTime += delta;
			currentStage.getRoot().setColor(1, 1, 1, transitionTime / RBActor.FADE_SLOW);
			if(currentStage.getRoot().getColor().a >= 1)
			{
				transitioning = false;
				Gdx.input.setInputProcessor(previousProcessor);
			}
		}
	}

	private static void setStage(RBStage s)
	{
		if(currentStage != null)
			currentStage.dispose();
		currentStage = s;
		Gdx.input.setInputProcessor(currentStage);
		currentStage.setViewport(view);
	}

	@Override
	public void render()
	{
		// update actors if not transitioning to new stage
		if(transitioning)
			checkTransition(Gdx.graphics.getDeltaTime());
		else
			currentStage.act();

		// render
		Gdx.gl20.glClearColor(0, 0, 0, 0);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		currentStage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		currentStage.getViewport().update(width, height);
	}

	@Override
	public void dispose()
	{
		currentStage.dispose();
		RBResources.dispose();
	}
}