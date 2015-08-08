package BasicGenerator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class WaterSection
{
	private int x;
	private int y;
	private int width;
	private int height;
	private double ySpd;
	private int xSpd;
	private int prevX;
	private int prevY;
	private boolean moved=true;

	public WaterSection(int _x, int _y, int _w, int _h)
	{
		x=_x;
		y=_y;
		width=_w;
		height=_h;
		xSpd=3;
		ySpd=1;
	}

	public void update(int newX, int newY)
	{

		moved=true;
		if(y>newY-ySpd && y<newY+ySpd){moved=true; prevX=x;}
		else 
		{
			if(y<newY){y+=ySpd; moved=false;}
			else if(y>newY){y-=ySpd; moved=false;}
		}
		if(x>newX-xSpd && x<newX+xSpd){moved=true;prevY=y;}
		else
		{
			if(x<newX){x+=xSpd; moved=false;}
			else if(x>newX){x-=xSpd; moved=false;}
		}





		//System.out.println("x: "+x+" newX: "+newX);
	}
	public void render(Graphics2D g)
	{
		g.setColor(new Color(0,125,255,225));
		g.fillOval((int)x,(int)y,width,height);
	}

	public boolean hasMoved(){return moved;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public int getX(){return x;}
	public int getY(){return y;}
	public void setX(int i){x=i;}
	public void setY(int i){y=i;}
	public void setWidth(int i){width=i;}
	public void setHeight(int i){height=i;}
}
