package com.viduan.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viduan.entities.Coin;
import com.viduan.entities.Entity;
import com.viduan.entities.Player;
import com.viduan.entities.Sensor;
import com.viduan.main.Game;

public class Tranformer extends Entity{

	
	
	public Tranformer(double x, double y, int width, int height, double speed, BufferedImage sprite,int value) {
		super(x, y, width, height, speed, sprite);
	
	}
	
	public void tick() {
		depth = 3;
		test();
		super.tick();
		
	}
	public void render(Graphics g) {
		super.render(g);
	}
	
	//Adiciona moeda na aonde foi pre-determinado
	public  void test() {
		if(Sensor.sense == true &&  change == false) {
			for(int i = 0; i < Game.transformer.size();i++){
				Tranformer e = Game.transformer.get(i);
				if(e instanceof Tranformer) {
					if(e.change == false && e.sprite != Tile.TILE_WALL_CHANGE) {
						e.sprite = Tile.TILE_WALL_CHANGE;
						Coin coin = new Coin(e.getX(),e.getY(),World.TILE_SIZE,World.TILE_SIZE,1,Entity.COIN[0],1);
						Game.entities.add(coin);	
						Game.coin.add(coin); 
						Player.maxCoins+=coin.getValue();
						Sensor.sense=false;
						 
						
					}
				}	
			}	
		}
			
	}
	
}
