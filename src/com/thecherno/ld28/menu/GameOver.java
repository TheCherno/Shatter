package com.thecherno.ld28.menu;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.thecherno.ld28.Game;
import com.thecherno.ld28.entity.Player;
import com.thecherno.ld28.util.Key;
import com.thecherno.ld28.util.Texture;

public class GameOver extends Menu {

	private UnicodeFont font;
	private Game game;

	public GameOver(Game game) {
		super(new String[] { "Main Menu" }, 0, 0);
		this.game = game;
		texture = Texture.GAME_OVER;
		position.x = 460.0f;
		position.y = Game.height - 80.0f;
		Font f = new Font("Verdana", 0, 50);
		font = new UnicodeFont(f);
		font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
	}

	public void update() {
		if (Key.typed(Keyboard.KEY_RETURN)) {
			Game.setMenu(Menu.MAIN);
			game.reset();
		}
	}

	public void render() {
		super.render();
		glDisable(GL_TEXTURE_2D);
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_FOG);
		glDisable(GL_DEPTH_TEST);
		font.drawString(500.0f, Game.height - 220.0f, "Score: " + Player.getScore());
		font.addGlyphs("Score:0123456789");
		glEnable(GL_FOG);
		glEnable(GL_DEPTH_TEST);
	}

}
