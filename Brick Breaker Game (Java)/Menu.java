import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class Menu extends JFrame implements ActionListener,Runnable{
	
	JButton a=new JButton("New Game"); 
	JButton b=new JButton("Options"); 
	JButton c=new JButton("High Scores");
	JButton d=new JButton("Help"); 
	JButton e=new JButton("About"); 
	JButton f=new JButton("Exit");
	public GameBGround bg;
	
	
	public Menu(){
	        
		setTitle("ARKANOID - Main Menu");
        setSize(400,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
	
        setLayout(new BorderLayout());
        // I have add an image in the src file
        setContentPane(new JLabel(new ImageIcon("C:\\Users\\90531\\workspace\\Game_Test\\image.jpg")));
        
        setLayout(null);   
		a.setBounds(125,20,140, 40);    
		add(a);   
		b.setBounds(125,75,140, 40);    
		add(b);      
		c.setBounds(125,130,140, 40);    
		add(c);     
		d.setBounds(125,185,140, 40);    
		add(d);    
		e.setBounds(125,240,140, 40);    
		add(e);    
		f.setBounds(125,295,140, 40);    
		add(f);   
		
		a.addActionListener(new ActionListener() { // New Game

			   
		    public void actionPerformed(ActionEvent e) {
		    	bg = new GameBGround();
		    }
		});
		
		b.addActionListener(new ActionListener() { // Options

			   
		    public void actionPerformed(ActionEvent e) {
		    	
		    	JFrame optionsFrame = new JFrame("Options");
		    	optionsFrame.setBounds(560,220,250,250);
		    	optionsFrame.setLayout(null); 
		    	JLabel label = new JLabel("Choose your difficulty:");
		    	label.setFont(new Font("Serif", Font.PLAIN, 16));
		    
		    	final JRadioButton d1=new JRadioButton("Novice");    
		    	final JRadioButton d2=new JRadioButton("Intermediate");  
		    	final JRadioButton d3=new JRadioButton("Advanced");    
		    	JButton jButton=new JButton("Select"); ;
		    	
		    	ButtonGroup g1 = new ButtonGroup(); 
		    	
		    	d1.setBounds(70,40,100,20);    
		    	d2.setBounds(70,70,100,20);
		    	d3.setBounds(70,100,100,20);     
		    	jButton.setBounds(70, 160, 100, 30);
		    	
		    	ButtonGroup bgroup=new ButtonGroup();
		    	//JPanel buttonPanel = new JPanel();
		    	
		    	     
		    	optionsFrame.add(d1);optionsFrame.add(d2);optionsFrame.add(d3);
		    	bgroup.add(d1);bgroup.add(d2);bgroup.add(d3);
		    	optionsFrame.add(jButton);
		    	
		    	g1.add(d1);
		    	g1.add(d2);
		    	g1.add(d3);
		    	
		    	jButton.addActionListener(new ActionListener() { 
		             
		             public void actionPerformed(ActionEvent e) 
		             { 
		            	 if (d1.isSelected()) { 
		            		 GamePlaying.difficulty = 1;
		                 } 
		                 else if (d2.isSelected()) {
		                	 GamePlaying.difficulty = 2;
		                 }  
		                 else if(d3.isSelected()){ 
		                	 GamePlaying.difficulty = 3;
		                 } 
		             } 
		         }); 
		    	
		    	optionsFrame.setVisible(true);  
		    	
		 
		    	
		    }
		});
		
		c.addActionListener(new ActionListener() { // High Scores

		    public void actionPerformed(ActionEvent e) {
		    
		    }
		});
		
		d.addActionListener(new ActionListener() { // Help
 
		    public void actionPerformed(ActionEvent e) {
		    	JFrame helpFrame = new JFrame("Help");
		    	helpFrame.setBounds(400,200,570,125);
		    	helpFrame.setLayout(new FlowLayout());
		    	JLabel label = new JLabel("You can control the paddle "
		    			+ "with left and right arrow keys.");
		    	JLabel label2 = new JLabel("When ball hits the bricks "
		    			+ "they disappear.");
		    	JLabel label3 = new JLabel("After all the bricks disappear "
		    			+ "you advance next level");
		    	
		    	helpFrame.setResizable(false);
		    	helpFrame.add(label);
		    	helpFrame.add(label2);
		    	helpFrame.add(label3);
		    	helpFrame.setVisible(true);
		    	
		    }
		});
		
		e.addActionListener(new ActionListener() { // About
  
		    public void actionPerformed(ActionEvent e) {
		    	JFrame aboutFrame = new JFrame("About");
		    	aboutFrame.setBounds(400,200,570,75);
		    	aboutFrame.setLayout(new FlowLayout());
		    	JLabel label = new JLabel("This game is developed by"
		   		+ " Ali Ulvi Gurselli, Yeditepe University");
		    	JLabel label2 = new JLabel("20160701013-072 , au.gurselli@gmail.com");
		   
		    	aboutFrame.setResizable(false);
		    	aboutFrame.add(label);
		    	aboutFrame.add(label2);
		    	aboutFrame.setVisible(true);
		    	 
		    }
		});
		
		f.addActionListener(new ActionListener() { // Exit

			   
		    public void actionPerformed(ActionEvent e) {
		        System.exit(0);
		    }
		});
    
		setSize(399,399); // Setsize refreshlenmeli
        setSize(400,400);
	}



	public void actionPerformed(ActionEvent arg0) {}



	public void run() {
		
			new Thread(bg).start();
			
		}
		
	

		
		
}
	

