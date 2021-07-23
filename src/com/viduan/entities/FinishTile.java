package com.viduan.entities;

import java.awt.image.BufferedImage;

import com.viduan.main.Game;

public class FinishTile extends Entity{

	public static boolean finalLevel;
	
	public FinishTile(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
	}
	public void tick() {
		depth = 2;
		super.tick();
		
	}
}
