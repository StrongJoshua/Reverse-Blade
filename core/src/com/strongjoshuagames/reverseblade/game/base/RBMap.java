package com.strongjoshuagames.reverseblade.game.base;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.strongjoshuagames.reverseblade.extraFramework.FPoint;
import com.strongjoshuagames.reverseblade.extraFramework.RBDimension;
import com.strongjoshuagames.reverseblade.game.save.SaveState;
import com.strongjoshuagames.reverseblade.ui.actors.RBGroup;
import com.strongjoshuagames.reverseblade.ui.stages.RBStage;

public abstract class RBMap extends RBGroup implements Disposable
{
	protected RBStage stage;
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer renderer;
	private MapDragListener drag;
	private MapClickListener click;
	private RBDimension[] mapSize;
	protected Grid grid;
	protected SaveState state;
	private boolean justDragged;

	/**
	 * Creates a new RBMap with the given map file.
	 * 
	 * @param file The map file without the folder.
	 */
	protected RBMap(String file)
	{
		tileMap = new TmxMapLoader().load("maps/" + file);

		MapProperties prop = tileMap.getProperties();
		mapSize = new RBDimension[2];
		mapSize[0] = new RBDimension(prop.get("width", Integer.class), (prop.get("height", Integer.class)));
		mapSize[1] = new RBDimension(mapSize[0].width * prop.get("tilewidth", Integer.class), mapSize[0].height
				* prop.get("tileheight", Integer.class));

		grid = new Grid(this);
	}
	
	public void init(SaveState s)
	{
		state = s;
		
		renderer = new OrthogonalTiledMapRenderer(tileMap, stage.getBatch());
		renderer.setView((OrthographicCamera) stage.getCamera());

		drag = new MapDragListener();
		stage.addListener(drag);
		
		click = new MapClickListener();
		stage.addListener(click);

		stage.addActor(this);
		
		if(state.map == null)
			setUpMapState();
		addAllToGrid();
	}

	public void render()
	{
		keepCameraInMap();
		
		Color tmp = stage.getBatch().getColor();
		stage.getBatch().setColor(stage.getRoot().getColor());

		renderer.render();

		stage.getBatch().setColor(tmp);
	}

	/**
	 * Returns a Dimension array of size 2 of the map's size. The first entry is it's size in tiles, the second in pixels.
	 * 
	 * @return The map's size dimension array.
	 */
	public RBDimension[] getMapSize()
	{
		return mapSize;
	}

	public void setStage(RBStage s)
	{
		stage = s;
	}
	
	/**
	 * Set up the map's Map State.
	 * Overwrite this method in sub-classes to add units (by calling <i>addToGrid(Unit u, int x, int y)</i> among other map related things.
	 */
	public abstract void setUpMapState();
	
	private void addAllToGrid()
	{
		Array<RBClass> units = state.player.party.getUnits();
		for(RBClass u : units)
			grid.addUnit(u);
		
		units = state.map.foes.getUnits();
		for(RBClass u : units)
			grid.addUnit(u);
	}
	
	public TiledMap getTileMap()
	{
		return tileMap;
	}
	
	@Override
	public void dispose()
	{
		tileMap.dispose();
		grid.dispose();
	}

	/**
	 * Keeps the Camera in the TiledMap.
	 * 
	 * @return True if the Camera had to be adjusted, false otherwise.
	 */
	private boolean keepCameraInMap()
	{
		Camera c = stage.getCamera();
		float cHW = c.viewportWidth / 2;
		float cHH = c.viewportHeight / 2;
		float cL = c.position.x - cHW;
		float cR = cL + c.viewportWidth;
		float cB = c.position.y - cHH;
		float cT = cB + c.viewportHeight;

		float mL = 0;
		float mR = mL + mapSize[1].width;
		float mB = 0;
		float mT = mB + mapSize[1].height;

		float xPos;
		float yPos;
		
		boolean adjusted = true;

		if(cL < mL)
			xPos = mL + cHW;
		else if(cR > mR)
			xPos = mR - cHW;
		else
			xPos = c.position.x;
		
		if(cB < mB)
			yPos = mB + cHH;
		else if(cT > mT)
			yPos = mT - cHH;
		else
			yPos = c.position.y;
		
		if(xPos == c.position.x && yPos == c.position.y)
			adjusted = false;

		c.position.set(xPos, yPos, 0);
		c.update();

		renderer.setView((OrthographicCamera) c);
		
		return adjusted;
	}

	private class MapDragListener extends DragListener
	{
		private FPoint p;
		private Vector2 last;
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
		{
			p = new FPoint(x, y);
			return super.touchDown(event, x, y, pointer, button);
		}
		
		@Override
		public void drag(InputEvent event, float x, float y, int pointer)
		{
			stage.getCamera().translate(p.x - x, p.y - y, 0);
			
			grid.hideUI();
			justDragged = true;
			
			if(last == null)
			{
				last = new Vector2(x - p.x, y - p.y);
				return;
			}
			
			Vector2 v = new Vector2(x - (p.x + last.x), y - (p.y + last.y));
			
			if(((last.x * -1 >= 0) == (v.x >= 0)) || ((last.y * -1 >= 0) == (v.y >= 0)) && keepCameraInMap())
				p.setLocation(x, y);
			
			last = null;
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			super.touchUp(event, x, y, pointer, button);
			p = null;
			last = null;
		}
	}
	
	private class MapClickListener extends ClickListener
	{
		@Override
		public void clicked (InputEvent event, float x, float y)
		{
			if(justDragged)
			{
				justDragged = false;
				return;
			}
			grid.processClick(x, y);
		}
	}

	public void setHealthAll(int health) {
		grid.setHealthAll(health);
	}
}