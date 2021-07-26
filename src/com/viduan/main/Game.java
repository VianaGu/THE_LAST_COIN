/*
 *
 *
 *Author: Gustavo Oliveira Viana 
 *Data inicio: 07/2021 
 *
 * 
 * 
 */
package com.viduan.main;

//Imports do java 
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

//Imports Internos 
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


//Inicio
public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{
	
	
	//area de variaveis
	//JFrame
	private static final long serialVersionUID = 1L;
	public static JFrame frame; 
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 416;
	public static final int HEIGHT = 225;
	public static final int SCALE = 2;
	
	//Fundo do JFrame
	private BufferedImage image;
	
	//Variaveis do mundo
	public static int CUR_LEVEL = 1,MAX_LEVEL = 3;
	public static World world;
	
	
	//Lista de entidades 
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Coin> coin;
	public static List<Tranformer> transformer;
	public static List<FinishTile> finish;
 	
	//Sistema para pegar Sprites
	public static Spritesheet spritesheet;
	
	//Declara variavel do player
	public static Player player;

	//User Interface
	public UI ui;

	//Sistema de som
	public Sound sound;
	
	//Sistema de interação com o mundo 
	public static boolean seletor = false;
	
	//Inicia o GAMESTATE
	public static String gameState = "MENU";
	
	//Sistema de gameOver
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	//Menu
	public Menu menu;
	
	//Sistema de salvar jogo 
	public static boolean saveGame = false;
	
	//Metodo construtor 
	public Game(){
		Sound.music.loop();//looping musica 
		addKeyListener(this);//escuta o teclado
		addMouseListener(this);//escuta o mouse 
		addMouseMotionListener(this);//escuta o mouse 
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));//muda o tamanho da janela 
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
	
	//Metodo main
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	

	public  void verifyCoin() {
		//TODO 
	}
	
	public void tick(){
		
		if(gameState == "NORMAL") {
		
			
			//Caso vida do jogador chegar em zero 
			if(Player.life <= 0) {
				gameState = "GAME_OVER";
			}

			this.restartGame = false;
			
			//"Liga" todas as entities
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			//Logica de passar de level por coletar todas as moedas
			if(Player.currentCoins == Player.maxCoins && enemies.size() ==0) {
				proxLevel();
			
			}
			//Logica de TILE final 
			if(FinishTile.finalLevel) {
				proxLevel();
				FinishTile.finalLevel=false;
			}
			
			//Salva o jogo 
			if(this.saveGame) {
				
				String[] opt1 = {"level"};
				int[] opt2 = {this.CUR_LEVEL};
				Menu.saveGame(opt1,opt2,0);
				this.saveGame = false;
				
			}
		
		//Sistema de mensagem de game over 
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
					else
						this.showMessageGameOver = true;
			}
			
			//Recomeça o jogo 
			if(restartGame ) {
				restartGame();
			}
		
		//Inicia o metodo do MENU
		}else if(gameState == "MENU") {
			
			menu.tick();
		}	
	}
	//Restart game
	public void restartGame() {
		this.restartGame = false;
		this.gameState = "NORMAL";
		CUR_LEVEL = 1;
		String newWorld = "level"+CUR_LEVEL+".png";
		World.restartGame(newWorld);
	}
	
	//Sistema de proximo level 
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


	//metodo de renderização
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
		
	
	
	
	//Game looping
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

	//Observa o teclado 
	@Override
	public void keyPressed(KeyEvent e){
		
		//Escuta tecla p/ confirmar ações 
		if(e.getKeyCode() == KeyEvent.VK_E) {
			seletor = true;
		}
		
		//Sistema de "agachar"
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			Player.speed =0.5;
		}
		//Entra no menu 
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
		
		//Confirmação no menu
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		//tecla de movimentação para a direita
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			player.right = true;
			player.moved = true;	
		}//tecla de movimentação para a esquerda 
		else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = true;
			player.moved = true;
		}
		//tecla para o pulo 
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}//Seleção de opcões no menu
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			if(gameState == "MENU") {
				menu.up = true;
			    Sound.troca.play();
			}
		}//Seleção de opcões no menu
		else if(e.getKeyCode() == KeyEvent.VK_S|| e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(gameState == "MENU") {
				menu.down = true;
				Sound.troca.play();
			}
		}
	}

	//caso a tecla seja solta 
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
	//eventos de mouse 
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
