package TreeGenerator;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1082718261400939023L;
	private static final int width=1280;
	private static final int height=720;
	private static boolean running;

	private BufferedImage image;
	private Graphics2D g;
	private Thread thread;
	private long iters=0;
	private double angleIncrement = Math.PI/10;
	private double magnitudeIncrement = 0.785;
	private int brushDepth = 10;
	
	private int imageNum;
	public Main() 
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
			delay(500);
		}
	}
	public void update()
	{
	}
	public void render()
	{
		iters=0;
		clearCanvas();
		brushDepth=5;
		//long t1 = System.nanoTime();
		branch(width/2,720,-150,Math.PI/2,brushDepth);
		/*try
		{ 
			System.out.println("saving");
			File outputfile = new File("image_"+imageNum+".png");
			ImageIO.write(image, "png", outputfile);
			imageNum++;
		}
		catch(Exception e){e.printStackTrace();}*/
		//System.out.println((System.nanoTime()-t1)/1000+" micros");
		//System.out.println("\n\n\n\n\n");
	}
	public void draw()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image,0,0,null);
		g2.dispose();
	}

	public void branch(int x, int y, double magnitude, double theta, int brushDepth)
	{
		angleIncrement = (Math.random()*Math.PI/4);
		//System.out.println(angleIncrement);
		if(magnitude>20 || magnitude<-20)
		{
			g.setColor(new Color(77, 168, 59));
			g.drawLine(x,y,(int)(x+magnitude*Math.cos(theta)),(int)(y+magnitude*Math.sin(theta)));
			//left branch
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




	public void clearCanvas()
	{
		g.setColor(Color.WHITE);
		g.fillRect(0,0,width,height);
		g.setColor(Color.BLACK);
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
		f.setContentPane(new Main());
		f.setSize(width,height);
		f.setVisible(true);
	}


}

