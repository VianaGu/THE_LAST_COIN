package com.viduan.entities;

import java.awt.image.BufferedImage;

import com.viduan.main.Game;
import com.viduan.world.Tile;
import com.viduan.world.Tranformer;
import com.viduan.world.World;

public class Unsensor extends Entity {

	public static boolean unsense;
	
	
	public Unsensor(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}
	public void tick() {
		depth = 0;
		//debug = true;
		super.tick();
	}
	
	
	
		
	

}
