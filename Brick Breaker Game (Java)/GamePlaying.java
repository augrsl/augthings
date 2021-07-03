import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class GamePlaying extends JPanel implements KeyListener,ActionListener,Runnable {

	private boolean playActive = false; // controls pause of the game
	
	private boolean close=false; // controls closing of the game
	 
	private int lifeNum=3; // #chance
	
	private int score = 0; // score
	
	public static int difficulty;
	
	public static boolean isLevelFinished=false;
	
//	public static ArrayList<Integer> highScores = new ArrayList<Integer>();
//	
//	static{
//		for(int i =0;i<11;i++){
//			highScores.add(0);
//		}
//	}
	
	public static int brickNum=9; // #brick

	private Timer timer;
	private int delay=8;
	
	private int playerPosition=310;
	
	private int ballPositionX=350;
	private int ballPositionY=530;
	private int ballDirectionX=-5;
	private int ballDirectionY=-3;

	
	public BrickGenerator map = new BrickGenerator();
	

	public GamePlaying(){
		
		if(BrickGenerator.toSecond==false&&BrickGenerator.toSecond==false){
			map.level1();
		}
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
	}
	
	
	
	public void paint(Graphics g){ // Overrided paint method
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(1, 1, 692, 592);
		
		map.draw((Graphics2D)g);
		
		g.setColor(Color.white); 
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		g.setColor(Color.green);
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString("Score: "+score, 40, 30);
		
		g.setColor(Color.red);
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString("You have '"+lifeNum+"' chances", 430, 30);
		
		g.setColor(Color.red);
		g.fillRect(playerPosition, 550, 100, 8);
	
		g.setColor(Color.blue);
		g.fillOval(ballPositionX, ballPositionY, 20, 20);
		
		if(ballPositionY>570){ // Decreasing #Life
			playActive=false;
			lifeNum--;
			ballPositionX=playerPosition+30;
			ballPositionY=530;

			if(lifeNum==0){
				close=true;
			}
		}
		
		
		g.dispose();
	}
	
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(playActive){
			
			 //Player and ball collision detection
			if(new Rectangle(ballPositionX,ballPositionY,20,20).intersects(new
					Rectangle(playerPosition,550,30,8))){
				//ballDirectionY = -ballDirectionY;
				if(ballDirectionX >0){
					ballDirectionY = -ballDirectionY;
					ballDirectionX = -ballDirectionX;
				}
				else{
					ballDirectionY = -ballDirectionY;
				}
			}else if(new Rectangle(ballPositionX,ballPositionY,20,20).intersects(new
					Rectangle(playerPosition+30,550,40,8))){
				
				ballDirectionY = -ballDirectionY;
				

			}else if(new Rectangle(ballPositionX,ballPositionY,20,20).intersects(new
					Rectangle(playerPosition+70,550,30,8))){
				if(ballDirectionX <0){
					ballDirectionY = -ballDirectionY;
					ballDirectionX = -ballDirectionX;
				}
				else{
					ballDirectionY = -ballDirectionY;
					
				}
			}
			
			//Brick and ball collision detection
		  A: for(int i = 0;i<map.BrickPattern.length;i++){
				for(int j = 0;j<map.BrickPattern[0].length;j++){
					if(map.BrickPattern[i][j]>0){
						int brickX = j*map.width+80;
						int brickY = i*map.height+50;
						int brickW = map.width;
						int brickH = map.height;
					
						Rectangle rect = new Rectangle(brickX,brickY,brickW,brickH);
						Rectangle ballRect = new Rectangle(ballPositionX,ballPositionY,20,20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)){
						
							map.setBrickValue(-1, i, j);
							brickNum--;
							if(brickNum==0){
								
							}
							score+=5;
						
							if(ballPositionX + 19 <= brickRect.x || ballPositionX+1 >= brickRect.x + brickRect.width){
								ballDirectionX=-ballDirectionX;
								}else{
								ballDirectionY=-ballDirectionY; 
								}
							break A; // !!!
						}
					}
				}
			}
			
			
			// Border collision detection
			ballPositionX+=ballDirectionX;
			ballPositionY+=ballDirectionY;
			if(ballPositionX<0){
				ballDirectionX = - ballDirectionX;
			}
			if(ballPositionY<0){
				ballDirectionY = - ballDirectionY;
			}
			if(ballPositionX>670){
				ballDirectionX = - ballDirectionX;
			}
			
		}
		
		repaint();
		
		if(close==true){ // !!!!!!!!!!!!!
			 JComponent comp = (JComponent) this; // Close the gameplaying screen
			 Window win = SwingUtilities.getWindowAncestor(comp);
			 win.dispose();
		}
	}

	public void keyPressed(KeyEvent e) { // 
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if(playerPosition >= 600){
				playerPosition=600;
			}else{
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if(playerPosition < 10 ){
				playerPosition=5;
			}else{
				moveLeft();
			}
		}
		// CTRL+Q close the game condition
		if((e.getKeyCode() == KeyEvent.VK_Q) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			JComponent comp = (JComponent) this; // O anki oyunu kapatma
			Window win = SwingUtilities.getWindowAncestor(comp);
			win.dispose();
		}
	}

	public void moveRight(){
		playActive = true;
		playerPosition+=40;
	}
	public void moveLeft(){
		playActive = true;
		playerPosition-=40;
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void run() {
			while(true){
				if(brickNum==0){
					isLevelFinished=true;
					brickNum=15;
				}
					
					
				try{
					Thread.sleep(2);
				}
				catch(Exception e){
				}
			}
	}
		
		
	
}


