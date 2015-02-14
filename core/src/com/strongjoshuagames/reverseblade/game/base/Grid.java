package com.strongjoshuagames.reverseblade.game.base;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.strongjoshuagames.reverseblade.extraFramework.FPoint;
import com.strongjoshuagames.reverseblade.extraFramework.MovementPath;
import com.strongjoshuagames.reverseblade.extraFramework.RBDimension;
import com.strongjoshuagames.reverseblade.game.base.RBTileInfo.Tint;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor;
import com.strongjoshuagames.reverseblade.ui.actors.RBActor.Direction;
import com.strongjoshuagames.reverseblade.ui.base.RBResources;
import com.strongjoshuagames.reverseblade.ui.base.ReverseBlade;
import com.strongjoshuagames.reverseblade.ui.misc.RBInfoBox;
import com.strongjoshuagames.reverseblade.ui.misc.RBMultiButton;
import com.strongjoshuagames.reverseblade.ui.stages.RBStageMainMenu;

public class Grid implements Disposable
{
	private RBMap map;
	private RBTileInfo[][] tileInfos;
	private int tileSizeX, tileSizeY;
	private RBActor selector;
	private RBInfoBox infoBox;
	private RBMultiButton mButton;
	private RBMultiButtonListener mButtonListener;
	private final String[] tileOptions = new String[] {"End Turn", "Main Menu", "Cancel"};
	private final String[] unitOptions = new String[] {"Move", "Stats"};
	private final String[] unitMovedOptions = new String[] {"Stats"};
	private final String[] movePhaseOptions = new String[] {"Wait", "Undo"};
	private boolean moveUnitPhase, waitingForConfirmMove;
	private RBClass movingUnit;
	private StatScreen stat;

	public Grid(RBMap map)
	{
		this.map = map;

		RBDimension[] tmp = map.getMapSize();

		tileSizeX = tmp[1].width / tmp[0].width;
		tileSizeY = tmp[1].height / tmp[0].height;

		tileInfos = new RBTileInfo[tmp[0].width][tmp[0].height];

		initTiles(tileInfos, map.getTileMap());

		selector = new RBActor();
		selector.setSprite("images/Selector.png");
		selector.setLoopingFade(RBActor.FADE_SLOW);
		map.addActor(selector);
		selector.setVisible(false);

		infoBox = new RBInfoBox();
		map.addActor(infoBox);
		infoBox.setVisible(false);

		mButtonListener = new RBMultiButtonListener();

		mButton = new RBMultiButton(mButtonListener);
		map.addActor(mButton);
		mButton.setVisible(false);
		
		stat = new StatScreen(null);
		map.addActor(stat);
		stat.setVisible(false);
	}

	/**
	 * Adds a unit to the map.
	 * 
	 * @param u The unit to be added.
	 * @param x The x tile on the map.
	 * @param y The y tile on the map.
	 */
	public void addUnit(RBClass u, int x, int y)
	{
		u.setPosition(x * tileSizeX, y * tileSizeY);
		addToGrid(tileInfos, u, x, y);
		u.resetSprite();
		map.addActor(u);
	}

	/**
	 * Adds a Unit to the grid, converting the x and y position into tile position.
	 * 
	 * @param u The unit to be added.
	 */
	public void addUnit(RBClass u)
	{
		float x = u.getX();
		float y = u.getY();

		x /= tileSizeX;
		y /= tileSizeY;

		addUnit(u, (int) x, (int) y);
	}

	public int convertToPixelsX(int tile)
	{
		return tile * tileSizeX;
	}

	public int convertToPixelsY(int tile)
	{
		return tile * tileSizeY;
	}

	public int convertToTilesX(float x)
	{
		return (int) (x / tileSizeX);
	}

	public int convertToTilesY(float y)
	{
		return (int) (y / tileSizeY);
	}

	private void addToGrid(RBTileInfo[][] os, RBClass u, int x, int y)
	{
		if(os[x][y].getUnit() != null)
			throw new IllegalArgumentException("(" + x + ", " + y + ") is already occupied.");
		else
			os[x][y].setUnit(u);
		;
	}

	public int[] getTotalTiles()
	{
		return new int[] {tileInfos.length - 1, tileInfos[0].length - 1};
	}

	public int getMapWidth()
	{
		return tileInfos.length - 1;
	}

	public int getMapHeight()
	{
		return tileInfos[0].length - 1;
	}

