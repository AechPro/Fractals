package BasicGenerator;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.*;

public class MountainGenerator extends JPanel implements Runnable
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


	private double angleIncrement = Math.PI/10;
	private double magnitudeIncrement = 0.77;
	private int brushDepth = 10;
	public MountainGenerator() 
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
			//delay(17);
		}
	}
	public void update()
	{

	}
	public void render()
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		g.setColor(Color.WHITE);
		int w=width>>1;
		int h=height>>1;
		counter=0;

		//set starting time
		long startTime = System.nanoTime();

		//generateMountain(8);
		brushDepth=5;
		generateMountain(3);
		//add the amount of time it took to generate the list to the average
		avg+=System.nanoTime()-startTime;


		//iterate a counter to know when we want to stop taking samples for the average
		timeCounter++;

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

		//branch(width/2-200,720,-150,Math.PI/2,brushDepth);
		//branch(width/2+400,720,-150,Math.PI/2,brushDepth);


		//render the lines
		g.setColor(Color.WHITE);
		for(int i=0;i<index-2;i+=2)
		{
			//System.out.println("drawing from: "+list[i]+","+list[i+1]+" to: "+list[i+2]+","+list[i+3]);
			g.drawLine((int)(w+list[i]),(int)(h+list[i+1]),(int)(w+list[i+2]),(int)(h+list[i+3]));
		}

		delay(500);
	}
	public void draw()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image,0,0,null);
		g2.dispose();
	}
	public void branch(int x, int y, double magnitude, double theta, int brushDepth)
	{
		angleIncrement = Math.random()*Math.PI/4;
		if(magnitude>5 || magnitude<-5)
		{
			g.setColor(new Color(77, 168, 59));
			g.drawLine(x,y,(int)(x+magnitude*Math.cos(theta)),(int)(y+magnitude*Math.sin(theta)));
			branch(x+(int)(magnitude*Math.cos(theta)),y+(int)(magnitude*Math.sin(theta)),magnitude*magnitudeIncrement,theta-angleIncrement,brushDepth-1);
			g.drawLine(x,y,(int)(x+magnitude*Math.cos(theta)),(int)(y+magnitude*Math.sin(theta)));
			branch(x+(int)(magnitude*Math.cos(theta)),y+(int)(magnitude*Math.sin(theta)),magnitude*magnitudeIncrement,theta,brushDepth-1);
			g.drawLine(x,y,(int)(x+magnitude*Math.cos(theta)),(int)(y+magnitude*Math.sin(theta)));
			for(int i=0;i<brushDepth;i++)
			{

				g.drawLine(x-i,y,(int)(x-i+magnitude*Math.cos(theta)),(int)(y+magnitude*Math.sin(theta)));
				g.drawLine(x+i,y,(int)(x+i+magnitude*Math.cos(theta)),(int)(y+magnitude*Math.sin(theta)));

			}
			branch(x+(int)(magnitude*Math.cos(theta)),y+(int)(magnitude*Math.sin(theta)),magnitude*magnitudeIncrement,theta+angleIncrement,brushDepth-1);
			g.setColor(new Color(70,40,40));
		}
	}
	public double[] generateFractal(ArrayList<Double> x, ArrayList<Double> y, int complexity)
	{
		//System.out.println();
		Random rand = new Random();

		double H=2.3;
		double maxRange=25;
		double range = maxRange;
		double N=0;
		int j=0;

		ArrayList<Double> newXPoints = new ArrayList<Double>();
		ArrayList<Double> newYPoints = new ArrayList<Double>();

		newXPoints.add(x.get(0));
		newYPoints.add(y.get(0));

		for(int i=0;i<y.size()-1;i++)
		{
			//generate random number in between -range and range, reset value range if it has been reduced too much
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
			if(rand.nextInt(2)==1)
			{
				if(x.get(i+1)>x.get(i)){ newX=(x.get(i+1)-(x.get(i+1)-x.get(i))/2)-(N/3); }
				else{ newX=(x.get(i)-(x.get(i)-x.get(i+1))/2)-(N/3); }
			}
			else
			{
				if(x.get(i+1)>x.get(i)){ newX=(x.get(i+1)-(x.get(i+1)-x.get(i))/2)+(N/3); }
				else{ newX=(x.get(i)-(x.get(i)-x.get(i+1))/2)+(N/3); }
			}
			

			if(y.get(i+1)>y.get(i)){ newY=(y.get(i+1)-(y.get(i+1)-y.get(i))/2)-N; }
			else{ newY=(y.get(i)-(y.get(i)-y.get(i+1))/2)-N; }

			//add newly found midpoints and offset midpoint to new list
			newXPoints.add(newX);
			newXPoints.add(x.get(i+1));

			newYPoints.add(newY);
			newYPoints.add(y.get(i+1));

			//reduce number range
			range*=1/Math.pow(2,H);
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
			int w=width>>1;
			int h=height>>1;
			int numTrees = (int)(Math.random()*500);
			for(int k=0;k<numTrees-1;k+=2)
			{
				int point = (int) (Math.random()*((newXPoints.size()*2)-2));
				if(point%2==0)
				{
					branch(w+(int)newPoints[point],h+(int)newPoints[point+1],(int)(Math.random()*-15),3.14/2,0);
				}
				else
				{
					branch(w+(int)newPoints[point-1],h+(int)newPoints[point],(int)(Math.random()*-15),3.14/2,0);
				}
			}




			return newPoints;
		}
		else
		{
			counter++;
			return generateFractal(newXPoints, newYPoints, complexity);
		}

	}

	public void generateMountain(int complexity)
	{
		Random rand = new Random();
		index = 0;
		initX = new ArrayList<Double>();
		initY = new ArrayList<Double>();

		initX.add(-maxX);
		initX.add((rand.nextDouble()*-maxX/4)-120);
		if(rand.nextInt(2)==1){initX.add((rand.nextDouble()*-maxX/8)+35);}
		else{initX.add((rand.nextDouble()*maxX/4)-35);}
		initX.add((rand.nextDouble()*maxX/4)+120);
		initX.add(maxX);

		double temp = rand.nextDouble();
		initY.add(maxY);
		initY.add(temp*-maxY/4);
		if(rand.nextInt(2)==1){initY.add(rand.nextDouble()*-maxY);}
		else{initY.add(rand.nextDouble()*maxY);}
		initY.add(temp*-maxY/4);
		initY.add(maxY);
		list = generateFractal(initX, initY, complexity);
		index=list.length;
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
		f.setContentPane(new MountainGenerator());
		f.setSize(width,height);
		f.setVisible(true);
	}


}

