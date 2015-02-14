package com.strongjoshuagames.reverseblade.game.base;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class Inventory implements Serializable
{
	private Item[] items;

	public Inventory()
	{
		items = new Item[5];
	}

	public Item[] getItems()
	{
		return items;
	}

	/**
	 * Adds an item to the inventory.
	 * 
	 * @param item The item to be added.
	 * @throws IllegalArgumentException if the inventory was already full.
	 */
	public void addItem(Item item)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] == null)
			{
				items[i] = item;
				return;
			}
		}

		throw new IllegalArgumentException("Inventory is full.");
	}

	/**
	 * Clears the inventory.
	 * 
	 * @return The inventory's previous contents.
	 */
	public Item[] clear()
	{
		Item[] tmp = items;
		items = new Item[5];
		return tmp;
	}

	@Override
	public void write(Json json)
	{
		json.writeValue("Items", items);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		json.readValue(Item[].class, jsonData.child().child());
	}
}