	private void initTiles(RBTileInfo[][] i, TiledMap t)
	{
		TiledMapTileLayer l = (TiledMapTileLayer) t.getLayers().get(0);
		MapProperties prop = null;

		for(int x = 0; x < i.length; x++)
		{
			for(int y = 0; y < i[x].length; y++)
			{
				prop = l.getCell(x, y).getTile().getProperties();

				int m = Integer.parseInt((String) prop.get("Movement Cost"));
				String n = (String) prop.get("Name");

				tileInfos[x][y] = new RBTileInfo(n, m);
				tileInfos[x][y].setPosition(x * tileSizeX, y * tileSizeY);
				tileInfos[x][y].setVisible(false);
				tileInfos[x][y].toFront();
				map.addActor(tileInfos[x][y]);
			}
		}
	}

	public void processClick(float x, float y)
	{
		if((mButton.isVisible() && new Rectangle(mButton.getX(), mButton.getY(), mButton.getWidth(), mButton.getHeight()).contains(x, y))
				|| mButton.justCanceled() || waitingForConfirmMove)
			return;
		if(stat.isVisible())
		{
			stat.processClick(x, y);
			return;
		}

		int tX = convertToTilesX(x);
		int tY = convertToTilesY(y);

		// //////
		boolean processedMovePhase = true;
		if(this.isInMoveUnitPhase())
		{
			processedMovePhase = processMovementPhase(tX, tY);
			if(processedMovePhase)
			{
				mButton.setButtons(movePhaseOptions);
				waitingForConfirmMove = true;
			}
		}
		else
		{
			if(this.containsUnit(tX, tY))
			{
				if(!this.getUnit(tX, tY).hasMoved())
					mButton.setButtons(RBResources.concatArrays(unitOptions, tileOptions));
				else
					mButton.setButtons(RBResources.concatArrays(unitMovedOptions, tileOptions));
			}
			else
				mButton.setButtons(tileOptions);
		}

		if(processedMovePhase)
		{
			Camera c = this.map.stage.getCamera();
			
			resetSelectorAndInfoBox(x, y);
			// //////
			mButton.toFront();
			mButton.resetPosition(leftRight(c, x), upDown(c, y), tX, tY, tileSizeX, tileSizeY);
			mButton.setVisible(true);
			// //////
			map.stage.playSound(RBResources.sounds.buttonPress);
		}
	}
	
	public static Direction leftRight(Camera c, float x)
	{
		Direction d;
		if(x - c.position.x <= 0)
			d = RBActor.Direction.RIGHT;
		else
			d = RBActor.Direction.LEFT;
		
		return d;
	}
	
	public static Direction upDown(Camera c, float y)
	{
		Direction d;
		if(y - c.position.y <= 0)
			d = RBActor.Direction.UP;
		else
			d = RBActor.Direction.DOWN;
		
		return d;
	}

	public void resetSelectorAndInfoBox(float x, float y)
	{
		Camera c = this.map.stage.getCamera();
		this.resetSelectorAndInfoBox(leftRight(c, x), this.convertToTilesX(x), this.convertToTilesY(y));
	}

	public void resetSelectorAndInfoBox(RBActor.Direction d, int tX, int tY)
	{
		selector.toFront();
		selector.setVisible(true);
		selector.setPosition(tX * tileSizeX, tY * tileSizeY);
		selector.restartLoop();
		// //////
		infoBox.setText(tileInfos[tX][tY].getName(), "Movement Cost: " + tileInfos[tX][tY].getMovementCost());
		infoBox.setVisible(true);
		infoBox.toFront();
		infoBox.resetPosition(d, map.stage.getCamera());
	}

	public RBClass getUnit(int tX, int tY)
	{
		return tileInfos[tX][tY].getUnit();
	}

	public void hideUI()
	{
		infoBox.setVisible(false);
		if(!waitingForConfirmMove)
			mButton.setVisible(false);
		selector.setVisible(mButton.isVisible());
		stat.setVisible(false);
	}

	public boolean containsUnit(int tileX, int tileY)
	{
		if(tileInfos[tileX][tileY].getUnit() != null)
			return true;
		else
			return false;
	}

	public boolean isInMoveUnitPhase()
	{
		return moveUnitPhase;
	}

	public FPoint getUnitTile(RBClass u)
	{
		return new FPoint(convertToTilesX(u.getX()), convertToTilesY(u.getY()));
	}

