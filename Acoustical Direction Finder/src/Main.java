import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JSlider;

import com.fazecast.jSerialComm.*;


public class Main {

	public static int buffersize = 2048*2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        
		double samplesPerUsec = 2000;
		double soundSpeed = 342.0;
		int scale = 10;
		
		ArrayList<MicInfo> mics = new ArrayList<MicInfo>();
		MicInfo mic1 = new MicInfo();
		MicInfo mic2 = new MicInfo();
		MicInfo mic3 = new MicInfo();
		
		mic1.location = new Point(0, 0);
		mic2.location = new Point(-85, 85);
		mic3.location = new Point(85, 85);
		
		mics.add(mic1);
		mics.add(mic2);
		mics.add(mic3);
		
		LocationTable table = null;
		
		Random rand = new Random();
		
		// Raw data
		int[] data1 = new int[buffersize];
		int[] data2 = new int[buffersize];
		int[] data3 = new int[buffersize];
		int[] data4 = rand.ints(buffersize, 1,50).toArray();
		
		// Data for image creation 
		int[] data1i = new int[buffersize-510];
		int[] data2i = new int[buffersize-510];
		int[] data3i = new int[buffersize-510];
		int[] data4i = rand.ints(buffersize-510, 1,50).toArray();
		
		
		// Creating image from data received from particualar port
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
       
            newLine = data.nextLine();
            //System.out.println(newLine);
            	
            dataSplit = newLine.split(" ",0);
       
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
            	
            j++;
    		
        	if(j == buffersize){
        		j=0;
        		
        		data1i = Arrays.copyOfRange(data1, 510, data1.length);
        		data2i = Arrays.copyOfRange(data2, 510, data2.length);
        		data3i = Arrays.copyOfRange(data3, 510, data3.length);
        		
//        		SoundUtils.normalize(data1i);
//    			SoundUtils.normalize(data2i);
//    			SoundUtils.normalize(data3i);
    			
    			CrossCorrelation cc12 = new CrossCorrelation(data1i,data2i);
    			CrossCorrelation cc13 = new CrossCorrelation(data1i,data3i);
    			
    			System.out.println("\nPost normalization (MIC1 basis is sample 0)\nCC21@ "+cc12.maxindex);
				System.out.println("CC31@ "+cc13.maxindex);
    			
        		createImage("test0",data4i,data3i,data2i,data1i,5,5,5,5,5);
     
        		System.out.println("Absolute Direction : " + directionAngle(cc12.maxindex,cc13.maxindex));
    			
        		
        		db(1);
        	}
        
        }
        
	}
	
	public static double directionAngle(int cc1to2, int cc1to3){
		
		double micp1 = 1;
		double micp2 = cc1to2;
		double micp3 = cc1to3;
		
		double degreeCalculated = 0;
		
		if(micp1 < micp2 && micp1 < micp3){ // mic1 first arrival
			
			degreeCalculated = 180;
			
			if(micp2 < micp3){ //mic2 second arrival
				
				db(123);
				degreeCalculated -= 60 * ( (micp3-micp2) / micp3 ) ;  
			
			}
			else if(micp3 < micp2){ // mic3 second arrival
				db(132);
				degreeCalculated += 60 * ( (micp2-micp3) / micp2 ) ;
				
			}
		}
		else if(micp2 < micp1 && micp2 < micp3){ // mic2 first arrival
			
			micp1 = micp1 - micp2;
			micp3 = micp3 - micp2;
			degreeCalculated = 60;
			
			if(micp3 < micp1){ //mic3 second arrival
				
				db(231);
				degreeCalculated -= 60 * ( (micp1-micp3) / micp1 ) ;
			
			}
			else if(micp1 < micp3){ // mic1 second arrival
				
				db(213);
				degreeCalculated += 60 * ( (micp3-micp1) / micp3 ) ;
				
			}
			
		}
		
		else if(micp3 < micp1 && micp3 < micp2){
			
			micp1 = micp1 - micp3;
			micp2 = micp2 - micp3;
			degreeCalculated = 300;
			
			if(micp1 < micp2){ //mic3 second arrival
				
				db(312);
				degreeCalculated -= 60 * ( (micp2-micp1) / micp2 ) ;
			
			}
			else if(micp2 < micp1){ // mic1 second arrival
				
				db(321);
				degreeCalculated += 60 * ( (micp1-micp2) / micp1 ) ;
				
			}
			
		}
		// Corner case ifleri yaz
		
		return degreeCalculated;
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
