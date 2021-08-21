package com.viduan.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.viduan.main.Game;
import com.viduan.world.Camera;


public class Particle extends Entity{

	public int lifeTime = 15;	
	public int curLife = 0;
	
	public int spd = 2;
	public double dx = 0;
	public double dy = 0;
	
	public Particle(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();
	}

	public void tick() {
		
		
		x+= dx*spd;
		y+= dy*spd;
		curLife++;
		if(lifeTime == curLife) {
			Game.entities.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(getX()-Camera.x, getY()-Camera.y, 1, 1);
	}
	
	
}
