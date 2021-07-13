package com.gcstudios.entities;

import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;
import com.gcstudios.world.Tile;
import com.gcstudios.world.Tranformer;
import com.gcstudios.world.World;

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
