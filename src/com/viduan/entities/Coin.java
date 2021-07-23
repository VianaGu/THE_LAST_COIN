package com.viduan.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Coin extends Entity{

	static int value;
	
	public static boolean gotcha = false;
	
	private int framesAnimation = 0;
	private int maxFrames = 12;
	
	private int maxSprite = 3;
	private int curSprite = 0;
	
	
	public Coin(double x, double y, int width, int height, double speed, BufferedImage sprite,int value) {
		
		super(x, y, width, height, speed, sprite);
		this.value = value;
	}
	public void tick() {
		//debug=true;
		depth = 2;
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
		
			sprite = Entity.COIN[curSprite];
		
	super.render(g);
	}
	
	public static boolean getGotcha() {
		return gotcha;
	}
	public static int getValue() {
		return value;
	}

}
