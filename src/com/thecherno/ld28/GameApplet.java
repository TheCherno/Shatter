package com.thecherno.ld28;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class GameApplet extends Applet implements Runnable {
	private static final long serialVersionUID = 1L;

	private Canvas display;
	private Thread thread;
	private Game game;

	public void init() {
		setLayout(new BorderLayout());
		try {
			display = new Canvas() {
				private static final long serialVersionUID = 1L;
				public final void addNotify() {
					super.addNotify();
					game = new Game();
					startGame();
				}

				public final void removeNotify() {
					stopGame();
					super.removeNotify();
				}
			};
			add(display);
			display.setSize(1280, 720);
			display.setFocusable(true);
			display.requestFocus();
			display.setIgnoreRepaint(true);
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to create display");
		}
	}

	public void startGame() {
		thread = new Thread(this);
		thread.start();
		Game.running = true;
	}

	public void start() {
	}

	public void stop() {
	}

	public void run() {
		try {
			Display.setParent(display);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		game.run();
	}

	private void stopGame() {
		Game.running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
