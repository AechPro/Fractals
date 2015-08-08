package Encryption;

public class TypeOne
{
	public TypeOne() 
	{
		long start = System.nanoTime();
		Fractal f = new Fractal();
		long avg=System.nanoTime()-start;

		System.out.println(avg+" nanoseconds");
		System.out.println(avg/1000+" microseconds");
		System.out.println(avg/1000000+" milliseconds");


	}

	public static void main(String[] args)
	{
		TypeOne s = new TypeOne();
	}


}

