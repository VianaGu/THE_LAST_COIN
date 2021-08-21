package com.viduan.entities;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viduan.main.Game;
import com.viduan.main.Sound;
import com.viduan.world.Camera;
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
	
	public int mx, my;
	public boolean mouseShoot = false;
    public boolean shoot = false;
    public static boolean hasGun = false;
	public boolean equipGun = true;
	public int ammo = 100;
	public int z = 0;
	
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
		depth = 0;
	}
	
	public void tick(){
	
		//debug = true;
		
		
		
		
		//Lï¿½gica dano no inimigo 
		if(World.isFree((int)x+1, (int)(y+gravity)) && isJumping == false ) {
			y+=gravity;
			
			for(int i = 0; i < Game.entities.size();i++){
				Entity e = Game.entities.get(i);
				if(e instanceof Enemy) {
					if(Entity.isColidding(this,e) ) {
							//Colisï¿½o na cabeï¿½a do inimigo 
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
		

		//Lï¿½gica do pulo
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
			
			
		//Colisï¿½o lateral	
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
			//Colisï¿½o com o sensor
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
			//Detectar colisão com a moeda e aumentar contagem 
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
				if(Entity.isColidding(this, e) && Game.seletor == true && ((Tree)e).sprite!=Tile.TREE_EMPTY && life<maxLife) {
					Sound.eat.play();
					Sound.eat2.play();
					Sound.eat3.play();
					maisVida =true;
					e.sprite = Tile.TREE_EMPTY;
				}
			}
			//Sistema para salvar o jogo 
			if(e instanceof Saver) {
				if(Entity.isColidding(this, e)) {
					if(Saver.showMessage==true) {
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
		checkCollisonGun();
		
		if(mouseShoot) {
			mouseShoot = false;
			if(this.equipGun == false) {
				this.equipGun = true;
			}
			else if(hasGun && ammo > 0) {
			Sound.soundShoot.play();			

			ammo--;
			double angle = 0;
			int px= 0,py = 8;
			if(dir == 1) {
				 px = 18;
				 angle = Math.atan2(my - (this.getY()+py - Camera.y)  ,mx - (this.getX()+8 - Camera.x));
			}else {
				 px = -8;
				 angle = Math.atan2(my - (this.getY()+py - Camera.y)  ,mx - (this.getX()+8 - Camera.x));
			}
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);

			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,4.0,null,dx,dy);
			Game.bullet.add(bullet);
			
			}
	}
		
	}
	
	public void checkCollisonGun() {
		for(int i = 0; i < Game.weapon.size();i++) {
			Entity atual = Game.weapon.get(i);
			if(atual instanceof Weapon) {
				
				if(Entity.isColidding(this, atual)) {
					hasGun = true;
					Game.entities.remove(atual);
					Game.weapon.remove(atual);
					
				}
			}
		}
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
				if(this.hasGun && equipGun) {
					g.drawImage(Entity.WEAPON_PICKED[0],this.getX()+7-Camera.x,this.getY()-4-Camera.y,null);
				}
			}else if(dir == -1) {
				sprite = Entity.PLAYER_SPRITE_LEFT[curSprite];
				if(this.hasGun && equipGun) {
					g.drawImage(Entity.WEAPON_PICKED[1],this.getX()+5-Camera.x,this.getY()-4-Camera.y,null);
				}

			}
		
		}else {
			if(dir == 1) {
				sprite = Entity.PLAYER_SPRITE_RIGHT_STOP;
				if(this.hasGun && equipGun) {
					g.drawImage(Entity.WEAPON_PICKED[0],this.getX()+7-Camera.x,this.getY()-4-Camera.y,null);
				}
			}else if(dir == -1) {
				sprite = Entity.PLAYER_SPRITE_LEFT_STOP;
				if(this.hasGun && equipGun) {
					g.drawImage(Entity.WEAPON_PICKED[1],this.getX()+5-Camera.x,this.getY()-4-Camera.y,null);
				}
			}
		
		}
		super.render(g);
	}

	


}
