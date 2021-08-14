package com.viduan.entities;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viduan.main.Game;
import com.viduan.main.Sound;
import com.viduan.world.Tile;
import com.viduan.world.Tranformer;
import com.viduan.world.Tree;
import com.viduan.world.World;


public class Player extends Entity{
	
	public static boolean maisVida = false;

	public boolean moved = false;
	public boolean right, left;
	public static double speed = 2.5;
	
	public static int maxLife = 100;
	public static double life = 100;
	
	public static int TOTAL_COINS = 0;
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
		
		
		
		
		//L�gica dano no inimigo 
		if(World.isFree((int)x+1, (int)(y+gravity)) && isJumping == false ) {
			y+=gravity;
			
			for(int i = 0; i < Game.entities.size();i++){
				Entity e = Game.entities.get(i);
				if(e instanceof Enemy) {
					if(Entity.isColidding(this,e) ) {
							//Colis�o na cabe�a do inimigo 
							vspd = -7;
							jump = false;
							Sound.jump.play();
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
		

		//L�gica do pulo
		vspd+=gravity;
		if(!World.isFree(getX(), (int)(y+1)) && jump) {
			vspd = -7;
			jump = false;
			Sound.jump.play();
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
			
			
		//Colis�o lateral	
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
					if(Entity.rand.nextInt(100) < 10) {
						life-=5;
						Sound.hurt.play();
					}
				}
			}
			//Colis�o com o sensor
			if(e instanceof Sensor) {
				if(Entity.isColidding(this, e)) {
					Sensor.sense = true;
					e.sprite = Tile.TILE_WALL_CHANGE;
					break;
				}
			}
			//finish
			if(e instanceof FinishTile) {
				if(Entity.isColidding(this, e)) {
					FinishTile.finalLevel = true;	
				}
			}
			//Detectar colis�o com a moeda e aumentar contagem 
			if(e instanceof Coin) {
				if(Entity.isColidding(this, e) ) {
					Game.entities.remove(i);
					((Coin) e).gotcha=true;
					Player.currentCoins++;
					Sound.coin.play();
					break;
				}
			}
			if(e instanceof Tree) {
				if(Entity.isColidding(this, e) && Game.seletor == true) {
					Game.seletor=false;
					e.sprite=Tile.TREE_EMPTY;
					this.maisVida = true;
				}
			}
			//Sistema para salvar o jogo 
			if(e instanceof Saver) {
				if(Entity.isColidding(this, e)) {
					if(Game.SIM) {
						Saver.salvando();
						Sound.salvo.play();
						Game.SaveGameShow = true;
						Game.seletor = false;
						Game.SIM=false;
						Game.NAO=false;
						for(int a = 0;a < Game.entities.size(); a++) {
							Entity ee = Game.entities.get(a);
								if(ee instanceof Saver) {
									Game.entities.remove(a);
								}
						}
					}
				}
			}
			
			if(e instanceof Saver) {
				if(Entity.isColidding(this, e)) {
					if(Game.NAO) {
						Game.seletor=false;
						Game.NAO=false;
						Game.SIM=false;
						for(int a = 0;a < Game.entities.size(); a++) {
							Entity ee = Game.entities.get(a);
								if(ee instanceof Saver) {
									Game.entities.remove(a);
								}
						}
					}
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
