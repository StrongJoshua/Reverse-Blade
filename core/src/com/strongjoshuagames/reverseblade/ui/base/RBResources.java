package com.strongjoshuagames.reverseblade.ui.base;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.strongjoshuagames.reverseblade.game.base.Chapter;
import com.strongjoshuagames.reverseblade.game.save.SaveState;

public class RBResources
{
	public static Images images;
	public static Sound sounds;
	public static Text text;
	public static States states;
	public static Styles styles;

	public static BitmapFont fontTitle, fontText;
	public static final int TEXT_SIZE = 48, TITLE_SIZE = 128;
	public static String objectMemory = "memory/savedata";

	private static final String fontFile = "font/RPG-Pixel-Font.ttf";
	private static Preferences pref;
	private static Json json;
	private static Object endObject = new String("!end!");

	/**
	 * Initializes necessary objects for the application. <b>Must</b> be called at application initialization.
	 */
	public static void initResources()
	{
		pref = Gdx.app.getPreferences("com.strongjoshuagames.reverseblade.settings");
		images = new Images();
		sounds = new Sound();
		text = new Text();
		states = new States();
		styles = new Styles();
		json = new Json();

		FreeTypeFontGenerator g = new FreeTypeFontGenerator(Gdx.files.internal(RBResources.fontFile));
		FreeTypeFontParameter p = new FreeTypeFontParameter();
		p.size = TITLE_SIZE;
		fontTitle = g.generateFont(p);
		p.size = TEXT_SIZE;
		fontText = g.generateFont(p);

		// clear saves in case of error on startup
		// Gdx.files.local(objectMemory).delete();
	}

	/**
	 * Disposes global assets and saves global values. <b>Must</b> be called at application termination.
	 */
	public static void dispose()
	{
		pref.putBoolean("Sound", sounds.isSoundOn);
		pref.putFloat("Volume", sounds.volume);
		pref.flush();
		styles.dispose();
		sounds.dispose();
	}

	/**
	 * Writes an object to memory, using JSON formatting.
	 * 
	 * @param token The token the object should be stored with.
	 * @param o The object to be written.
	 */
	public static void writeObject(Object token, Object o)
	{
		String s = json.prettyPrint(o);
		writeValue(objectMemory, token, s);
	}

	/**
	 * Reads an object from memory.
	 * 
	 * @param c The class type of the object.
	 * @param token The token the object was stored with.
	 * @return The object.
	 */
	public static Object readObject(Class<?> c, Object token)
	{
		Object o = readValue(objectMemory, token);
		if(o == null)
			return null;

		String s = (String) o;
		return json.fromJson(c, s);
	}

