package com.thecherno.ld28.level;

import static org.lwjgl.opengl.GL11.*;

import com.thecherno.ld28.util.Texture;
import com.thecherno.ld28.util.Vector3f;

public class Wall {
	private int list;
	private int width, height, depth;
	private Vector3f position, rotation;

	public Wall(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
		width = 32;
		height = 64;
		depth = 32;
		create();
	}

	private void create() {
		list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glBegin(GL_QUADS);
		// Front
		glColor3f(1.0f, 1.0f, 1.0f);
		glTexCoord2f(0, 1);
		glVertex3f(0, depth, 0);
		glTexCoord2f(1, 1);
		glVertex3f(width, depth, 0);
		glTexCoord2f(1, 0);
		glVertex3f(width, 0, 0);
		glTexCoord2f(0, 0);
		glVertex3f(0, 0, 0);

		// Back
		glTexCoord2f(0, 1);
		glVertex3f(0, depth, height);
		glTexCoord2f(0, 0);
		glVertex3f(0, 0, height);
		glTexCoord2f(1, 0);
		glVertex3f(width, 0, height);
		glTexCoord2f(1, 1);
		glVertex3f(width, depth, height);

		// Left
		glTexCoord2f(1, 1);
		glVertex3f(0, depth, height);
		glTexCoord2f(1, 0);
		glVertex3f(0, depth, 0);
		glTexCoord2f(0, 0);
		glVertex3f(0, 0, 0);
		glTexCoord2f(0, 1);
		glVertex3f(0, 0, height);

		// Right
		glTexCoord2f(1, 1);
		glVertex3f(width, depth, height);
		glTexCoord2f(0, 1);
		glVertex3f(width, 0, height);
		glTexCoord2f(0, 0);
		glVertex3f(width, 0, 0);
		glTexCoord2f(1, 0);
		glVertex3f(width, depth, 0);

		// Top
		glTexCoord2f(0, 1);
		glVertex3f(0, depth, height);
		glTexCoord2f(1, 1);
		glVertex3f(width, depth, height);
		glTexCoord2f(1, 0);
		glVertex3f(width, depth, 0);
		glTexCoord2f(0, 0);
		glVertex3f(0, depth, 0);

		// Bottom
		glTexCoord2f(0, 1);
		glVertex3f(0, 0, height);
		glTexCoord2f(0, 0);
		glVertex3f(0, 0, 0);
		glTexCoord2f(1, 0);
		glVertex3f(width, 0, 0);
		glTexCoord2f(1, 1);
		glVertex3f(width, 0, height);
		glEnd();
		glEndList();
	}

	public void render() {
		glPushMatrix();
		glTranslatef(position.x, position.y, position.z);
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		glRotatef(rotation.z, 0, 0, 1);
		glBindTexture(GL_TEXTURE_2D, Texture.WALLS);
		glCallList(list);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return depth;
	}

	public Vector3f getPosition() {
		return position;
	}
}
