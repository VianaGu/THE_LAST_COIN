 package com.viduan.graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.viduan.entities.Player;
import com.viduan.main.Game;

public class UI {

	public static int saveGameFrames = 0; 
	public static boolean showMessageSavegame = true;
	public static int count = 0;
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(10, 10, 250, 30);
		g.setColor(Color.green);
		g.fillRect(10, 10,(int) ((Player.life/Player.maxLife)*250), 30); 
		g.setColor(Color.white);
		g.drawRect(10, 10, 250, 30);
		if(Player.maxCoins < 10) {
			g.setFont(new Font("arial",Font.BOLD,17));
			g.drawString("Moedas: "+ Player.currentCoins + "/"+Player.maxCoins, Game.WIDTH*Game.SCALE-100, 30);
		}else {
			g.setFont(new Font("arial",Font.BOLD,17));
			g.drawString("Moedas: "+ Player.currentCoins + "/"+Player.maxCoins, Game.WIDTH*Game.SCALE-115, 30);
		}
		
			if(Game.SaveGameShow && count <= 120 ) {	
				count+=1;
				this.saveGameFrames++;
				if(this.saveGameFrames == 15) {
					this.saveGameFrames = 0;
					if(this.showMessageSavegame)
						this.showMessageSavegame = false;
						else
							this.showMessageSavegame = true;
					
					System.out.println(count);
					if(count >= 120) {
						count=0;
						Game.SaveGameShow=false;
					}
			}	
		if(showMessageSavegame == true) {
			g.setFont(new Font("arial",Font.BOLD,20));
			g.setColor(Color.red);
			g.drawString("Jogo Salvo ", Game.WIDTH*Game.SCALE-230, 30);
		}
		
		
	}
		
	

	}
	}
