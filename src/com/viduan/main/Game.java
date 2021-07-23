/*
 *Author: Gustavo Oliveira Viana 
 * 
 * 
 * 
 */
package com.viduan.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import com.viduan.entities.Coin;
import com.viduan.entities.Enemy;
import com.viduan.entities.Entity;
import com.viduan.entities.FinishTile;
import com.viduan.entities.Player;
import com.viduan.graficos.Spritesheet;
import com.viduan.graficos.UI;
import com.viduan.world.Camera;
import com.viduan.world.Tranformer;
import com.viduan.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{
	
	
	//area de variaveis
	private static final long serialVersionUID = 1L;
	public static JFrame frame; 
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 416;
	public static final int HEIGHT = 225;
	public static final int SCALE = 2;
	
	private BufferedImage image;
	
	public static int CUR_LEVEL = 1,MAX_LEVEL = 2;
	public static World world;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Coin> coin;
	public static List<Tranformer> transformer;
	public static List<FinishTile> finish;
 	
	public static Spritesheet spritesheet;
	public static Player player;

	public UI ui;

	public Sound sound;
	
	public static boolean seletor = false;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public Menu menu;
	
	public static boolean saveGame = false;
	
	//Metodo construtor 
	public Game(){
		Sound.music.loop();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		coin = new ArrayList<Coin>();
		transformer = new ArrayList<Tranformer>();
		finish = new ArrayList<FinishTile>();
		player = new Player(WIDTH/2 - 30,HEIGHT/2,World.TILE_SIZE,World.TILE_SIZE,Player.speed,Entity.PLAYER_SPRITE_RIGHT[0]);
		world = new World("/level1.png"); 	
		ui = new UI();
		
		entities.add(player);
		
		menu = new Menu();
		
	}
	
	//Tela do jogo
	public void initFrame(){
		frame = new JFrame("The last coin");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public  int verifyCoin() {
		Coin[] a = {};
		int n = 0;
		for(int i = 0; i < coin.size();i++) {
			a[i] = coin.get(i);
			if(a[i].getGotcha() == true) {
				n=1;
			}else {
				n=0;
			}
		}
		return n;
	}
	
	public void tick(){
		
		if(gameState == "NORMAL") {
		
			
			if(Player.life <= 0) {
				gameState = "GAME_OVER";
			}	
			this.restartGame = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			//Logica de passar de level por coletar todas as moedas
			if(Player.currentCoins == Player.maxCoins && enemies.size() ==0) {
				proxLevel();
			
			}
			if(FinishTile.finalLevel) {
				proxLevel();
				FinishTile.finalLevel=false;
			}
			
			if(this.saveGame) {
				
				String[] opt1 = {"level","posX","posY","life"};
				int[] opt2 = {this.CUR_LEVEL,player.getX(),player.getY(),(int) player.life};
				Menu.saveGame(opt1,opt2,0);
				this.saveGame = false;
				
			}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
					else
						this.showMessageGameOver = true;
			}
			if(restartGame ) {
				restartGame();
			}
		}else if(gameState == "MENU") {
			
			menu.tick();
		}else if(gameState == "proxLevel") {
			
		}
		
		
		
	}
	
	public void restartGame() {
		this.restartGame = false;
		this.gameState = "NORMAL";
		CUR_LEVEL = 1;
		String newWorld = "level"+CUR_LEVEL+".png";
		World.restartGame(newWorld);
	}
	
	public void proxLevel() {
		Camera.x = Camera.clamp((int)getX()-Game.WIDTH / 2, 0, World.WIDTH*World.TILE_SIZE - Game.WIDTH );
		Camera.y = Camera.clamp((int)getY()-Game.HEIGHT / 2, 0, World.HEIGHT*World.TILE_SIZE - Game.HEIGHT);
		gameState = "MENU";
		menu.pause = true;
		CUR_LEVEL++;
		if(CUR_LEVEL > MAX_LEVEL){
			CUR_LEVEL = 1;
		}
		String newWorld = "level"+CUR_LEVEL+".png";
		//System.out.println(newWorld);
		World.restartGame(newWorld);
	}


	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(122,102,255));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		g = bs.getDrawGraphics();
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial",Font.BOLD,36));
			g.setColor(Color.white);
			g.drawString("Game Over",(WIDTH*SCALE) / 2 - 90,(HEIGHT* SCALE) / 2 - 20);
			g.setFont(new Font("arial",Font.BOLD,32));
			if(showMessageGameOver)
				g.drawString(">Pressione Enter para reiniciar<",(WIDTH*SCALE) / 2 - 230,(HEIGHT* SCALE) / 2 + 40);
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}
		
	
	
	
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_E) {
			seletor = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			Player.speed =0.5;
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	
			gameState = "MENU";
			menu.pause = true;
			
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			player.right = true;
			player.moved = true;
		}else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = true;
			player.moved = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			if(gameState == "MENU") {
				menu.up = true;
			    Sound.troca.play();
			}
			}else if(e.getKeyCode() == KeyEvent.VK_S|| e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(gameState == "MENU") {
					menu.down = true;
					Sound.troca.play();
				}
			}
		}


	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_E) {
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			Player.speed = 2.5;
		}
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = false;
			player.moved = false;
		}else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = false;
			player.moved = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = false;
		}if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			if(gameState == "MENU") 
				menu.up = false;
			}else if(e.getKeyCode() == KeyEvent.VK_S|| e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(gameState == "MENU") 
					menu.down = false;
				
			}
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	
}
