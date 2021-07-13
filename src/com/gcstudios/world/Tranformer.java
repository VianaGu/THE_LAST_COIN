package com.gcstudios.world;

import java.awt.image.BufferedImage;

import com.gcstudios.entities.Coin;
import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Player;
import com.gcstudios.entities.Sensor;
import com.gcstudios.entities.Unsensor;
import com.gcstudios.main.Game;

public class Tranformer extends Entity{

	
	
	public Tranformer(double x, double y, int width, int height, double speed, BufferedImage sprite,int value) {
		super(x, y, width, height, speed, sprite);
		
	}
	
	public void tick() {
		depth = 2;
		test();
		super.tick();
		
	}
	
	public void test() {
		if(Sensor.sense == true &&  change == false) {
			for(int i = 0; i < Game.transformer.size();i++){
				Tranformer e = Game.transformer.get(i);
				if(e instanceof Tranformer) {
					if(e.change == false) {
						e.sprite = Tile.TILE_WALL_CHANGE;
						Coin coin = new Coin(e.getX(),e.getY(),World.TILE_SIZE,World.TILE_SIZE,1,Entity.COIN,1);
						Game.entities.add(coin);	
						Game.coin.add(coin); 
						Player.maxCoins+=coin.getValue();
						Sensor.sense=false;
						e.change = true;
					}
				}	
			}	
		}
			
	}
	
}
