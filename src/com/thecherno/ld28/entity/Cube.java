package com.thecherno.ld28.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import com.thecherno.ld28.util.Vector3f;

public class Cube extends Entity {
	private int list;
	private float size = 16.0f;
	private Random random = new Random();

	public Cube(Vector3f position) {
		super();
		width = height = size;
		position.z += size;
		this.position = position;
		create();
	}

	private void create() {
		list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glBegin(GL_QUADS);
		// Front
		glColor3f(1.0f, 0, 0);
		glVertex3f(0, size, 0);
		glVertex3f(size, size, 0);
		glVertex3f(size, 0, 0);
		glVertex3f(0, 0, 0);

		// Back
		glColor3f(0.8f, 0.4f, 0);
		glVertex3f(0, size, size);
		glVertex3f(0, 0, size);
		glVertex3f(size, 0, size);
		glVertex3f(size, size, size);

		// Left
		glColor3f(0.8f, 0, 0);
		glVertex3f(0, size, size);
		glVertex3f(0, size, 0);
		glVertex3f(0, 0, 0);
		glVertex3f(0, 0, size);

		// Right
		glColor3f(0.6f, 0, 0);
		glVertex3f(size, size, size);
		glVertex3f(size, 0, size);
		glVertex3f(size, 0, 0);
		glVertex3f(size, size, 0);

		// Top
		glColor3f(0.4f, 0, 0);
		glVertex3f(0, size, size);
		glVertex3f(size, size, size);
		glVertex3f(size, size, 0);
		glVertex3f(0, size, 0);

		// Bottom
		glColor3f(0.2f, 0, 0);
		glVertex3f(0, 0, size);
		glVertex3f(0, 0, 0);
		glVertex3f(size, 0, 0);
		glVertex3f(size, 0, size);
		glEnd();
		glEndList();
	}

	int xa = 0;
	int ya = 0;

	public void update() {
		if (random.nextInt(40) == 0) {
			xa = random.nextInt(3) - 1;
			ya = random.nextInt(3) - 1;
		}
		if (!level.wall(this, 0, ya)) {
			move(0, ya);
		}
		if (!level.wall(this, xa, 0)) {
			move(xa, 0);
		}
	}

	public void remove() {
		if (removed) return;
		removed = true;
		level.add(new BulletDrop(new Vector3f(position.x + 8, position.y + 8, position.z)));
	}

	public void render() {
		glPushMatrix();
		glTranslatef(position.x, position.y, position.z);
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		glRotatef(rotation.z, 0, 0, 1);
		glCallList(list);
		glPopMatrix();
	}
}
