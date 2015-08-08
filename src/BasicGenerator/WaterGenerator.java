package BasicGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.*;

public class WaterGenerator extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1082718261400939023L;
	private static final int width=1280;
	private static final int height=720;
	private static boolean running;

	private BufferedImage image;
	private Graphics2D g;
	private Thread thread;

	private ArrayList<Double> initX;
	private ArrayList<Double> initY;
	private double[] list;
	private int counter=0;
	private int timeCounter=0;
	private long avg;
	private double maxX=500;
	private double maxY=150;
	int index=0;
	int renderCounter=0;
	boolean initGenerated=false;
	
	private WaterSection[] sections;

	public WaterGenerator() 
	{
		super();
	}

	public void addNotify()
	{
		super.addNotify();
		if(thread==null)
		{
			thread = new Thread(this);
			try{thread.start();}
			catch(Exception e){}
		}
	}
	public void init()
	{
		image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();

		initX = new ArrayList<Double>();
		initY = new ArrayList<Double>();
		
		

		//initialize the lists with five points to generate fractals in
		
		
		running=true;
	}

	public void run() 
	{
		init();
		while(running)
		{
			update();
			render();
			draw();
			delay(17);
		}
	}
	public void update()
	{
		int w=width>>1;
		int h=height>>1;
		if(initGenerated)
		{
			for(int i=0;i<sections.length-2;i+=2)
			{
				//System.out.println("updating from "+sections[i].getX()+" to "+(w+(int)list[i]));
				sections[i].update(w+(int)list[i],h+(int)list[i+1]);
			}
		}
		
	}
	public void render()
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		g.setColor(Color.WHITE);
		int w=width>>1;
		int h=height>>1;

		counter=0;
		boolean generate=true;
		if(initGenerated)
		{
			for(int i=0;i<sections.length-2;i+=2)
			{
				if(!sections[i].hasMoved()){generate=false;}
			}
		}
		
		if(generate && renderCounter>5)
		{
			renderCounter=0;
			//set starting time
			long startTime = System.nanoTime();
			
			generateWater(5);
			if(!initGenerated)
			{
				sections = new WaterSection[list.length];
				for(int i=0;i<list.length-2;i+=2)
				{
					sections[i] = new WaterSection(w+(int)list[i],h+(int)list[i+1],33,33);
				}
				initGenerated=true;
			}
			//add the amount of time it took to generate the list to the average
			avg+=System.nanoTime()-startTime;
			
			
			//iterate a counter to know when we want to stop taking samples for the average
			timeCounter++;
		}
		
		
		if(timeCounter==100)
		{
			//divide average by number of samples taken
			avg/=timeCounter;
			
			//reset the counter so we can take another average
			timeCounter=0;
			
			//print out time in nanoseconds, microseconds, and milliseconds
			System.out.println("avg time with list of size "+index+":");
			System.out.println(avg+" nanoseconds");
			System.out.println(avg/1000+" microseconds");
			System.out.println(avg/1000000+" milliseconds");
			
			//reset average
			avg=0L;
		}		
		
		//print the size of the values generated
		//System.out.println(index);
		
		//render the lines
		/*for(int i=0;i<index-2;i+=2)
		{
			//System.out.println("drawing from: "+list[i]+","+list[i+1]+" to: "+list[i+2]+","+list[i+3]);
			g.setColor(new Color(0,125,255,225));
			g.fillOval((int)(w+list[i]),(int)(h+list[i+1]),20,20);
		}*/
		//System.out.println(counter);
		if(initGenerated)
		{
			for(int i=2;i<sections.length-2;i+=2)
			{
				sections[i].render(g);
			}
		}
		renderCounter++;
	}
	public void draw()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image,0,0,null);
		g2.dispose();
	}
	
	public double[] generateFractal(ArrayList<Double> x, ArrayList<Double> y, int complexity)
	{
		//System.out.println();
		Random rand = new Random();
		double H=7;
		double maxRange=4;
		double range = maxRange;
		double N=0;
		int j=0;
		ArrayList<Double> newXPoints = new ArrayList<Double>();
		ArrayList<Double> newYPoints = new ArrayList<Double>();
		newXPoints.add(x.get(0));
		newYPoints.add(y.get(0));
		for(int i=0;i<y.size()-1;i++)
		{
			//generate random number in between -range and range, reset value range if it has been reduced too much\
			//System.out.println(range);
			if(rand.nextInt(2)==1)
			{
				if(range>1){N = rand.nextDouble()*range;}
				else
				{
					range=maxRange/2;
					N = rand.nextDouble()*range;
				}
			}
			else
			{
				if(range>1){N = -rand.nextDouble()*range;}
				else
				{
					range=maxRange/2;
					N = -rand.nextDouble()*range;
				}
			}
			
			double newX=0;
			double newY=0;
			
			//find midpoints and add offset
			if(x.get(i+1)>x.get(i)){ newX=(x.get(i+1)-(x.get(i+1)-x.get(i))/2); }
			else{ newX=(x.get(i)-(x.get(i)-x.get(i+1))/2); }

			if(y.get(i+1)>y.get(i)){ newY=(y.get(i+1)-(y.get(i+1)-y.get(i))/2)-N; }
			else{ newY=(y.get(i)-(y.get(i)-y.get(i+1))/2)-N; }

			//add newly found midpoints and offset midpoint to new list
			newXPoints.add(newX);
			newXPoints.add(x.get(i+1));

			newYPoints.add(newY);
			newYPoints.add(y.get(i+1));
		}
		if(counter>=complexity)
		{
			j=0;
			double[] newPoints = new double[newXPoints.size()*2];
			for(int i=0;i<newXPoints.size();i++)
			{
				newPoints[j]=(newXPoints.get(i));
				newPoints[j+1]=(newYPoints.get(i));
				j+=2;
			}
			return newPoints;
		}
		else
		{
			counter++;
			return generateFractal(newXPoints, newYPoints, complexity);
		}
	}
	
	public void generateWater(int complexity)
	{
		index = 0;
		initX = new ArrayList<Double>();
		initY = new ArrayList<Double>();
		
		initX.add(-maxX);
		initX.add(maxX);
		initY.add(0.0);
		initY.add(0.0);
		list = generateFractal(initX, initY, complexity);
		
		for(int i=0;i<list.length;i++)
		{
			index++;
			if(list[i]==maxX && list[i+1]==maxY){break;}
		}
		index++;
	}
	public void delay(int millis)
	{
		try{Thread.sleep(millis);}
		catch(Exception e){e.printStackTrace();}
	}
	public static void main(String[] args)
	{
		JFrame f = new JFrame("Main");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new WaterGenerator());
		f.setSize(width,height);
		f.setVisible(true);
	}


}
