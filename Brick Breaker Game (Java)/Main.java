import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
	
		
		//GameBGround bg = new GameBGround(); // Bg of game
		Menu menu = new Menu();
		new Thread(menu).start();
	}
	
}
