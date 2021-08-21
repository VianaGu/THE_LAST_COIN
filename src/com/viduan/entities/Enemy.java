package com.viduan.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viduan.main.Game;
import com.viduan.world.World;

public class Enemy extends Entity{
	private int framesAnimation = 0;
	private int maxFrames = 10;
	
	private int maxSprite = 2;
	private int curSprite = 0; 
	
	private double gravity = 2;
	
	public int life = 5;
	
	public boolean right = true, left = false; 

	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	public void tick() {
		depth = 1;
		this.checkCollidingBullet();
		if(World.isFree((int)x, (int)(y+gravity)) ) {
			y+=gravity;
		}
		if(right) {
			if(World.isFree((int)(x+speed),(int) y)) {
				x+=speed;
				if(World.isFree(this.getX()+World.TILE_SIZE, this.getY()+1)) {
					right = false;
					left = true;
				}
			}else {
				right = false;
				left = true;
			}
		}
		if(left) {
			if(World.isFree((int)(x-speed),(int) y)) {
				x-=speed;
				if(World.isFree(this.getX()-World.TILE_SIZE, this.getY()+1)) {
					right = true;
					left = false;
				}
			}else {
				right = true;
				left = false;
			}
		}
	}
	
	public void checkCollidingBullet() {
		for(int i =0; i<Game.bullet.size();i++) {
			Entity e = Game.bullet.get(i);
			if(e instanceof BulletShoot) {
				if(Entity.isColidding(this,e)) {
					life-= Game.rand.nextInt(7);
					Game.bullet.remove(i);
					System.out.print("To aqui");
					return;
				}
			}
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
		if(right) {
			sprite = Entity.ENEMY1_RIGHT[curSprite];
		}else if(left) {
			sprite = Entity.ENEMY1_LEFT[curSprite];
		}
		
		super.render(g);
	}
	 
}
