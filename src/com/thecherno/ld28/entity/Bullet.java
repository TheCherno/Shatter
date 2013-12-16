package com.thecherno.ld28.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import com.thecherno.ld28.util.Vector3f;

public class Bullet extends Entity {

	private int list;
	private Vector3f direction;
	private double angle;
	private boolean stuck = false;

	public Bullet(Vector3f position, double angle) {
		super();
		width = 8;
		height = 8;
		this.direction = new Vector3f((float) Math.cos(angle), (float) Math.sin(angle), 0);
		this.angle = angle;
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

		this.position = position;
	}

	public void update() {
		for (int i = 0; i < 12; i++) {
			if (level.wall(this, 0, 0)) {
				stuck = true;
				return;
			}
			if (collision()) {
				remove();
			}
			move(direction.x, direction.y);
		}
	}

	private boolean collision() {
		List<Entity> entities = level.getEntities(this, 16);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			float x0 = position.x;
			float y0 = position.y;
			float x1 = e.position.x;
			float y1 = e.position.y;
			if (x0 >= x1 - 4 && x0 <= x1 + e.getWidth() + 4) {
				if (y0 >= y1 - 4 && y0 <= y1 + e.getHeight() + 4) {
					if (!(e instanceof Bullet) && !(e instanceof BulletDrop)) {
						if (e.removed) continue;
						e.remove();
						Player.score += 100;
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean stuck() {
		return stuck;
	}

	public void render() {
		glDisable(GL_FOG);
		glPushMatrix();
		glTranslatef(position.x, position.y, position.z);
		glRotatef(90 + (float) (angle * 180 / Math.PI), 0, 0, 1);
		glCallList(list);
		glPopMatrix();
		glEnable(GL_FOG);
	}

}
