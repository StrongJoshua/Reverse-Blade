package com.strongjoshuagames.reverseblade.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.strongjoshuagames.reverseblade.ui.base.ReverseBlade;

public class AndroidLauncher extends AndroidApplication
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ReverseBlade(), config);
	}
}