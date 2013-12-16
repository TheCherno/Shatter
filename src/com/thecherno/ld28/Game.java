package com.thecherno.ld28;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.PNGDecoder;

import com.thecherno.ld28.entity.Player;
import com.thecherno.ld28.level.Level;
import com.thecherno.ld28.menu.AboutMenu;
import com.thecherno.ld28.menu.GameOver;
import com.thecherno.ld28.menu.MainMenu;
import com.thecherno.ld28.menu.Menu;
import com.thecherno.ld28.util.Texture;
import com.thecherno.ld28.util.Vector3f;

public class Game implements Runnable {

	public static int width = 1280, height = 720;
	private Thread thread;
	public static boolean running = false;

	private Vector3f rotation = new Vector3f(140, 0, 0);
	private Player player;
	private Level level;

	private static Menu menu;

	public synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public static void setMenu(Menu menu) {
		Game.menu = menu;
	}

	private ByteBuffer loadIcon() {
		try {
			PNGDecoder decoder = new PNGDecoder(Game.class.getResourceAsStream("/icon.png"));
			ByteBuffer bytebuf = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
			decoder.decode(bytebuf, decoder.getWidth() * 4, PNGDecoder.RGBA);
			bytebuf.flip();
			return bytebuf;
		} catch (IOException e) {
			System.err.println("Could not load game icon!");
			e.printStackTrace();
		}
		return null;
	}

	public void initGL() {
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(65.0f, (float) width / (float) height, 0.1f, 400.0f);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_FOG);
		float[] f = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer fog = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(f).flip();
		glFogf(GL_FOG_MODE, GL_EXP2);
		glHint(GL_FOG_HINT, GL_NICEST);
		glFog(GL_FOG_COLOR, fog);
		glFogf(GL_FOG_DENSITY, 0.007f);
	}

	public void init() {
		try {
			Display.setIcon(new ByteBuffer[] { loadIcon() });
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Shatter :  A Game by Yan Chernikov (LD28)");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		level = new Level("/levels/Prelude.png");
		rotation = new Vector3f(140, 0, 0);
		player = new Player(this, new Vector3f(-120, -220, 100), rotation);
		Player.resetScore();
		player.init(level);
	}

	public void initGame() {
		Menu.MAIN = new MainMenu();
		Menu.ABOUT = new AboutMenu();
		Menu.GAME_OVER = new GameOver(this);
		menu = Menu.MAIN;
		reset();
	}

	public void run() {
		init();
		initGL();
		new Texture().load();
		initGame();
		Mouse.setGrabbed(true);
		long lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			clear();
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				frames = 0;
				updates = 0;
			}
			if (Display.isCloseRequested()) break;
		}
		AL.destroy();
		Display.destroy();
	}

	private void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
	}

	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) Mouse.setGrabbed(false);
		if (Mouse.isButtonDown(0)) Mouse.setGrabbed(true);
		// rotation.x -= Mouse.getDY() * 0.5f;
		// rotation.y -= Mouse.getDX() * 0.5f;
		if (State.getState() == State.GAME) {
			player.update();
			level.update();
		} else if (State.getState() == State.MENU) {
			menu.update();
		}
		if (!Mouse.isGrabbed()) return;
	}

	public void render() {
		clear();
		if (State.getState() == State.GAME) {
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			GLU.gluPerspective(65.0f, (float) width / (float) height, 0.1f, 380.0f);
			glMatrixMode(GL_MODELVIEW);
			player.render();
			level.render();
			glPushMatrix();
			glLoadIdentity();
			player.renderHUD();
			glPopMatrix();
		} else if (State.getState() == State.MENU) {
			menu.render();
		}
		Display.sync(60);
		Display.update();
	}

	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
		Game game = new Game();
		game.start();
	}
}
