package com.gcstudios.entities;

import java.awt.image.BufferedImage;

public class Coin extends Entity{

	static int value;
	
	public Coin(double x, double y, int width, int height, double speed, BufferedImage sprite,int value) {
		
		super(x, y, width, height, speed, sprite);
		this.value = value;
	}
	public void tick() {
		
		depth = 2;
	}
	public static int getValue() {
		return value;
	}

}
