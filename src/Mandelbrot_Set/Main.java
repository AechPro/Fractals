package Mandelbrot_Set;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
public class Main extends JPanel implements Runnable, KeyListener
{
	private static final int width = 1280;
	private static final int height = 720;
	private Graphics2D g;
	private BufferedImage image;
	private boolean running;
	private Thread thread;
	private double zx,zy;
	private double cx,cy;
	private double zoom = 150;
	private double xOffset = 0.3851562500000003;
	private double yOffset = 0.17506510416666735;
	private final int maxIterations=254;
	private int iterations;
	private double[] xCoords;
	private double[] yCoords;
	private int[] color;
	private double zxSqr;
	private double zySqr;
	private double zProd;
	private double reflectY = yOffset*zoom*2;
	private int coordTracker=0;
	private int maxPixels=720*1280;
	public Main()
	{
		super();
		this.setFocusable(true);
		requestFocus();
	}
	public void addNotify()
	{
		super.addNotify();
		if(thread==null)
		{
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	public void init()
	{
		image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D)image.getGraphics();
		xCoords = new double[maxPixels];
		yCoords = new double[maxPixels];
		color = new int[maxPixels];
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
			
			
			//delay(1000);
		}
	}

	public void update()
	{
		
	}
	public void render()
	{
		clearCanvas();
		calc();
		/*for(int i=0;i<maxPixels;i++)
		{
			g.setColor(new Color(color[i],0,0));
			g.drawLine((int)xCoords[i],(int)yCoords[i],(int)xCoords[i],(int)yCoords[i]);
		}*/
	}
	public void draw()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	public void calc()
	{
		coordTracker=0;
		reflectY=yOffset*zoom*2;
		long t1 = System.nanoTime();
		for(double y=0;y<height;y+=1)
		{
			for(double x=0;x<width;x+=1)
			{
                cx = ((x - width/2 ) / zoom) + xOffset;
                cy = ((y - height/2) / zoom) + yOffset;
                zxSqr = 0;
                zySqr = 0;
                zProd=0;
                iterations=0;
                while (zxSqr+zySqr < 4 && iterations < maxIterations)
                {
                	zy = zProd + cy;
                    zx = zxSqr-zySqr+cx;
                    zxSqr = zx*zx;
                    zySqr = zy*zy;
                    zProd = 2*zx*zy;
                    iterations++;
                }
                /*xCoords[coordTracker]=x;
                yCoords[coordTracker]=y;
                if(iterations<10){color[coordTracker]=25*iterations;}
                else{color[coordTracker]=iterations;}
                coordTracker++;*/
                if(iterations>=254){g.setColor(Color.BLACK);}
                else if(iterations<10){g.setColor(new Color(0,0,25*iterations));}
                else{g.setColor(new Color(0,0,iterations));}
                g.drawLine((int)x,(int)y,(int)x,(int)y);
				/*f(iterations==maxIterations)
				{
					g.setColor(Color.BLACK);
					g.drawLine((int)x,(int)y,(int)x,(int)y);
				}
				else
				{
					if(iterations<10){g.setColor(new Color(100*iterations));}
					else{g.setColor(new Color(0,0,iterations));}
					g.drawLine((int)x,(int)y,(int)x,(int)y);
				}*/
			}
		}
		System.out.println((System.nanoTime()-t1)/1000000+" millis");
	}

	public void clearCanvas()
	{
		g.clearRect(0,0,width,height);
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
		JFrame frame = new JFrame("Mandelbrot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Main());
		frame.setSize(width,height);
		frame.setVisible(true);
	}
	public void keyPressed(KeyEvent e) 
	{
		double offsetAmount=10/zoom;
		if(e.getKeyCode()== KeyEvent.VK_RIGHT)
		{
			xOffset-=offsetAmount;
		}
		if(e.getKeyCode()== KeyEvent.VK_LEFT)
		{
			xOffset+=offsetAmount;
		}
		if(e.getKeyCode()== KeyEvent.VK_UP)
		{
			yOffset+=offsetAmount;
		}
		if(e.getKeyCode()== KeyEvent.VK_DOWN)
		{
			yOffset-=offsetAmount;
		}
		if(e.getKeyCode()== KeyEvent.VK_SPACE)
		{
			zoom*=2;
			
		}
		if(e.getKeyCode()==KeyEvent.VK_DELETE)
		{
			zoom/=2;
		}
		if(e.getKeyCode()==KeyEvent.VK_E)
		{
			System.out.println("x: "+xOffset);
			System.out.println("y: "+yOffset);
			System.out.println("z: "+zoom);
		}
		
	}
	public void keyReleased(KeyEvent e)
	{
		
	}
	public void keyTyped(KeyEvent e)
	{
		
	}
}