	/**
	 * Reads a specific value from memory.
	 * 
	 * @param file The file to read from (done for efficiency).
	 * @param token The token assigned to the value.
	 * @return The value.
	 */
	public static Object readValue(String file, Object token)
	{
		FileHandle fh = Gdx.files.local(file);

		if(fh.length() == 0)
			return null;

		try
		{
			ObjectInputStream oi = new ObjectInputStream(fh.read());

			while(true)
			{
				Object o = oi.readObject();
				if(o.equals(endObject))
					break;
				Object v = oi.readObject();
				if(o.equals(token))
				{
					oi.close();
					return v;
				}
			}
			oi.close();
			return null;
		} catch(Exception e)
		{
			System.out.println("Could not read.\n");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Writes a specific value to memory.
	 * 
	 * @param file The file to write the value to (done for efficiency).
	 * @param token The token the value is to be assigned to.
	 * @param val The value to be written.
	 */
	public static void writeValue(String file, Object token, Object val)
	{
		FileHandle fh = Gdx.files.local(file);
		FileHandle tmp = FileHandle.tempFile("tmp");

		boolean exists = false;

		ObjectOutputStream oo = null;
		ObjectInputStream oi = null;

		try
		{
			oo = new ObjectOutputStream(tmp.write(false));

			if(fh.length() != 0)
			{
				oi = new ObjectInputStream(fh.read());

				while(true)
				{
					Object o = oi.readObject();
					if(o.equals(endObject))
					{
						if(exists)
							oo.writeObject(endObject);
						break;
					}
					Object v = oi.readObject();
					if(o.equals(token))
					{
						exists = true;
						v = val;
					}
					oo.writeObject(o);
					oo.writeObject(v);
				}
				oi.close();
			}

			if(!exists)
			{
				oo.writeObject(token);
				oo.writeObject(val);
				oo.writeObject(endObject);
			}

			oo.close();

			tmp.copyTo(fh);
		} catch(Exception e)
		{
			System.out.println("Could not save.");
			System.out.println();
			e.printStackTrace();
		}
	}

	/**
	 * Removes a value from the specified memory file.
	 * 
	 * @param file The file to be accessed.
	 * @param token The token for the value to be removed (is removed as well).
	 */
	public static void removeValue(String file, Object token)
	{
		FileHandle fh = Gdx.files.local(file);
		FileHandle tmp = FileHandle.tempFile("tmp");

		ObjectOutputStream oo = null;
		ObjectInputStream oi = null;

		try
		{
			oo = new ObjectOutputStream(tmp.write(false));

			if(fh.length() != 0)
			{
				oi = new ObjectInputStream(fh.read());

				while(true)
				{
					Object o = oi.readObject();
					if(o.equals(endObject))
					{
						oo.writeObject(endObject);
						break;
					}
					else if(!o.equals(token))
					{
						oo.writeObject(o);
						oo.writeObject(oi.readObject());
					}
					else
						oi.readObject();
				}
				oi.close();
			}
			oo.close();
			tmp.copyTo(fh);
		} catch(Exception e)
		{
			System.out.println("Could not remove.\n");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static Object classFromString(String s)
	{
		try
		{
			return ClassReflection.forName(s).getDeclaredConstructor().newInstance();
		} catch(Exception e)
		{
			System.out.println("Couldn't create class: " + s);
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T[] concatArrays(T[] a1, T[] a2)
	{
		@SuppressWarnings("unchecked")
		T[] both = (T[]) Array.newInstance(a1.getClass().getComponentType(), a1.length + a2.length);

		for(int i = 0; i < a1.length; i++)
			both[i] = a1[i];

		for(int i = 0; i < a2.length; i++)
			both[i + a1.length] = a2[i];

		return both;
	}

	public static class Images
	{
		public final String backgroundMainMenu = "images/tempMainMenuBackground.png";
		public final String backgroundIntro = "images/IntroBG.png";
		public final String buttonSound = "images/SoundButton.png";
		public final String buttonSoundNo = "images/SoundButtonNo.png";
	}

	public static class Sound implements Disposable
	{
		public boolean isSoundOn;
		public float volume;
		private final String buttonPressed = "sounds/ButtonPressed.wav";
		private final float buttonPressedDur = .2f;
		public RBSound buttonPress;

		public Sound()
		{
			isSoundOn = pref.getBoolean("Sound", true);
			volume = pref.getFloat("Volume", 50);
			buttonPress = new RBSound(buttonPressed, buttonPressedDur);
		}

		@Override
		public void dispose()
		{
			buttonPress.dispose();
		}
	}

	public static class Text
	{
		public final String intro = "text/intro.txt";
		public final String classDescriptions = "text/classDescriptions.txt";
		public final String statDescriptions = "text/statDescriptions.txt";

		public String[] getLines(String file)
		{
			FileHandle fh = Gdx.files.internal(file);

			if(fh.length() == 0)
				return null;

			String total = fh.readString();
			return total.split("\\r?\\n");
		}
	}

	public static class States
	{
		public static enum State
		{
			state1, state2, state3
		}

		public String[] getStatesChapterNames()
		{
			Object o1 = readObject(SaveState.class, State.state1);
			String s1 = o1 == null ? "----" : ((Chapter) classFromString("com.strongjoshuagames.reverseblade.game.chapters."
					+ ((SaveState) o1).player.chapter)).getName();
			Object o2 = readObject(SaveState.class, State.state2);
			String s2 = o2 == null ? "----" : ((Chapter) classFromString("com.strongjoshuagames.reverseblade.game.chapters."
					+ ((SaveState) o2).player.chapter)).getName();
			Object o3 = readObject(SaveState.class, State.state3);
			String s3 = o3 == null ? "----" : ((Chapter) classFromString("com.strongjoshuagames.reverseblade.game.chapters."
					+ ((SaveState) o3).player.chapter)).getName();

			return new String[] {s1, s2, s3};
		}
	}

	public static class Styles implements Disposable
	{
		public Skin gameSkin;
		public TextButtonStyle textButtonStyle, textLargeButtonStyle, titleButtonStyle;
		public LabelStyle titleLabelStyle, gameTitleLabelStyle, infoBoxLabelStyle;

		public Styles()
		{
			gameSkin = new Skin(Gdx.files.internal("skins/gameskin.json"));

			textButtonStyle = gameSkin.get("text", TextButtonStyle.class);

			titleButtonStyle = gameSkin.get("title", TextButtonStyle.class);

			titleLabelStyle = gameSkin.get("title", LabelStyle.class);

			textLargeButtonStyle = gameSkin.get("text", TextButtonStyle.class);
			textLargeButtonStyle.font = gameSkin.getFont("textLarge-font");

			gameTitleLabelStyle = gameSkin.get("gameTitle", LabelStyle.class);

			infoBoxLabelStyle = gameSkin.get("info", LabelStyle.class);
		}

		@Override
		public void dispose()
		{
			gameSkin.dispose();
		}
	}
}