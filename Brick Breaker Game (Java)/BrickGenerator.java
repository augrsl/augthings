import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class BrickGenerator {

	public int BrickPattern[][];
	public int width;
	public int height;
	public static boolean toSecond=false;
	public static boolean toThird=false;
	
	public BrickGenerator(){
		//Difficulty control here !!!	
	}
	
	
	public void draw(Graphics2D g){ // Draw bricks if its map value is 1
		for(int i = 0 ;i<BrickPattern.length;i++){
			for(int j = 0; j<BrickPattern[0].length;j++){
				if(BrickPattern[i][j]>0){
					g.setColor(Color.orange);
					g.fillRect(j*width+80, i*height+50, width, height);
					
					// Bricklerin dýþýný çizme
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j*width+80, i*height+50, width, height);
				}
			}
		}
	}
	
	public void setBrickValue(int value, int row, int col){ // Brickmapping
		BrickPattern[row][col]=value;
	}
	
	// Level generators functions(1-9)
	public  void level1(){
		BrickPattern = new int[3][7];
		for(int i = 0 ;i<BrickPattern.length;i++){
			for(int j = 0; j<BrickPattern[0].length;j++){
				if(j==0||j==1||j==5||j==6){
					continue;
				}
				BrickPattern[i][j]=1;
			}
			width=540/7;
			height=150/3;	
		}
	}
	public void level2(){
		BrickPattern = new int[3][7];
		for(int i = 0 ;i<BrickPattern.length;i++){
			for(int j = 0; j<BrickPattern[0].length;j++){
				if(j==1||j==5){
					continue;
				}
				BrickPattern[i][j]=1;
			}
			width=540/7;
			height=150/3;	
		}
	}
	public void level3(){
		BrickPattern = new int[3][7];
		for(int i = 0 ;i<BrickPattern.length;i++){
			for(int j = 0; j<BrickPattern[0].length;j++){
				if(j==3){
					continue;
				}
				BrickPattern[i][j]=1;
			}
			width=540/7;
			height=150/3;	
		}
	}
	
	public void level4(){
		
	}
	public void level5(){
		
	}
	public void level6(){
		
	}
	public void level7(){
		
	}
	public void level8(){
		
	}
	public void level9(){
		
	}
	
}


//if(level==1){// 123 456 789
//BrickPattern = new int[row][col];
//for(int i = 0 ;i<BrickPattern.length;i++){ // Burasý brick pozisyon fonksiyonu aslýnda
//	for(int j = 0; j<BrickPattern[0].length;j++){
//		BrickPattern[i][j]=1;
//	}
//	width=540/col;
//	height=150/row;	
//}
//}	
