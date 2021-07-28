package com.viduan.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viduan.main.Game;

public class Saver extends Entity {
	
	private int framesAnimation = 0;
	private int maxFrames = 10;
	
	private int maxSprite = 2;
	private int curSprite = 0;
	
	
	public Saver(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
		
	}
	public void tick() {
		depth=3;
	}
	public void render(Graphics g) {
		framesAnimation ++;
		if(framesAnimation == maxFrames) {
			curSprite++;
			framesAnimation = 0; 
			if(curSprite == maxSprite) {
				curSprite = 0;
			}
		}
		
			sprite = Entity.SAVER[curSprite];
		super.render(g);
	}
	public static void salvando() {
		Game.saveGame=true;
	}
	

}
