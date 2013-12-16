package com.thecherno.ld28.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class Key {

	private static List<Integer> pressedKeys = new ArrayList<Integer>();

	public static boolean typed(int key) {
		boolean pressed = Keyboard.isKeyDown(key);
		if (!pressed) {
			if (pressedKeys.contains(key)) {
				pressedKeys.remove(new Integer(key));
			}
			return false;
		}
		if (pressedKeys.contains(key)) return false;
		pressedKeys.add(key);
		return true;
	}

}
