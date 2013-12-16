package com.thecherno.ld28.util;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Mouse;

public class Cursor {

	private int list;
	private final float SIZE = 8.0f;

	public Cursor() {
		list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glBegin(GL_QUADS);
		glColor3f(1.0f, 1.0f, 1.0f);
		glTexCoord2f(0, 0);
		glVertex2f(0.0f, 0.0f);
		glTexCoord2f(0, 1);
		glVertex2f(0.0f, SIZE);
		glTexCoord2f(1, 1);
		glVertex2f(SIZE, SIZE);
		glTexCoord2f(1, 0);
		glVertex2f(SIZE, 0.0f);
		glEnd();
		glEndList();
	}

	public void render() {
		glPushMatrix();
		// glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glTranslatef(Mouse.getX(), 720 - Mouse.getY(), 0);
		glBindTexture(GL_TEXTURE_2D, Texture.CURSOR);
		glCallList(list);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

}
