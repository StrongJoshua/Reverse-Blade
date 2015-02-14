package com.strongjoshuagames.reverseblade.game.base;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.strongjoshuagames.reverseblade.game.base.RBClass.Stats;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor.Direction;
import com.strongjoshuagames.reverseblade.ui.actors.RBGroup;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;

public class StatScreen extends RBGroup
{
	private final Stats[] stats = Stats.values();
	private RBClass unit;
	private Table statTable;
	private LabelStyle labelStyle;
	private Label nameLabel, helpLabel, levelLabel;
	private RBActor background;

	/**
	 * Creates a new stat screen for the specified unit. Does not draw anything if the unit is null.
	 * 
	 * @param u The unit.
	 */
	public StatScreen(RBClass u)
	{
		this.setTouchable(Touchable.childrenOnly);
		statTable = new Table();
		labelStyle = new LabelStyle(RBResources.styles.infoBoxLabelStyle.font, Color.WHITE);
		nameLabel = new Label("", labelStyle);
		helpLabel = new Label("", RBResources.styles.gameSkin.get("help", LabelStyle.class));
		levelLabel = new Label("", labelStyle);
		background = new RBActor();
		
		helpLabel.setVisible(false);
		background.setSprite("images/StatScreen.png");
		setUnit(u);

		this.addActor(background);
		this.addActor(statTable);
		this.addActor(nameLabel);
		this.addActor(helpLabel);
		this.addActor(levelLabel);
		this.setTransform(true);

		statTable.setX(450);
		nameLabel.setX(110);
		levelLabel.setX(110);
	}

	/**
	 * Resets the StatScreen to reflect the given Unit.
	 * 
	 * @param u The given Unit.
	 */
	public void setUnit(RBClass u)
	{
		unit = u;
		if(u == null)
		{
			this.setVisible(false);
			return;
		}

		statTable.clear();

		statTable.pad(0);

		int[] unitStats = unit.getStats();
		String s = "";
		for(int i = 0; i < 4; i++)
		{
			s = stats[i] + ": " + unitStats[i];
			if(i == 0)
				s += "/" + unit.getHealth();
			statTable.add(new Label(s, labelStyle)).padRight(150).expand().fill();

			s = stats[(stats.length - 1) - 3 + i] + ": " + unitStats[(unitStats.length - 1) - 3 + i];
			statTable.add(new Label(s, labelStyle)).expand().fill().row();
		}

		statTable.setSize(statTable.getPrefWidth(), statTable.getPrefHeight());

		nameLabel.setText(unit.getName());
		nameLabel.setSize(nameLabel.getPrefWidth(), nameLabel.getPrefHeight());
		
		levelLabel.setText("Level: " + unit.getLevel());
		levelLabel.setSize(levelLabel.getPrefWidth(), levelLabel.getPrefHeight());
		
		statTable.setY(background.getHeight() - 220);
		nameLabel.setY(background.getHeight() - nameLabel.getHeight() - 5);
		levelLabel.setY(nameLabel.getY() - levelLabel.getHeight());
	}

	public void processClick(float x, float y)
	{
		helpLabel.setVisible(false);
		
		Actor a = this.hit(x - this.getX(), y - this.getY(), true);

		if(a == null)
			return;

		Label l;

		try
		{
			l = (Label) a;
		} catch(ClassCastException e)
		{
			return;
		}

		String help = null;

		for(int i = 0; i < stats.length; i++)
		{
			String s = l.getText() + "";
			if(s.startsWith(stats[i].toString()))
			{
				help = stats[i].getStatDescription();
			}
		}
		
		if(l.getText().toString().equals(unit.getName()))
			help = unit.getUnitClass().getClassDescription();
		
		if(help != null)
			showHelp(x, y, help);
	}
	
	private void showHelp(float x, float y, String msg)
	{
		helpLabel.setText(msg);
		helpLabel.setVisible(true);
		helpLabel.setSize(helpLabel.getPrefWidth(), helpLabel.getPrefHeight());
		resetHelpLabelPosition(x, y);
	}
	
	private void resetHelpLabelPosition(float x, float y)
	{
		Camera c = this.getStage().getCamera();
		
		Direction d = Grid.leftRight(c, x);
		Direction d2 = Grid.upDown(c, y);
		
		if(d.equals(Direction.LEFT))
			x -= helpLabel.getWidth();
		if(d2.equals(Direction.DOWN))
			y -= helpLabel.getHeight();

		Vector2 v = this.parentToLocalCoordinates(new Vector2(x, y));
		
		helpLabel.setPosition(v.x, v.y);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if(unit == null)
			return;

		super.draw(batch, parentAlpha);
	}

	@Override
	public void setVisible(boolean b)
	{
		super.setVisible(b);

		if(unit == null || !b)
			return;

		Camera c = unit.getStage().getCamera();

		this.setPosition(c.position.x - this.getWidth() / 2, c.position.y - this.getHeight() / 2);
	}
}