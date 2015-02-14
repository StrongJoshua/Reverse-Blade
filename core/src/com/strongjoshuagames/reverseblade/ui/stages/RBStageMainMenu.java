package com.strongjoshuagames.reverseblade.ui.stages;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor;
import com.strongjoshuagames.reverseblade.ui.actors.RBGroup;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;
import com.strongjoshuagames.reverseblade.ui.base.RBResources.States.State;
import com.strongjoshuagames.reverseblade.ui.base.ReverseBlade;
import com.strongjoshuagames.reverseblade.ui.misc.RBVolumeSlider;

public class RBStageMainMenu extends RBStage
{
	private TextButton newGame, continueGame, options, state1, state2, state3;
	private RBActor bg, sound;
	private Slider volume;
	private RBGroup optionsGroup, stateGroup;

	@Override
	public void init()
	{
		bg = new RBActor(0, 0, RBResources.images.backgroundMainMenu);

		newGame = new TextButton("New Game", RBResources.styles.titleButtonStyle);
		continueGame = new TextButton("Continue", RBResources.styles.titleButtonStyle);
		options = new TextButton("Options", RBResources.styles.titleButtonStyle);

		Label title = new Label("ReverseBlade", RBResources.styles.gameTitleLabelStyle);
		title.setY(ReverseBlade.GAME_HEIGHT - title.getHeight());

		String soundImg = RBResources.sounds.isSoundOn ? RBResources.images.buttonSound : RBResources.images.buttonSoundNo;
		sound = new RBActor(0, 0, soundImg);

		volume = new RBVolumeSlider(0, 100, 1, false);

		String[] ss = RBResources.states.getStatesChapterNames();

		state1 = new TextButton(ss[0], RBResources.styles.textLargeButtonStyle);
		state2 = new TextButton(ss[1], RBResources.styles.textLargeButtonStyle);
		state3 = new TextButton(ss[2], RBResources.styles.textLargeButtonStyle);

		newGame.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent e, float x, float y)
			{
				performFunction(1);
			}
		});
		newGame.setPosition(150, ReverseBlade.GAME_HEIGHT - 300);

		continueGame.setPosition(newGame.getX(), newGame.getY() - 10);
		continueGame.setY(continueGame.getY() - continueGame.getHeight());
		continueGame.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent e, float x, float y)
			{
				performFunction(2);
			}
		});

		options.setPosition(continueGame.getX(), continueGame.getY() - 10);
		options.setY(options.getY() - options.getHeight());
		options.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent e, float x, float y)
			{
				performFunction(3);
			}
		});

		Label vLabel = new Label("Volume:", RBResources.styles.gameSkin);

		sound.setY(vLabel.getTop());
		sound.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent e, float x, float y)
			{
				performFunction(4);
			}
		});

		volume.setX(vLabel.getRight() + 10);
		volume.setY(vLabel.getTop() - vLabel.getHeight() / 2 - volume.getHeight() / 2 - 2);
		volume.setValue(RBResources.sounds.volume);

		state1.setY(state2.getHeight() + state3.getHeight() + 10);
		state2.setY(state2.getY() + state3.getHeight() + 5);
		state1.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				playSound(RBResources.sounds.buttonPress);
				if(state1.getText().equals("----"))
					checkState(State.state1, newGame.isChecked());
				else
					checkState(State.state1, newGame.isChecked());
				state1.toggle();
			}
		});
		state1.addListener(new ClickListener(Buttons.RIGHT)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				playSound(RBResources.sounds.buttonPress);
				Dialog d = new Dialog("Erase?", RBResources.styles.gameSkin);
				d.text("Would you like to erase this file?");
				TextButton yes = new TextButton("Yes", RBResources.styles.gameSkin), no = new TextButton("No",
						RBResources.styles.gameSkin);
				yes.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent e, float x, float y)
					{
						playSound(RBResources.sounds.buttonPress);
						RBResources.removeValue(RBResources.objectMemory, RBResources.States.State.state1);
						state1.setText("----");
						state1.pack();
					}
				});
				no.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent event, float x, float y)
					{
						playSound(RBResources.sounds.buttonPress);
					}
				});
				d.button(yes);
				d.button(no);
				d.show(state1.getStage());
			}
		});
		state2.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				playSound(RBResources.sounds.buttonPress);
				if(state2.getText().equals("----"))
					checkState(State.state2, newGame.isChecked());
				else
					checkState(State.state2, newGame.isChecked());
				state2.toggle();
			}
		});
		state2.addListener(new ClickListener(Buttons.RIGHT)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				playSound(RBResources.sounds.buttonPress);
				Dialog d = new Dialog("Erase?", RBResources.styles.gameSkin);
				d.text("Would you like to erase this file?");
				TextButton yes = new TextButton("Yes", RBResources.styles.gameSkin), no = new TextButton("No",
						RBResources.styles.gameSkin);
				yes.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent e, float x, float y)
					{
						playSound(RBResources.sounds.buttonPress);
						RBResources.removeValue(RBResources.objectMemory, RBResources.States.State.state2);
						state2.setText("----");
						state2.pack();
					}
				});
				no.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent event, float x, float y)
					{
						playSound(RBResources.sounds.buttonPress);
					}
				});
				d.button(yes);
				d.button(no);
				d.show(state1.getStage());
			}
		});
		state3.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				playSound(RBResources.sounds.buttonPress);
				if(state3.getText().equals("----"))
					checkState(State.state3, newGame.isChecked());
				else
					checkState(State.state3, newGame.isChecked());
				state3.toggle();
			}
		});
		state3.addListener(new ClickListener(Buttons.RIGHT)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				playSound(RBResources.sounds.buttonPress);
				Dialog d = new Dialog("Erase?", RBResources.styles.gameSkin);
				d.text("Would you like to erase this file?");
				TextButton yes = new TextButton("Yes", RBResources.styles.gameSkin), no = new TextButton("No",
						RBResources.styles.gameSkin);
				yes.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent e, float x, float y)
					{
						playSound(RBResources.sounds.buttonPress);
						RBResources.removeValue(RBResources.objectMemory, RBResources.States.State.state3);
						state3.setText("----");
						state3.pack();
					}
				});
				no.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent event, float x, float y)
					{
						playSound(RBResources.sounds.buttonPress);
					}
				});
				d.button(yes);
				d.button(no);
				d.show(state1.getStage());
			}
		});

		optionsGroup = new RBGroup();
		optionsGroup.setTransform(true);
		optionsGroup.addActor(sound);
		optionsGroup.addActor(vLabel);
		optionsGroup.addActor(volume);
		optionsGroup.setVisible(false);
		Color tmp = optionsGroup.getColor();
		optionsGroup.setColor(tmp.r, tmp.g, tmp.b, 0);
		optionsGroup.setPosition(newGame.getRight() + 50, options.getY() + options.getHeight() / 2 - optionsGroup.getHeight() / 2);

		stateGroup = new RBGroup();
		stateGroup.setTransform(true);
		stateGroup.setVisible(false);
		stateGroup.addActor(state1);
		stateGroup.addActor(state2);
		stateGroup.addActor(state3);
		tmp = stateGroup.getColor();
		stateGroup.setColor(tmp.r, tmp.g, tmp.b, 0);
		stateGroup.setPosition(optionsGroup.getX(),
				(newGame.getTop() - continueGame.getY()) / 2 + continueGame.getY() - stateGroup.getHeight() / 2);

		this.addActor(bg);
		this.addActor(newGame);
		this.addActor(continueGame);
		this.addActor(options);
		this.addActor(centerActorStage(this, title, true, false));
		this.addActor(optionsGroup);
		this.addActor(stateGroup);
	}

	public void performFunction(int func)
	{
		switch(func)
		{
			case 1:
				if(optionsGroup.isVisible())
				{
					optionsGroup.addAction(Actions.fadeOut(RBActor.FADE_FAST));
					options.setChecked(false);
				}
				if(continueGame.isChecked())
					continueGame.setChecked(false);
				else if(newGame.isChecked())
					stateGroup.addAction(Actions.fadeIn(RBActor.FADE_FAST));
				else
					stateGroup.addAction(Actions.fadeOut(RBActor.FADE_FAST));
				break;
			case 2:
				if(optionsGroup.isVisible())
				{
					optionsGroup.addAction(Actions.fadeOut(RBActor.FADE_FAST));
					options.setChecked(false);
				}
				if(newGame.isChecked())
					newGame.setChecked(false);
				else if(continueGame.isChecked())
					stateGroup.addAction(Actions.fadeIn(RBActor.FADE_FAST));
				else
					stateGroup.addAction(Actions.fadeOut(RBActor.FADE_FAST));
				break;
			case 3:
				newGame.setChecked(false);
				continueGame.setChecked(false);
				if(stateGroup.isVisible())
					stateGroup.addAction(Actions.fadeOut(RBActor.FADE_FAST));
				if(options.isChecked())
					optionsGroup.addAction(Actions.fadeIn(RBActor.FADE_FAST));
				else
					optionsGroup.addAction(Actions.fadeOut(RBActor.FADE_FAST));
				break;
			case 4:
				RBResources.sounds.isSoundOn = !RBResources.sounds.isSoundOn;
				String soundImg = RBResources.sounds.isSoundOn ? RBResources.images.buttonSound : RBResources.images.buttonSoundNo;
				sound.setSprite(soundImg);
		}
		playSound(RBResources.sounds.buttonPress);
	}

	private void checkState(final State s, boolean n)
	{
		Object o = RBResources.readValue(RBResources.objectMemory, s);

		if(o == null && n)
		{
			startNewGame(s);
		}
		else if(o != null && !n)
		{
			startGame(s);
		}
		else if(o == null && !n)
		{
			Dialog d = new Dialog("Error", RBResources.styles.gameSkin);
			d.text("You cannot continue an empty game.");
			TextButton b = new TextButton("Ok", RBResources.styles.gameSkin);
			b.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					playSound(RBResources.sounds.buttonPress);
				}
			});
			d.button(b);
			d.show(this);
		}
		else
		{
			Dialog d = new Dialog("Caution", RBResources.styles.gameSkin);
			d.text("This will overwrite the previous save data.\nAre you sure?");
			TextButton yes = new TextButton("Yes", RBResources.styles.gameSkin), no = new TextButton("No", RBResources.styles.gameSkin);
			yes.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					startNewGame(s);
					playSound(RBResources.sounds.buttonPress);
				}
			});
			no.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					playSound(RBResources.sounds.buttonPress);
				}
			});
			d.button(yes);
			d.button(no);
			d.show(this);
		}
	}

	private void startNewGame(State s)
	{
		ReverseBlade.transition(new RBStageIntro(s));
	}

	private void startGame(State s)
	{
		ReverseBlade.transition(new RBStageChapter(s));
	}

	@Override
	public void dispose()
	{
		while(RBResources.sounds.buttonPress.isPlaying())
			;// wait for sound to stop, if it's playing
		super.dispose();
	}
}