package com.viduan.entities;


import java.awt.image.BufferedImage;

public class Weapon extends Entity {

	public Weapon(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
		depth = 1;
	}
	

}
