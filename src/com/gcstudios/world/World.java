package com.gcstudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.gcstudios.entities.Coin;
import com.gcstudios.entities.Enemy;
import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Player;
import com.gcstudios.entities.Sensor;
import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static int TILE_SIZE = 32;
	
	
	public World(String path){
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TILE_FLOOR);
						
					}else if(pixelAtual == 0xFFffffff) {
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TILE_WALL);
						if(yy-1 >= 0  &&  pixels[xx+((yy-1) * map.getWidth())] == 0xFFffffff ) {
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TIEL_WALL1);
						}
					}else if(pixelAtual == 0xFF0026FF) {
						//Jogador 
						Game.player.setX(xx*TILE_SIZE);
						Game.player.setY(yy*TILE_SIZE);
					}else if(pixelAtual == 0xFFFF0000) {
						//Inimigo
						Enemy enemy = new Enemy(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Entity.ENEMY1_RIGHT);
						Game.entities.add(enemy);
						Game.enemies.add(enemy);
					}else if(pixelAtual == 0xFFFFD800) {
						//Moeda 
						Coin coin = new Coin(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Entity.COIN,1);
						Game.entities.add(coin);	
						Game.coin.add(coin);
						Player.maxCoins+=coin.getValue();
					}else if(pixelAtual == 0xFF606060) {
						//Sensor
						Sensor sensor = new Sensor(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Tile.TIEL_WALL1);
						Game.entities.add(sensor);
					}else if(pixelAtual == 0xFFFFE359) {
						//Pode virar moeda
						Tranformer trans = new Tranformer(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Tile.TIEL_WALL1,0);
						Game.entities.add(trans);
						Game.transformer.add(trans);
						
						
					}
					
					
					
					
					
					
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void restartGame(String level){
		Game.entities.clear();
		Game.enemies.clear();
	    Player.maxCoins = 0;
	    Player.currentCoins = 0;
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Player.life=Player.maxLife;
		Game.player = new Player(WIDTH/2 - 30,HEIGHT/2,World.TILE_SIZE,World.TILE_SIZE,1.4,Entity.PLAYER_SPRITE_RIGHT[0]);
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
	
	public static boolean isFree(int xnext,int ynext){
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	
	public void render(Graphics g){
		int xstart = Camera.x >> 5;
		int ystart = Camera.y >> 5;
		
		int xfinal = xstart + (Game.WIDTH >> 5);
		int yfinal = ystart + (Game.HEIGHT >> 5);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
