import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JSlider;

import com.fazecast.jSerialComm.*;


public class Main {

	public static int buffersize = 2048;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//int[][] adcdata= new int[3][100];
		
		// Create Image Test
        
		Random rand = new Random();
		
		int[] data1 = new int[buffersize];
		int[] data2 = new int[buffersize];
		int[] data3 = new int[buffersize];
		int[] data4 = rand.ints(buffersize, 1,50).toArray();
		
        
		
		
		String fileprefix = "";
		int fpc = 0;
		
     // determine which serial port to use
        SerialPort ports[] = SerialPort.getCommPorts();
        System.out.println("Select a port:");
        int i = 1;
        for(SerialPort port : ports) {
                System.out.println(i++ + ". " + port.getSystemPortName());
        }
        
        Scanner s = new Scanner(System.in);
        int chosenPort = s.nextInt();

        // open and configure the port
        SerialPort port = ports[chosenPort - 1];
        if(port.openPort()) {
                System.out.println("Successfully opened the port.");
        } else {
                System.out.println("Unable to open the port.");
                return;
        }
		
        port.setBaudRate(115200);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        
        Scanner data = new Scanner(port.getInputStream());
        
        String[] dataSplit;
        int j=0;
        while(data.hasNextLine()) {
            String newLine = " ";
            
            //try{
            	newLine = data.nextLine();
            	System.out.println(newLine);
            	
            	dataSplit = newLine.split(" ",0);
            	
//            	for (String a : dataSplit){
//                    System.out.println(a);
//            	}
            	//db(Integer.parseInt(dataSplit[0]));
            	
            		try{
            			data1[j] = Integer.parseInt(dataSplit[0])-256; // Neden bak !!!
            			data2[j] = Integer.parseInt(dataSplit[1])-256;
            			data3[j] = Integer.parseInt(dataSplit[2])-256;
            		}
            		catch(Exception e){
            			
            			data1[j] = 0;
            			data2[j] = 0;
            			data3[j] = 0;
            			
            	
            		}
            	
            	
            	//}
            //catch(Exception e){}
         
            j++;
        	
            
        	if(j == buffersize){
        		j=0;
        		createImage("test0",data4,data3,data2,data1,5,5,5,5,5);
        		db(1);
        	}
        }
        
        
        
	}
	
	public static void db(int a){
		System.out.println("HOOP_"+a);
	}

	private static void createImage(String fileprefix,int data1[], int data2[], int data3[], int data4[], int event, int sample1, int sample2, int sample3, int sample4) {
		int dsize = data1.length;
		BufferedImage image1 = new BufferedImage(dsize, 256 * 4, BufferedImage.TYPE_INT_RGB);
		image1.createGraphics();
		Graphics2D g2d = (Graphics2D) image1.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.drawRect(0, 0, dsize - 1, 256 * 4 - 1);
		g2d.setColor(Color.GRAY);
		g2d.drawLine(event, 0, event, 256 * 4 - 1);
		g2d.setColor(Color.RED);
		g2d.drawLine(sample1, 0, sample1, 256 * 4 - 1);
		g2d.setColor(Color.GREEN);
		g2d.drawLine(sample2, 0, sample2, 256 * 4 - 1);
		g2d.setColor(Color.BLUE);
		g2d.drawLine(sample3, 0, sample3, 256 * 4 - 1);
		g2d.setColor(Color.YELLOW);
		g2d.drawLine(sample4, 0, sample4, 256 * 4 - 1);
		g2d.setColor(Color.GRAY);
		g2d.drawLine(0, 128, dsize, 128);
		g2d.drawLine(0, 128 + 256, dsize, 128 + 256);
		g2d.drawLine(0, 128 + 256 * 2, dsize, 128 + 256 * 2);
		g2d.drawLine(0, 128 + 256 * 3, dsize, 128 + 256 * 3);
		g2d.setColor(Color.WHITE);

		for (int i = 0; i < data1.length; i++) {
			if (i > 0) {
				g2d.setColor(Color.RED);
				g2d.drawLine(i - 1, -data1[i - 1] + 128, i, -data1[i] + 128);
				g2d.setColor(Color.GREEN);
				g2d.drawLine(i - 1, -data2[i - 1] + 128 + 256, i, -data2[i] + 128 + 256);
				g2d.setColor(Color.BLUE);
				g2d.drawLine(i - 1, -data3[i - 1] + 128 + 256 + 256, i, -data3[i] + 128 + 256 + 256);
				g2d.setColor(Color.YELLOW);
				g2d.drawLine(i - 1, -data4[i - 1] + 128 + 256 + 256 + 256, i, -data4[i] + 128 + 256 + 256 + 256);
			}
		}
		File file1;
		try {
			file1 = File.createTempFile(fileprefix+"-", ".png");
			String result1 = file1.getAbsolutePath();
			ImageIO.write(image1, "PNG", file1);
			System.out.print(result1);
			System.out.println("done");
			//Runtime.getRuntime().exec("eog -f -w " + result1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}