	public void createMovePhase(RBClass u)
	{
		if(this.isInMoveUnitPhase())
			return;

		moveUnitPhase = true;
		movingUnit = u;
		int[][] result = new int[tileInfos.length][tileInfos[0].length];
		recursiveSearch(tileInfos, result, null, this.getUnitTile(u), u.getMoves(), 1);

		for(int x = 0; x < result.length; x++)
		{
			for(int y = 0; y < result[x].length; y++)
			{
				int i = result[x][y];

				if(i == 1)
					tileInfos[x][y].setTint(Tint.BLUE);
				else if(i == 2)
					tileInfos[x][y].setTint(Tint.RED);

				if(i != 0)
					tileInfos[x][y].setVisible(true);
			}
		}
	}

	public boolean processMovementPhase(int x, int y)
	{
		if(tileInfos[x][y].getTint() == RBTileInfo.Tint.BLUE)
		{
			this.moveUnit(movingUnit, x, y);
			this.hideAllTints();
			this.resetAllPaths();
			return true;
		}
		return false;
	}

	/**
	 * Moves the unit to the new tile. Throws an IllegalArgumentException if the given unit is not in the grid.
	 * 
	 * @param u The unit to be moved.
	 * @param x The x location of the tile to be moved to.
	 * @param y The y location of the tile to be moved to.
	 */
	public void moveUnit(RBClass u, int x, int y)
	{
		for(int i = 0; i < tileInfos.length; i++)
		{
			for(int j = 0; j < tileInfos[i].length; j++)
			{
				if(tileInfos[i][j].getUnit() == u)
				{
					movingUnit.setPreviousTile(movingUnit.getCurrentTile());
					tileInfos[i][j].setUnit(null);
					tileInfos[x][y].setUnit(u);
					return;
				}
			}
		}
		throw new IllegalArgumentException("Unit is not in the grid.");
	}

	public void endMovingPhase()
	{
		moveUnitPhase = false;
		waitingForConfirmMove = false;
		if(movingUnit.getPreviousTile() != movingUnit.getCurrentTile())
			movingUnit.setHasMoved(true);
	}

	public void hideAllTints()
	{
		for(int x = 0; x < tileInfos.length; x++)
		{
			for(int y = 0; y < tileInfos[x].length; y++)
			{
				tileInfos[x][y].setVisible(false);
			}
		}
	}
	
	public void resetAllPaths()
	{
		for(int x = 0; x < tileInfos.length; x++)
		{
			for(int y = 0; y < tileInfos[x].length; y++)
			{
				tileInfos[x][y].setPath(null);;
			}
		}
	}

	public int[][] getMovementCostArray()
	{
		int[][] ms = new int[tileInfos.length][tileInfos[0].length];

		for(int x = 0; x < tileInfos.length; x++)
		{
			for(int y = 0; y < tileInfos[x].length; y++)
			{
				ms[x][y] = tileInfos[x][y].getMovementCost();
			}
		}

		return ms;
	}

	/***
	 * Use this method in a tile set game with movement cost spaces; like games in the Fire Emblem series.
	 * Note that the exception catches are to prevent searching outside of the map.
	 * 
	 * @param t The array of tiles.
	 * @param r The resulting array of possible moves; should be the same size as 't'. The resulting numbers are 0, for impassable, 1 for
	 *            passable, and 2, for the attack range.
	 * @param mp The unit's MovementPath object (used for recursion, leave null)
	 * @param p The unit's location.
	 * @param m The unit's amount of moves.
	 * @param ra The unit's range (0 if they cannot attack).
	 */
	private static void recursiveSearch(RBTileInfo[][] t, int[][] r, MovementPath mp, FPoint p, int m, int ra)
	{
		int x = (int) p.x;
		int y = (int) p.y;
		r[x][y] = 1;
		if(mp == null)
		{
			mp = new MovementPath(m);
			t[x][y].setPath(mp);
		}
		else
		{
			mp = new MovementPath(m, RBResources.concatArrays(mp.getPoints(), new FPoint[]{new FPoint(x, y)}));
			if(!t[x][y].setPath(mp))
				return;
		}
		
		// check for range
		recursiveRangeSearch(r, new FPoint(x, y), ra);
		
		if(m == 0)
		{
			return;
		}
		try
		{
			if(m - t[x + 1][y].getMovementCost() >= 0)
			{
				recursiveSearch(t, r, mp, new FPoint(x + 1, y), m - t[x + 1][y].getMovementCost(), ra);
			}
		} catch(Exception e)
		{}
		try
		{
			if(m - t[x - 1][y].getMovementCost() >= 0)
			{
				recursiveSearch(t, r, mp, new FPoint(x - 1, y), m - t[x - 1][y].getMovementCost(), ra);
			}
		} catch(Exception e)
		{}
		try
		{
			if(m - t[x][y + 1].getMovementCost() >= 0)
			{
				recursiveSearch(t, r, mp, new FPoint(x, y + 1), m - t[x][y + 1].getMovementCost(), ra);
			}
		} catch(Exception e)
		{}
		try
		{
			if(m - t[x][y - 1].getMovementCost() >= 0)
			{
				recursiveSearch(t, r, mp, new FPoint(x, y - 1), m - t[x][y - 1].getMovementCost(), ra);
			}
		} catch(Exception e)
		{}
	}
	
