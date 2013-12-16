package com.thecherno.ld28.util;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sound {

	public final static int MENU = 0x0;
	public final static int GAME = 0x1;

	private static Audio menu, game;

	static {
		try {
			menu = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("audio/LostHope(Piano).ogg"));
			game = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("audio/LostHope.ogg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void play(int track) {
		if (track == MENU) {
			menu.playAsMusic(1.0f, 2.0f, true);
		} else if (track == GAME) {
			game.playAsMusic(1.0f, 2.0f, true);
		}
	}

}
