import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class GameBGround extends JFrame implements Runnable {
	
	public GamePlaying gp = new GamePlaying();
	
	public GameBGround(){
		//Oyunun kendisi böyle çalýþýyor
		setBounds(300,50,700,650);
		setTitle("Brick Breaker Test");
		setResizable(false);
		setVisible(true);
		add(gp);
		
	}

		public void run() {
				while(true) {
					
					if(GamePlaying.isLevelFinished==true){
						BrickGenerator.toSecond=true;
						gp= new GamePlaying();
						add(gp);
					}
					
					try{
						Thread.sleep(10);
					}
					catch(Exception e){
					
					}
					
				}

				
			
		}

	}
	
	

