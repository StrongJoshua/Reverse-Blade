package com.strongjoshuagames.reverseblade.ui.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;

public class RBSound implements Sound
{
	private Sound sound;
	private long start, pause, duration;
	private boolean playing;

	/**
	 * Creates a new Sound that can be checked to see if it is still playing.
	 * 
	 * @param s The String path of the sound.
	 * @param duration The duration, in seconds, of the sound.
	 */
	public RBSound(String s, float duration)
	{
		sound = Gdx.audio.newSound(Gdx.files.internal(s));

		this.duration = (long) (1000000000 * duration);
		
		playing = false;
	}

	@Override
	public long play()
	{
		start = TimeUtils.nanoTime();
		playing = true;
		return sound.play();
	}

	@Override
	public long play(float volume)
	{
		start = TimeUtils.nanoTime();
		playing = true;
		return sound.play(volume);
	}

	@Override
	public long play(float volume, float pitch, float pan)
	{
		start = TimeUtils.nanoTime();
		playing = true;
		return sound.play(volume, pitch, pan);
	}

	@Override
	public long loop()
	{
		start = TimeUtils.nanoTime();
		playing = true;
		return sound.loop();
	}

	@Override
	public long loop(float volume)
	{
		start = TimeUtils.nanoTime();
		playing = true;
		return sound.loop(volume);
	}

	@Override
	public long loop(float volume, float pitch, float pan)
	{
		start = TimeUtils.nanoTime();
		playing = true;
		return sound.loop(volume, pitch, pan);
	}

	@Override
	public void stop()
	{
		playing = false;
		sound.stop();
	}

	@Override
	public void pause()
	{
		playing = false;
		pause = TimeUtils.nanoTime();
		sound.pause();
	}

	@Override
	public void resume()
	{
		playing = true;
		start = TimeUtils.nanoTime() - (pause - start);
		sound.resume();
	}

	@Override
	public void dispose()
	{
		sound.dispose();
	}

	@Override
	public void stop(long soundId)
	{
		sound.stop(soundId);
	}

	@Override
	public void pause(long soundId)
	{
		sound.pause(soundId);
	}

	@Override
	public void resume(long soundId)
	{
		sound.resume(soundId);
	}

	@Override
	public void setLooping(long soundId, boolean looping)
	{
		sound.setLooping(soundId, looping);
	}

	@Override
	public void setPitch(long soundId, float pitch)
	{
		sound.setPitch(soundId, pitch);
	}

	@Override
	public void setVolume(long soundId, float volume)
	{
		sound.setVolume(soundId, volume);
	}

	@Override
	public void setPan(long soundId, float pan, float volume)
	{
		sound.setPan(soundId, pan, volume);
	}

	@Override
	public void setPriority(long soundId, int priority)
	{
		sound.setPriority(soundId, priority);
	}

	/**
	 * 
	 * @return Returns if the song is playing, taking into consideration it's duration.
	 */
	public boolean isPlaying()
	{
		if(playing)
		{
			playing = start + duration < TimeUtils.nanoTime() ? false : true;
		}
		return playing;
	}
}