package com.thecherno.ld28.level;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.thecherno.ld28.entity.Bullet;
import com.thecherno.ld28.entity.Cube;
import com.thecherno.ld28.entity.Entity;
import com.thecherno.ld28.entity.Player;
import com.thecherno.ld28.util.Texture;
import com.thecherno.ld28.util.Vector2f;
import com.thecherno.ld28.util.Vector3f;

public class Level {

	private int width, height;
	private int list;
	private float z = 30.0f;
	private int size = 32;

	private List<Entity> entities = new ArrayList<Entity>();
	private int[] pixels;
	private List<Wall> walls = new ArrayList<Wall>();

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		create();
	}

	public Level(String file) {
		load(file);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int col = pixels[x + y * width];
				if (col == 0xff000000) walls.add(new Wall(new Vector3f(x * size, y * size, z - 64), new Vector3f(0, 0, 0)));
				if (col == 0xffff0000) {
					for (int i = 0; i < 5; i++) {
						add(new Cube(new Vector3f(x * size, y * size, 0)));
					}
				}
			}
		}
		create();
	}

	private void load(String file) {
		try {
			BufferedImage image = ImageIO.read(Level.class.getResourceAsStream(file));
			int w = image.getWidth();
			int h = image.getHeight();
			this.width = w;
			this.height = h;
			pixels = new int[w * h];
			image.getRGB(0, 0, w, h, pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void create() {
		list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glBegin(GL_QUADS);
		for (int y = 0; y < height; y++) {
			int ya = y * size;
			for (int x = 0; x < width; x++) {
				int xa = x * size;
				glColor3f(1.0f, 1.0f, 1.0f);
				glTexCoord2f(0, 0);
				glVertex3f(xa, ya, z);
				glTexCoord2f(0, 1);
				glVertex3f(xa, ya + size, z);
				glTexCoord2f(1, 1);
				glVertex3f(xa + size, ya + size, z);
				glTexCoord2f(1, 0);
				glVertex3f(xa + size, ya, z);
			}
		}
		glEnd();
		glEndList();
	}

	public void add(Entity e) {
		e.init(this);
		entities.add(e);
	}

	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
			if (entities.get(i).removed) entities.remove(i);
		}
	}

	public boolean wall(Vector2f pos, Entity e, float xa, float ya) {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			float x0 = w.getPosition().x;
			float y0 = w.getPosition().y;
			float x1 = pos.x + xa;
			float y1 = pos.y + ya;
			if (x1 + e.getWidth() >= x0 && x1 <= x0 + w.getWidth()) {
				if (y1 <= y0 + w.getHeight() - 8 && y1 + e.getHeight() >= y0 - 12) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean wall(Entity e, int xa, int ya) {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			float x0 = w.getPosition().x;
			float y0 = w.getPosition().y;
			float x1 = e.getPosition().x + xa;
			float y1 = e.getPosition().y + ya;
			int xOffset0 = 0;
			int xOffset1 = 0;
			int yOffset0 = 0;
			int yOffset1 = 0;
			if (e instanceof Bullet) {
				xOffset0 = 0;
				xOffset1 = -8;
				yOffset0 = 8;
			}
			if (x1 + e.getWidth() + xOffset1 >= x0 && x1 <= x0 + w.getWidth() + xOffset0) {
				if (y1 <= y0 + w.getHeight() + yOffset1 && y1 + e.getHeight() >= y0 + yOffset0) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Entity> getEntities(Vector3f from, Entity ignore, int radius) {
		List<Entity> result = new ArrayList<Entity>();
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e == ignore) continue;
			double dx = e.getPosition().x - from.x;
			double dy = e.getPosition().y - from.y;
			if (Math.sqrt(dx * dx + dy * dy) > radius) continue;
			result.add(e);
		}
		return result;
	}

	public List<Entity> getEntities(Entity from, int radius) {
		List<Entity> result = new ArrayList<Entity>();
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e == from) continue;
			double dx = e.getPosition().x - from.getPosition().x;
			double dy = e.getPosition().y - from.getPosition().y;
			if (Math.sqrt(dx * dx + dy * dy) > radius) continue;
			result.add(e);
		}
		return result;
	}

	public void render() {
		glPushMatrix();
		glBindTexture(GL_TEXTURE_2D, Texture.FLOOR);
		glCallList(list);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).render();
		}
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render();
			if (entities.get(i).removed) entities.remove(i);
		}
	}

	public boolean cube(Vector3f position, Player player) {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!(e instanceof Cube)) continue;
			float x0 = position.x;
			float y0 = position.y;
			float x1 = e.getPosition().x;
			float y1 = e.getPosition().y;
			if (x1 + e.getWidth() + 8 >= x0 && x1 <= x0 + player.getWidth() - 8) {
				if (y1 <= y0 + player.getHeight() + 4 && y1 + e.getHeight() >= y0) {
					return true;
				}
			}
		}
		return false;
	}
}
