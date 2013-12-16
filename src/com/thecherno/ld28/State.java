package com.thecherno.ld28;

import com.thecherno.ld28.util.Sound;

public class State {

	public final static int MENU = 0x0;
	public final static int GAME = 0x1;

	private static int currentState;

	static {
		setState(MENU);
	}

	public static void setState(int state) {
		if (state == MENU) Sound.play(Sound.MENU);
		else if (state == GAME) Sound.play(Sound.GAME);
		State.currentState = state;
	}

	public static int getState() {
		return currentState;
	}

}
