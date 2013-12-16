package com.thecherno.ld28.menu;

import org.lwjgl.input.Keyboard;

import com.thecherno.ld28.Game;
import com.thecherno.ld28.util.Key;
import com.thecherno.ld28.util.Texture;

public class AboutMenu extends Menu {

	public AboutMenu() {
		super(new String[] { "Back" }, 0, 720);
		position.x = Game.width - 230;
		position.y = Game.height - 65;
		texture = Texture.MENU;
	}

	public void update() {
		super.update();
		if (Key.typed(Keyboard.KEY_RETURN)) {
			Game.setMenu(Menu.MAIN);
		}
	}

	public void render() {
		super.render();
	}
}
