package com.viduan.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.viduan.entities.Coin;
import com.viduan.entities.Enemy;
import com.viduan.entities.Entity;
import com.viduan.entities.FinishTile;
import com.viduan.entities.Player;
import com.viduan.entities.Saver;
import com.viduan.entities.Sensor;
import com.viduan.graficos.Spritesheet;
import com.viduan.main.Game;
import com.viduan.main.Sound;

public class World {

	
	
	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static int TILE_SIZE = 32;
	
	//criação do mapa
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
						if(yy-1 >= 0  &&  pixels[xx+((yy-1) * map.getWidth())] == 0xFFffffff || yy-1 >= 0  &&  (pixels[xx+((yy-1) * map.getWidth())] == 0xFF606060 || pixels[xx+((yy-1) * map.getWidth())] == 0xFFFFE359 )) {
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TILE_WALL1);
						}
						
					}else if(pixelAtual == 0xFFB200FF) {
						Saver saver = new Saver(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Entity.SAVER[0]);
						Game.entities.add(saver);
					}else if(pixelAtual == 0xFF0026FF) {
						//Jogador 
						Game.player.setX(xx*TILE_SIZE);
						Game.player.setY(yy*TILE_SIZE);
					}else if(pixelAtual == 0xFFFF0000) {
						//Inimigo
						Enemy enemy = new Enemy(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Entity.ENEMY1_RIGHT[0]);
						Game.entities.add(enemy);
						Game.enemies.add(enemy);
					}else if(pixelAtual == 0xFFFFD800) {
						//Moeda 
						Coin coin = new Coin(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Entity.COIN[0],1);
						Game.entities.add(coin);	
						Game.coin.add(coin);
						Player.maxCoins+=coin.getValue();
					}else if(pixelAtual == 0xFF606060) {
						//Sensor
						Sensor sensor = new Sensor(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Tile.TILE_WALL1);
						Game.entities.add(sensor); 
					}else if(pixelAtual == 0xFFFFE359) {
						//Pode virar moeda
						Tranformer trans = new Tranformer(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Tile.TILE_WALL1,0);
						Game.entities.add(trans);
						Game.transformer.add(trans);
					}else if(pixelAtual == 0xFF007F46) {
						//Final 
						FinishTile finale = new FinishTile(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Tile.TILE_FINAL);
						Game.entities.add(finale);
						Game.finish.add(finale);
					}else if(pixelAtual == 0xFF00fa0a) {
						//Adiciona arvores
						Tree tree = new Tree(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,1,Tile.TREE);
						Game.entities.add(tree);
					}
					
				}
			}
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	//reinicia todos os itens do jogo 
	public static void restartGame(String level){
		Game.player.TOTAL_COINS = Game.player.TOTAL_COINS+Game.player.currentCoins;
		Sound.gameOver.stop();
		Sound.music.loop();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.entities.clear();
		Game.enemies.clear();
		Game.transformer.clear();
		Game.finish.clear();
		Game.coin.clear();
		Game.player = new Player(WIDTH/2 - 30,HEIGHT/2,World.TILE_SIZE,World.TILE_SIZE,Player.speed,Entity.PLAYER_SPRITE_RIGHT[0]);
	    Player.maxCoins = 0;
	    Player.currentCoins = 0;
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.coin = new ArrayList<Coin>();
		Game.transformer = new ArrayList<Tranformer>();
		Game.finish = new ArrayList<FinishTile>();
		Game.world = new World("/"+level);
		Player.life=Player.maxLife;
		Game.entities.add(Game.player);
		Sensor.sense=false;
		System.out.println(Game.player.TOTAL_COINS);
		return;
	}
	
	//confere se est� livre proximo Tile
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
