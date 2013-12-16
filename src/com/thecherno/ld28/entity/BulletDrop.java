package com.thecherno.ld28.entity;

import static org.lwjgl.opengl.GL11.*;

import com.thecherno.ld28.util.Vector3f;

public class BulletDrop extends Entity {
	private int list;

	public BulletDrop(Vector3f position) {
		super();
		this.position = position;
		list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glBegin(GL_QUADS);
		// Front
		glColor3f(1.0f, 0.941f, 0);
		glVertex3f(0, 8, 0);
		glVertex3f(1, 8, 0);
		glVertex3f(1, 0, 0);
		glVertex3f(0, 0, 0);

		// Back
		glVertex3f(0, 8, 1);
		glVertex3f(0, 0, 1);
		glVertex3f(1, 0, 1);
		glVertex3f(1, 8, 1);

		// Left
		glVertex3f(0, 8, 1);
		glVertex3f(0, 8, 0);
		glVertex3f(0, 0, 0);
		glVertex3f(0, 0, 1);

		// Right
		glVertex3f(1, 8, 1);
		glVertex3f(1, 0, 1);
		glVertex3f(1, 0, 0);
		glVertex3f(1, 8, 0);

		// Top
		glVertex3f(0, 8, 1);
		glVertex3f(1, 8, 1);
		glVertex3f(1, 8, 0);
		glVertex3f(0, 8, 0);

		// Bottom
		glVertex3f(0, 0, 1);
		glVertex3f(0, 0, 0);
		glVertex3f(1, 0, 0);
		glVertex3f(1, 0, 1);

		glEnd();
		glEndList();
	}

	private double time = 0;

	public void update() {
		time += 0.07;
		rotation.z++;
		position.z += (float) Math.sin(time) * 0.25;
	}

	public void render() {
		glDisable(GL_FOG);
		glPushMatrix();
		glTranslatef(position.x, position.y, position.z);
		glTranslatef(1, 4, 0);
		glRotatef(rotation.z, 0, 0, 1);
		glTranslatef(-1, -4, 0);
		glCallList(list);
		glPopMatrix();
		glEnable(GL_FOG);
	}
}
