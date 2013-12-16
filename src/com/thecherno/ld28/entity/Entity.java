package com.thecherno.ld28.entity;

import com.thecherno.ld28.level.Level;
import com.thecherno.ld28.util.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Entity {

	protected Level level;
	protected Vector3f position, rotation;
	protected float width, height;
	public boolean removed = false;

	protected Entity() {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
	}

	public void init(Level level) {
		this.level = level;
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}

	public void translate(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}

	public void rotate(float x, float y, float z) {
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
	}

	public void move(float xa, float ya) {
		position.x += xa;
		position.y += ya;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void remove() {
		removed = true;
	}

	public void update() {
	}

	public void render() {
		if (position.z < 36) position.z = 36;
		if (position.z > 150) position.z = 150;
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		float ox = 0.0f;
		float oy = 80.0f;
		glTranslatef(-ox, -oy, 0);
		glRotatef(rotation.z, 0, 0, 1);
		glTranslatef(ox, oy, 0);
		glTranslatef(position.x, position.y, position.z);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

}
