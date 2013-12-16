package com.thecherno.ld28.menu;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.thecherno.ld28.Game;
import com.thecherno.ld28.util.Key;
import com.thecherno.ld28.util.Texture;
import com.thecherno.ld28.util.Vector2f;

public class Menu {

	protected String[] options;
	protected final int MIN_SELECTED, MAX_SELECTED;
	protected int list, selector;
	protected int selected = 0;
	protected Vector2f position;
	protected int texture = 0;

	public static Menu MAIN, ABOUT, GAME_OVER;

	protected Menu(String[] options, float xOffset, float yOffset) {
		this.options = options;
		MIN_SELECTED = 0;
		MAX_SELECTED = options.length - 1;
		create(xOffset, yOffset);
		position = new Vector2f(0, 0);
		texture = Texture.MENU;
	}

	private void create(float xOffset, float yOffset) {
		list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glBegin(GL_QUADS);
		glTexCoord2f(xOffset / 2048.0f, yOffset / 2048.0f);
		glVertex2f(0, 0);
		glTexCoord2f(xOffset / 2048.0f, (float) (yOffset + Game.height) / 2048.0f);
		glVertex2f(0, (float) Game.height);
		glTexCoord2f((float) (xOffset + Game.width) / 2048.0f, (float) (yOffset + Game.height) / 2048.0f);
		glVertex2f((float) Game.width, (float) (Game.height));
		glTexCoord2f((float) (xOffset + Game.width) / 2048.0f, yOffset / 2048.0f);
		glVertex2f((float) Game.width, 0);
		glEnd();
		glEndList();

		selector = glGenLists(1);
		glNewList(selector, GL_COMPILE);
		glBegin(GL_QUADS);
		glColor3f(1.0f, 1.0f, 1.0f);
		glVertex2f(0, 0);
		glVertex2f(0, 12);
		glVertex2f(22, 12);
		glVertex2f(22, 0);
		glEnd();
		glEndList();
	}

	public void update() {
		if (Key.typed(Keyboard.KEY_UP)) {
			if (selected > MIN_SELECTED) selected--;
		}
		if (Key.typed(Keyboard.KEY_DOWN)) {
			if (selected < MAX_SELECTED) selected++;
		}
	}

	public void render() {
		glPushMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, (float) Game.width, (float) Game.height, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glBindTexture(GL_TEXTURE_2D, texture);
		glCallList(list);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPushMatrix();
		glLoadIdentity();
		glDisable(GL_DEPTH_TEST);
		glTranslatef(position.x, position.y, 0);
		glCallList(selector);
		glEnable(GL_DEPTH_TEST);
		glPopMatrix();
		glPopMatrix();
	}
}
