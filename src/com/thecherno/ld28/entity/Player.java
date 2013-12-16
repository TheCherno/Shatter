package com.thecherno.ld28.entity;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.thecherno.ld28.Game;
import com.thecherno.ld28.State;
import com.thecherno.ld28.menu.Menu;
import com.thecherno.ld28.util.Cursor;
import com.thecherno.ld28.util.Texture;
import com.thecherno.ld28.util.Vector2f;
import com.thecherno.ld28.util.Vector3f;

public class Player extends Entity {

	private int list, overlay;
	private float size = 16.0f;
	private int delay = 20;
	private Cursor cursor;
	private int bullets = 1;
	private UnicodeFont font;
	static long score = 0;

	public Player(Game game, Vector3f position, Vector3f rotation) {
		super();
		position.z += size;
		this.position = position;
		width = height = size;
		this.rotation = rotation;
		Font f = new Font("Verdana", 0, 50);
		font = new UnicodeFont(f);
		create();
		cursor = new Cursor();
		font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
	}

	private void create() {
		list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glBegin(GL_QUADS);
		// Front
		glColor3f(0.0f, 1.0f, 0.0f);
		glVertex3f(0, size, 0);
		glVertex3f(size, size, 0);
		glVertex3f(size, 0, 0);
		glVertex3f(0, 0, 0);

		// Back
		glColor3f(0.4f, 0.8f, 0.0f);
		glVertex3f(0, size, size);
		glVertex3f(0, 0, size);
		glVertex3f(size, 0, size);
		glVertex3f(size, size, size);

		// Left
		glColor3f(0.0f, 0.8f, 0.0f);
		glVertex3f(0, size, size);
		glVertex3f(0, size, 0);
		glVertex3f(0, 0, 0);
		glVertex3f(0, 0, size);

		// Right
		glColor3f(0.0f, 0.6f, 0.0f);
		glVertex3f(size, size, size);
		glVertex3f(size, 0, size);
		glVertex3f(size, 0, 0);
		glVertex3f(size, size, 0);

		// Top
		glColor3f(0.0f, 0.4f, 0.0f);
		glVertex3f(0, size, size);
		glVertex3f(size, size, size);
		glVertex3f(size, size, 0);
		glVertex3f(0, size, 0);

		// Bottom
		glColor3f(0.0f, 0.2f, 0.0f);
		glVertex3f(0, 0, size);
		glVertex3f(0, 0, 0);
		glVertex3f(size, 0, 0);
		glVertex3f(size, 0, size);
		glEnd();
		glEndList();

		overlay = glGenLists(1);
		glNewList(overlay, GL_COMPILE);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0.0f, 0.0f);
		glTexCoord2f(0, 1);
		glVertex2f(0.0f, 1020.0f);
		glTexCoord2f(1, 1);
		glVertex2f(2050.0f, 1020.0f);
		glTexCoord2f(1, 0);
		glVertex2f(2050.0f, 0.0f);
		glEnd();
		glEndList();
	}

	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			float xa = (float) (Math.sin(Math.toRadians(rotation.z)));
			float ya = (float) (Math.cos(Math.toRadians(rotation.z)));
			if (!level.wall(new Vector2f(-position.x - size / 2, -position.y - 90 - size / 2), this, -xa, -ya)) {
				translate(xa, ya, 0);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			float xa = (float) (-Math.sin(Math.toRadians(rotation.z)));
			float ya = (float) (-Math.cos(Math.toRadians(rotation.z)));
			if (!level.wall(new Vector2f(-position.x - size / 2, -position.y - 90 - size / 2), this, -xa, -ya)) {
				translate(xa, ya, 0);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			float xa = (float) (Math.sin(Math.toRadians(rotation.z + 90)));
			float ya = (float) (Math.cos(Math.toRadians(rotation.z + 90)));
			if (!level.wall(new Vector2f(-position.x - size / 2, -position.y - 90 - size / 2), this, -xa, -ya)) {
				translate(xa, ya, 0);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			float xa = (float) (Math.sin(Math.toRadians(rotation.z - 90)));
			float ya = (float) (Math.cos(Math.toRadians(rotation.z - 90)));
			if (!level.wall(new Vector2f(-position.x - size / 2, -position.y - 90 - size / 2), this, -xa, -ya)) {
				translate(xa, ya, 0);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
			State.setState(State.MENU);
			Game.setMenu(Menu.GAME_OVER);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			rotation.z += 2.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			rotation.z -= 2.0f;
		}
		position.z -= Mouse.getDWheel() / 20;
		if (delay > 0) delay--;
		if (Mouse.isButtonDown(0)) {
			if (delay > 0) return;
			if (bullets <= 0) return;
			Vector3f pos = position.inverse();
			pos.y -= 80;
			pos.z = 16;
			double dx = Mouse.getX() - 640;
			double dy = 720 - Mouse.getY() - 426;
			double dir = Math.atan2(dy, dx) - (rotation.z * Math.PI / 180);
			level.add(new Bullet(pos, dir));
			bullets--;
			delay = 20;
		}

		List<Entity> entities = level.getEntities(new Vector3f(-position.x, -position.y - 90, 0), this, 40);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof BulletDrop || (e instanceof Bullet && ((Bullet) e).stuck())) {
				float x0 = -position.x;
				float y0 = -position.y - 90;
				float x1 = e.position.x;
				float y1 = e.position.y;
				if (e instanceof BulletDrop) {
					if (x1 > x0) e.position.x -= 0.8f;
					if (x1 < x0) e.position.x += 0.8f;
					if (y1 > y0) e.position.y -= 0.8f;
					if (y1 < y0) e.position.y += 0.8f;
				}
				if (e instanceof Bullet) {
					if (x0 > x1 - 24 && x0 < x1 + 12) {
						if (y0 > y1 - 24 && y0 < y1 + 12) {
							bullets++;
							e.remove();
						}
					}
				} else {
					if (x0 > x1 - 12 && x0 < x1 + 12) {
						if (y0 > y1 - 12 && y0 < y1 + 12) {
							score += 10;
							bullets++;
							e.remove();
						}
					}
				}
			}
		}
		if (level.cube(new Vector3f(-position.x, -position.y - 90, 0), this)) {
			State.setState(State.MENU);
			Game.setMenu(Menu.GAME_OVER);
		}

	}

	public void renderHUD() {
		glPushMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1280, 720, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		cursor.render();
		renderOverlay();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		glEnable(GL_TEXTURE_2D);
		glPushMatrix();
		glLoadIdentity();
		glDisable(GL_FOG);
		glDisable(GL_DEPTH_TEST);
		font.drawString(20.0f, Game.height - 80.0f, "" + bullets);
		font.drawString(20.0f, 20.0f, "Score: " + score);
		font.addGlyphs("Score:0123456789");
		glEnable(GL_FOG);
		glEnable(GL_DEPTH_TEST);
		glPopMatrix();
	}

	public void renderOverlay() {
		glPushMatrix();
		glLoadIdentity();
		glBindTexture(GL_TEXTURE_2D, Texture.OVERLAY);
		glCallList(overlay);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

	public void render() {
		super.render();
		glPushMatrix();
		glLoadIdentity();
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		glTranslatef(-8.0f, -92, position.z + 14);
		glEnable(GL_COLOR_MATERIAL);
		glDisable(GL_TEXTURE_2D);
		glCallList(list);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_COLOR_MATERIAL);
		glPopMatrix();

	}

	public static void resetScore() {
		score = 0;
	}

	public static long getScore() {
		return score;
	}
}
