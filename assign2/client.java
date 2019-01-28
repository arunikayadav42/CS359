import java.net.*;
import java.io.*;

public class client
{
	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream out = null;

	public client(String address, int port)
	{
      try{
      	socket = new Socket(address, port);
      	System.out.println("Connected");
      }
      catch(UnknownHostException u)
      {
      	System.out.println(u);
      }
      catch(IOException i)
      {
      	System.out.println(i);
      }
      String line = "";
      while(!line.equals("Over"))
      {
      	try
      	{
      		line = input.readLine();
      		out.writeUTF(line);
      	}
      	catch(IOException i)
      	{
      		System.out.println(i);
      	}
      }
      try
      {
      	input.close();
      	out.close();
      	socket.close();
      }
      catch(IOException i)
      {
      	System.out.println(i);
      }
	}
	public static void main(String args[])
	{
		client client = new client("172.16.158.225", 5000);
	}
}