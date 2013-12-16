package com.thecherno.ld28.util;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

public class Texture {

	public static int CURSOR;
	public static int FLOOR;
	public static int OVERLAY;
	public static int MENU;
	public static int GAME_OVER;
	public static int WALLS;

	public void load() {
		Texture.CURSOR = loadTexture("/cursor.png", true);
		Texture.FLOOR = loadTexture("/tex/floor.png", false);
		Texture.WALLS = loadTexture("/tex/walls.png", false);
		Texture.OVERLAY = loadTexture("/overlay.png", false);
		Texture.MENU = loadTexture("/menu/main.png", false);
		Texture.GAME_OVER = loadTexture("/menu/game_over.png", false);
	}

	private int loadTexture(final String path, boolean antialiased) {
		org.newdawn.slick.opengl.Texture tex = null;
		try {
			tex = org.newdawn.slick.opengl.TextureLoader.getTexture("PNG", Texture.class.getResourceAsStream(path));
			glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
			if (antialiased) {
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			} else {
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			}
			glBindTexture(GL_TEXTURE_2D, 0);
		} catch (IOException e) {
			System.err.println("Access denied: " + tex);
			e.printStackTrace();
			return 0;
		} catch (NullPointerException e) {
			System.err.println("Failed to load texture: " + tex);
			e.printStackTrace();
			return 0;
		}
		return tex.getTextureID();
	}

}
