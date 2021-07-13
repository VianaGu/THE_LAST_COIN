package com.gcstudios.entities;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;
import com.gcstudios.main.Sound;
import com.gcstudios.world.Tile;
import com.gcstudios.world.Tranformer;
import com.gcstudios.world.World;


public class Player extends Entity{

	public boolean moved = false;
	public boolean right, left;
	public static double speed = 2.5;
	
	public static int maxLife = 100;
	public static double life = 100;
	
	public static int currentCoins = 0;
	public static int maxCoins;
	
	private double gravity = 0.3;
	private double vspd = 0;
	
	public int dir = 1;
	
	public boolean jump = false;
	
	public boolean isJumping = false;
	public int jumpHeight = 1;
	public int jumpFrames = 0;
	
	private int framesAnimation = 0;
	private int maxFrames = 10;
	
	private int maxSprite = 2;
	private int curSprite = 0; 
	
	private Tranformer t;
	private Sensor sensor;
	
	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
		
	}
	
	public void tick(){
		depth = 2;
		//debug = true;
		
		
		//Lógica dano no inimigo 
		if(World.isFree((int)x+1, (int)(y+gravity)) && isJumping == false ) {
			y+=gravity;
			for(int i = 0; i < Game.entities.size();i++){
				Entity e = Game.entities.get(i);
				if(e instanceof Enemy) {
					if(Entity.isCollidingPerfect(this.getX(), this.getY(),e.getX(),e.getY(),this.spritePath(),e.spritePath()) ) {
							//Colisão na cabeça do inimigo 
							vspd = -7;
							jump = false;
							//tirando vida do inimigo
							((Enemy)e).life--;
							if(((Enemy)e).life == 0){
								//Destruir Inimigo 
								((Enemy) e).destroySelf();
								
								break;
							}
					
					}
				}else {
					jumpHeight = 32;
				}
			}
			
		}
		

		//Lógica do pulo
		vspd+=gravity;
		if(!World.isFree(getX(), (int)(y+1)) && jump) {
			vspd = -7;
			jump = false;
		}
		if(!World.isFree(getX(),(int)(y+vspd))) {
			int signVsp = 0;
			if(vspd >= 0) {
				signVsp = 1;
			}else {
				signVsp = -1;
			}
			while(World.isFree(getX(), (int)(y+signVsp))) {
				y = y+signVsp;
			}
			vspd = 0;
		}
		y = y +vspd;
			
			
		//Colisão lateral	
		if(right && World.isFree((int)(x+speed), (int)y)) {
			x+=speed;
			dir = 1;

		}else if(left && World.isFree((int)(x-speed), (int)y)) {
			x-=speed;
			dir = -1; 
		}	
		
		
		//Detectar dano 		
		for(int i = 0; i < Game.entities.size();i++){
			Entity e = Game.entities.get(i);
			if(e instanceof Enemy) {
				if(Entity.isColidding(this, e)) {
					if(Entity.rand.nextInt(100) < 5) {
					life-=5;
					Sound.hurtEffect.play();
					}
				}
			}
		}	
		//Colisão com o sensor
		for(int i = 0; i < Game.entities.size();i++){
			Entity e = Game.entities.get(i);
			if(e instanceof Sensor) {
				if(Entity.isColidding(this, e)) {
					Sensor.sense = true;
					e.sprite = Tile.TILE_WALL_CHANGE;
					break;
				}
			}
		
		}
		
		
		
		//Detectar colisão com a moeda e aumentar contagem 
		for(int i = 0; i < Game.entities.size();i++){
			Entity e = Game.entities.get(i);
			if(e instanceof Coin) {
				if(Entity.isColidding(this, e) ) {
					Game.entities.remove(i);
					Player.currentCoins++;
					Sound.coin.play();
					break;
				}
			}
		}	
		
		
		updateCamera();
	}
	
	
	public void render(Graphics g) {
		if(moved == true) {
			framesAnimation ++;
			if(framesAnimation == maxFrames) {
				curSprite++;
				framesAnimation = 0; 
				if(curSprite == maxSprite) {
					curSprite = 0;
				}
			}
			if(dir == 1) {
				sprite = Entity.PLAYER_SPRITE_RIGHT[curSprite];
			}else if(dir == -1) {
				sprite = Entity.PLAYER_SPRITE_LEFT[curSprite];
			}
		
		}else {
			if(dir == 1) {
				sprite = Entity.PLAYER_SPRITE_RIGHT_STOP;
			}else if(dir == -1) {
				sprite = Entity.PLAYER_SPRITE_LEFT_STOP;
			}
		
		}
		super.render(g);
	}

	


}