	/***
	 * To be called by the recursive movement search method; should not be used individually.
	 * @param moveA The movement array as created by the recursive movement search method.
	 * @param p The current search point.
	 * @param ra The unit's weapon's range.
	 */
	private static void recursiveRangeSearch(int[][] moveA, FPoint p, int ra)
	{
		int x = (int) p.x;
		int y = (int) p.y;
		
		if(moveA[x][y] == 0)
			moveA[x][y] = 2;
		
		if(ra <= 0)
			return;

		try {
			recursiveRangeSearch(moveA, new FPoint(x + 1, y), ra - 1);
		}
		catch(Exception e)
		{}
		
		try {
			recursiveRangeSearch(moveA, new FPoint(x - 1, y), ra - 1);
		}
		catch(Exception e)
		{}
		
		try {
			recursiveRangeSearch(moveA, new FPoint(x, y + 1), ra - 1);
		}
		catch(Exception e)
		{}
		
		try {
			recursiveRangeSearch(moveA, new FPoint(x, y - 1), ra - 1);
		}
		catch(Exception e)
		{}
	}

	/**
	 * Calls resetPosition() for all units in the grid.
	 */
	public void resetUnitTiles()
	{
		RBClass[] us = this.getUnits();
		for(RBClass u : us)
			resetToPreviousTile(u);
	}

	public static void resetToPreviousTile(RBClass u)
	{
		if(u.getPreviousTile() == null)
			return;
		u.getCurrentTile().setUnit(null);
		u.getPreviousTile().setUnit(u);
	}

	public void endTurn()
	{
		RBClass[] us = getUnits();
		for(RBClass u : us)
			u.setHasMoved(false);
	}

	public RBClass[] getUnits()
	{
		Array<RBClass> a = new Array<RBClass>();

		for(RBTileInfo[] j : tileInfos)
			for(RBTileInfo i : j)
			{
				RBClass u = i.getUnit();
				if(u != null)
					a.add(u);
			}

		return a.toArray(RBClass.class);
	}

	@Override
	public void dispose()
	{
		if(moveUnitPhase)
			resetToPreviousTile(movingUnit);
	}

	private class RBMultiButtonListener extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			String s = mButton.getButtonTexts()[mButton.getCheckedButton()];
			mButton.uncheckAll();

			// Check tile options
			if(s.equals("End Turn"))
			{
				endTurn();
			}
			
			else if(s.equals("Main Menu"))
				ReverseBlade.transition(new RBStageMainMenu());

			// Check unit options
			else if(s.equals("Move"))
			{
				FPoint p = mButton.getTile();
				createMovePhase(tileInfos[(int) p.x][(int) p.y].getUnit());
			}

			else if(s.equals("Stats"))
			{
				FPoint p = mButton.getTile();
				stat.setUnit(tileInfos[(int) p.x][(int) p.y].getUnit());
				stat.setVisible(true);
				stat.toFront();
			}

			// Check unit movement options
			else if(s.equals("Wait"))
			{
				endMovingPhase();
			}
			// Undo
			else if(s.equals(movePhaseOptions[1]))
			{
				resetToPreviousTile(movingUnit);
				endMovingPhase();
				createMovePhase(movingUnit);
				resetSelectorAndInfoBox(movingUnit.getX(), movingUnit.getY());
			}

			// Check conditional unit options

			// Play button press sound
			map.stage.playSound(RBResources.sounds.buttonPress);
			// Close the RBMultiButton
			mButton.cancel();
		}
	}

	public void setHealthAll(int health) {
		RBClass[] units = getUnits();
		for(int i = 0; i < units.length; i++) {
			units[i].setMaxHealth(health);
			units[i].setHealth(health);
		}
	}
}