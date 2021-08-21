package com.viduan.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.viduan.main.Game;
import com.viduan.world.Camera;
import com.viduan.world.World;

public class BulletShoot extends Entity {

	private double dx;
	private double dy;
	private double spd = 4.0 ;
	
	private int life = 30,curLife= 0;
	
	public BulletShoot(int x, int y, int width, int height, double speed, BufferedImage sprite,double dx,double dy) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
		this.dx = dx;
		this.dy = dy;
	}

	
	public void tick() {
		if(World.isFreeDynamic((int)(x+=(dx*spd)), (int)(y+=(dy*spd)), 3,3)) {			
			x+=dx*spd;
			y+=dy*spd;
		}else {
			Game.bullet.remove(this);
			World.generateParticles(100, (int)x,(int)y);
			return;
		}
		curLife++;
		if(curLife == life) {
			Game.bullet.remove(this);
			World.generateParticles(100, (int)x,(int)y);
			return;
		
		}		
	}
	
	public boolean isColidding(int xnext,int ynext) {
		Rectangle bulletCur = new Rectangle(xnext,ynext,World.TILE_SIZE,World.TILE_SIZE);
		for(int i = 0;i < Game.bullet.size();i++) {
			BulletShoot e = Game.bullet.get(i);
			if(e == this) 
				continue;
				
			Rectangle targetBullet = new Rectangle(e.getX(),e.getY(),World.TILE_SIZE,World.TILE_SIZE);	
			if(bulletCur.intersects(targetBullet)) {
				
	
				return true;
				
			}
			
		}
		return false;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(this.getX()-Camera.x,this.getY()-Camera.y,3,3);
	}
	
}
