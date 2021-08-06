package com.viduan.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viduan.main.Game;

public class Saver extends Entity {
	
	private int framesAnimation = 0;
	private int maxFrames = 10;
	
	private int maxSprite = 2;
	private int curSprite = 0;
	
	public static boolean showMessage = false;
	
	public String[] frases = new String[2];
	
	public Saver(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
		frases[0] = "Olá! Deseja salvar o jogo?";
		  
	}
	public void tick() {
		depth=1;
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		
		int xSaver = (int)x;
		int ySaver = (int)y;
		
		
		
		
		
		if(Math.abs(xPlayer-xSaver) < 20 &&
				Math.abs(yPlayer-ySaver) < 20) {
			showMessage = true;
			
		}else {
			showMessage = false;
		}
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
		if(Game.seletor == true) {
			if(showMessage == true) {
				g.setColor(Color.black);
				g.drawRect(9, 21,Game.WIDTH-249,Game.HEIGHT-149);
				g.setColor(Color.white);
				g.fillRect(10, 22, Game.WIDTH-250, Game.HEIGHT-150);
				g.setFont(new Font("arial",Font.BOLD,10));
				g.setColor(Color.black);
				g.drawString(frases[0],30,50);
				g.setFont(new Font("arial",Font.BOLD,9));
				g.drawString("Digite S p/ sim e N p/ não", 37, 60);
				
			}
		}
	}
	public static void salvando() {
		Game.saveGame=true;
	}
	

}
