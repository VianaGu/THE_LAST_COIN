package com.viduan.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.viduan.main.Game;
import com.viduan.world.Camera;
import com.viduan.world.Node;
import com.viduan.world.Vector2i;
import com.viduan.world.World;

public class Entity {
	
	//inicia todas a sprites de formas estatica 
	public static BufferedImage[] SAVER = {Game.spritesheet.getSprite(96, 0, World.TILE_SIZE, World.TILE_SIZE),Game.spritesheet.getSprite(128, 0, World.TILE_SIZE, World.TILE_SIZE) };
	public static BufferedImage PLAYER_SPRITE_LEFT_STOP = Game.spritesheet.getSprite(32, 224, 32, 32);
	public static BufferedImage PLAYER_SPRITE_RIGHT_STOP = Game.spritesheet.getSprite(0, 224, 32, 32);
	public static BufferedImage[] PLAYER_SPRITE_RIGHT = {Game.spritesheet.getSprite(0, 288, World.TILE_SIZE, World.TILE_SIZE),Game.spritesheet.getSprite(32, 288, World.TILE_SIZE, World.TILE_SIZE)};
	public static BufferedImage[] PLAYER_SPRITE_LEFT = {Game.spritesheet.getSprite(0, 256, World.TILE_SIZE, World.TILE_SIZE),Game.spritesheet.getSprite(32, 256, World.TILE_SIZE, World.TILE_SIZE)};
	public static BufferedImage[] COIN = {Game.spritesheet.getSprite(288, 0, World.TILE_SIZE, World.TILE_SIZE),Game.spritesheet.getSprite(224, 0, World.TILE_SIZE, World.TILE_SIZE),Game.spritesheet.getSprite(256, 0, World.TILE_SIZE, World.TILE_SIZE)};
	public static BufferedImage[] ENEMY1_LEFT = {Game.spritesheet.getSprite(288, 256, World.TILE_SIZE, World.TILE_SIZE),Game.spritesheet.getSprite(256, 256, World.TILE_SIZE, World.TILE_SIZE)};
	public static BufferedImage[] ENEMY1_RIGHT = {Game.spritesheet.getSprite(288, 288, World.TILE_SIZE, World.TILE_SIZE),Game.spritesheet.getSprite(256, 288, World.TILE_SIZE, World.TILE_SIZE)};
	
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected double speed;
	
	public boolean change = false;
	
	//Sistema para definir é renderizado mais em cima 
	public static int depth;

	protected List<Node> path;
	
	//Sistema de debug de mascara de contato
	public boolean debug = false;
	
	public  BufferedImage sprite;
	
	public static Random rand = new Random();
	
	public Entity(double x,double y,int width,int height,double speed,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		
		
		
	} 
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity n0,Entity n1) {
			if(n1.depth < n0.depth)
				return +1;
			if(n1.depth > n0.depth)
				return -1;
			return 0;
		}
		
	};
	
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*World.TILE_SIZE - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*World.TILE_SIZE - Game.HEIGHT);
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	public BufferedImage spritePath() {
		return this.sprite;
	}
	
	
	public void tick(){}
	
	public double calculateDistance(int x1,int y1,int x2,int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;
				if(x < target.x * World.TILE_SIZE) {
					x++;
				}else if(x > target.x * World.TILE_SIZE) {
					x--;
				}
				
				if(y < target.y * World.TILE_SIZE) {
					y++;
				}else if(y > target.y * World.TILE_SIZE) {
					y--;
				}
				
				if(x == target.x * World.TILE_SIZE && y == target.y * World.TILE_SIZE) {
					path.remove(path.size() - 1);
				}
				
			}
		}
	}
	
	public static boolean isColidding(Entity e1,Entity e2){
		
		Rectangle e1Mask = new Rectangle(e1.getX()+6,e1.getY(),e1.getWidth()-12,e1.getHeight());
		Rectangle e2Mask = new Rectangle(e2.getX()+6,e2.getY(),e2.getWidth()-12,e2.getHeight());
		
		
		return e1Mask.intersects(e2Mask);
	}
	
	public static boolean isCollidingPerfect(int x1, int y1,int x2,int y2,BufferedImage sprite1, BufferedImage sprite2) {
		//criando array dentro da imagem 
		int[] pixels1 = new int[sprite1.getWidth()*sprite1.getHeight()]; 
		sprite1.getRGB(0, 0,sprite1.getWidth(),sprite1.getHeight(),pixels1, 0, sprite1.getWidth()); 
		int[] pixels2 = new int[sprite2.getWidth()*sprite2.getHeight()]; 
		sprite1.getRGB(0, 0,sprite2.getWidth(),sprite2.getHeight(),pixels2, 0, sprite2.getWidth()); 
		
		/*confirma se é transparente
		if(pixels1[0] == 0x00FFFFFF || pixels2[0] == 0x00FFFFFF) {
			
		}
		*/
		
		for(int xx1=0;xx1 < sprite1.getWidth();xx1++) {
			for(int yy1=0;yy1 < sprite1.getHeight();yy1++) {
				for(int xx2 =0;xx2<sprite2.getWidth();xx2++) {
					for(int yy2 =0; yy2< sprite2.getHeight();yy2++) {
						int pixelAtual1 = pixels1[xx1+yy1*sprite1.getWidth()];
						int pixelAtual2 = pixels2[xx2+yy2*sprite2.getWidth()];
						if(pixelAtual1 == 0x00FFFFFF || pixelAtual2 == 0x00FFFFFF) {
							continue;
						}
						if(xx1+x1 == xx2+x2 && yy1+y1 == yy2+y2) {
							return true;
						}
						
					}
				}
			}
		}
		return false;
	}
	
	public void render(Graphics g) {
		
		g.drawImage(sprite,this.getX() - Camera.x,this.getY() - Camera.y,null);
		//Mascara para debugar
		if(debug) {
			g.setColor(Color.red);	
			g.fillRect(this.getX()+6 +  - Camera.x,this.getY()  - Camera.y,World.TILE_SIZE-12,World.TILE_SIZE);
		}
		
		
	}
	
}
