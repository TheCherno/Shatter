package com.thecherno.ld28.menu;

import org.lwjgl.input.Keyboard;

import com.thecherno.ld28.Game;
import com.thecherno.ld28.State;
import com.thecherno.ld28.util.Key;
import com.thecherno.ld28.util.Texture;

public class MainMenu extends Menu {

	public MainMenu() {
		super(new String[] { "Play", "About", "Quit" }, 0, 0);
		texture = Texture.MENU;
	}

	public void update() {
		super.update();
		if (Key.typed(Keyboard.KEY_RETURN)) {
			switch (selected) {
			case 0:
				State.setState(State.GAME);
				break;
			case 1:
				Game.setMenu(Menu.ABOUT);
				break;
			case 2:
				Game.running = false;
				break;
			default:
				break;
			}
		}
		switch (selected) {
		case 0:
			position.x = Game.width - 230;
			position.y = Game.height - 210;
			break;
		case 1:
			position.x = Game.width - 265;
			position.y = Game.height - 135;
			break;
		case 2:
			position.x = Game.width - 230;
			position.y = Game.height - 65;
			break;
		}
	}

	public void render() {
		super.render();
	}
}
