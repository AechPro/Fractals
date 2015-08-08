package Encryption;

import java.util.ArrayList;
import java.util.Random;

public class Fractal {

	private ArrayList<Double> initX;
	private ArrayList<Double> initY;
	private double maxX=500;
	private int counter;
	private double[][] ranges;
	
	public Fractal() 
	{
		initX = new ArrayList<Double>();
		initY = new ArrayList<Double>();
		
		initX.add(-maxX);
		initX.add(maxX);
		initY.add(0.0);
		initY.add(0.0);
		ranges = new double[7][66];
		generateRanges(initX, initY,4);
		unlockFractal(initX, initY, 4);
	}
	
	public double[] unlockFractal(ArrayList<Double> x, ArrayList<Double> y, int complexity)
	{
		//System.out.println();
		int j=0;
		
		ArrayList<Double> newXPoints = new ArrayList<Double>();
		ArrayList<Double> newYPoints = new ArrayList<Double>();
		newXPoints.add(x.get(0));
		newYPoints.add(y.get(0));
		
		for(int i=0;i<y.size()-1;i++)
		{
			double newX=0;
			double newY=0;

			//find midpoints and add offset
			if(x.get(i+1)>x.get(i)){ newX=(x.get(i+1)-(x.get(i+1)-x.get(i))/2); }
			else{ newX=(x.get(i)-(x.get(i)-x.get(i+1))/2); }
			
			if(y.get(i+1)>y.get(i)){ newY=(y.get(i+1)-(y.get(i+1)-y.get(i))/2)-ranges[counter][i]; }
			else{ newY=(y.get(i)-(y.get(i)-y.get(i+1))/2)-ranges[counter][i]; }
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
			counter=0;
			return newPoints;
		}
		else
		{
			counter++;
			return unlockFractal(newXPoints, newYPoints, complexity);
		}

	}
	public double[][] generateRanges(ArrayList<Double> x, ArrayList<Double> y, int complexity)
	{
		Random rand = new Random();
		double H=1.3;
		double maxRange=10000;
		double range = maxRange;
		double N=0;
		int j=0;
		ArrayList<Double> newXPoints = new ArrayList<Double>();
		ArrayList<Double> newYPoints = new ArrayList<Double>();
		newXPoints.add(x.get(0));
		newYPoints.add(y.get(0));
		for(int i=0;i<y.size()-1;i++)
		{
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
			ranges[counter][i]=N;
			
			double newX=0;
			double newY=0;
			if(x.get(i+1)>x.get(i)){ newX=(x.get(i+1)-(x.get(i+1)-x.get(i))/2); }
			else{ newX=(x.get(i)-(x.get(i)-x.get(i+1))/2); }
			if(y.get(i+1)>y.get(i)){ newY=(y.get(i+1)-(y.get(i+1)-y.get(i))/2)-ranges[counter][i]; }
			else{ newY=(y.get(i)-(y.get(i)-y.get(i+1))/2)-ranges[counter][i]; }
			newXPoints.add(newX);
			newXPoints.add(x.get(i+1));

			newYPoints.add(newY);
			newYPoints.add(y.get(i+1));
			
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
			counter=0;
			return ranges;
		}
		else
		{
			counter++;
			return generateRanges(newXPoints, newYPoints, complexity);
		}
	}

}
