package com.viduan.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viduan.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0,0,World.TILE_SIZE,World.TILE_SIZE);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(32,0,World.TILE_SIZE,World.TILE_SIZE);
	public static BufferedImage TILE_WALL1 = Game.spritesheet.getSprite(32, 32, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_WALL_CHANGE = Game.spritesheet.getSprite(0, 32, World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage TILE_FINAL = Game.spritesheet.getSprite(64, 0, World.TILE_SIZE, World.TILE_SIZE);

	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x,int y,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}

}
