// Código Java para probar la serialización y deserialización con ayuda de la clase SerializationUtils.java
import java.io.*;

class Demo implements java.io.Serializable
{
	public String servidor1;
	public String servidor2;
	public String servidor3;
	public String servidor4;

	// Default constructor
	public Demo(String servidor1, String servidor2, String servidor3, String servidor4)
	{
		this.servidor1 = servidor1;
		this.servidor2 = servidor2;
		this.servidor3 = servidor3;
		this.servidor4 = servidor4;
	}
	void imprimirDemo(){
		System.out.println("servidor1:" + servidor1);
		System.out.println("servidor2:" + servidor2);
		System.out.println("servidor3:" + servidor3);
		System.out.println("servidor4:" + servidor4);
	}
}
/*
class Test
{
	public static void main(String[] args)
	{
		Demo object = new Demo(2022, "Prueba serializacion y deserializacion");
		Demo objeto = null;
		
		byte[] serializado = SerializationUtils.serialize(object);
		System.out.println(serializado);
		objeto = (Demo)SerializationUtils.deserialize(serializado);
		System.out.println("a = " + objeto.a);
		System.out.println("b = " + objeto.b);
	}
}
*/