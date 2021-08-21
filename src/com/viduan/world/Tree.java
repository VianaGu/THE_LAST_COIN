package com.viduan.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viduan.entities.Entity;
import com.viduan.main.Game;
import com.viduan.main.Sound;

public class Tree extends Entity{
	
	public static boolean collected;

	public Tree(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
	}
	
	public void tick() {
		if(Game.player.maisVida==true) {
			Game.player.maisVida=false;
			ganharVida();
			
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
	}
	
	
	public void ganharVida() {
		if((Game.player.life+20) >= 100) {
			Game.player.life=100;
		}else {
			Game.player.life+=20;
		}
	}
	
}
