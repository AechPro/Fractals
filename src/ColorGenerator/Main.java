package ColorGenerator;

/**
 * @author Matthew Allen
 * 
 * Visual Fractal color generator
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main extends JPanel implements Runnable
{
	private static final long serialVersionUID = 6893637610904218693L;
	private static final int scrW=1280;
	private static final int scrH=720;
	private boolean running;
	private Thread thread;
	private Graphics2D g;
	private BufferedImage image;
	
	
	private int R,G,B;
	private int x,y,w,h;
	
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
			thread.start();
		}
	}
	public void init()
	{
		image = new BufferedImage(scrW,scrH,BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D)(image.getGraphics());
		
		x=1;
		y=1;
		w=1280/2;
		h=1280/2;
		R=G=B=100;
		running = true;
	}
	public void run()
	{
		init();
		while(running)
		{
			update();
			render();
			draw();
			delay(1000);
		}
	}
	
	public void update()
	{
		
	}
	public void render()
	{
		resetCanvas();
		g.fillRect(x,y,w,h);
		generateFractal(x,y,w,h);
	}
	public void draw()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image,0,0,null);
		g2.dispose();
	}
	
	
	public void generateFractal(int x, int y, int w, int h)
	{
		int nx = (int)(Math.round(x*70));// (int) (x/(Math.random()*10));
		int s = 5;
		int rows = h/(nx+s);
		int cols = w/(nx+s);
		
		//rows=cols=8;
		
		int horiz = w-((nx+s)*cols)+s;
		int vert = h-((nx+s)*rows)+s;
		int xPlacer = x+(horiz/2);
		int yPlacer = y+(vert/2);
		int N;
		int range=254;
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<cols;j++)
			{
				//range = (int)(Math.random()*150)+30;
				if((int)(Math.random()*2)==1){N = (int)(Math.random()*range);}
				else{N = -(int)(Math.random()*range);}
				if((R/2)-N>0 && (R/2)-N<254){R=(R/2)-N;}
				//range = (int)(Math.random()*254);
				if((int)(Math.random()*2)==1){N = (int)(Math.random()*range);}
				else{N = -(int)(Math.random()*range);}
				if((G/2)-N>0 && (G/2)-N<254){G=(G/2)-N;}
				//range = (int)(Math.random()*254);
				if((int)(Math.random()*2)==1){N = (int)(Math.random()*range);}
				else{N = -(int)(Math.random()*range);}
				if((B/2)-N>0 && (B/2)-N<254){B=(B/2)-N;}
				
				g.setColor(new Color(R,G,B));
				
				g.fillRect(xPlacer,yPlacer,nx,nx);
				xPlacer+=(nx+s);
			}
			yPlacer+=(nx+s);
			xPlacer=x+(horiz/2);
		}
	}
	
	public void resetVals()
	{
		
	}
	
	
	public void resetCanvas()
	{
		g.clearRect(0,0,scrW,scrH);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, scrW, scrH);
		g.setColor(Color.BLACK);
	}
	public void delay(int millis)
	{
		try{Thread.sleep(millis);}
		catch(Exception e){e.printStackTrace();}
	}
	public static void main(String[] args)
	{
		JFrame f = new JFrame("main");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new Main());
		f.setSize(scrW,scrH);
		f.setVisible(true);
		f.requestFocus();
	}

}
