package com.viduan.entities;

import java.awt.image.BufferedImage;

import com.viduan.main.Game;
import com.viduan.world.Tile;
import com.viduan.world.Tranformer;
import com.viduan.world.World;

public class Sensor extends Entity {
	public static boolean sense = false;

	public Sensor(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		
	}
	
	
}
