import java.net.*;
import java.io.*;
import java.util.*;

class ClientHandler extends Thread
{
   //accepted socket from the webserver
   Socket ClientConnection;
   public ClientHandler(Socket ClientConnection) throws Exception
   {
      this.ClientConnection = ClientConnection;
      start();
   }

   public void run()
   {
      try
      {  //Open connections to the socket
         BufferedReader in = new BufferedReader(new InputStreamReader(ClientConnection.getInputStream()));
         PrintStream out = new PrintStream(new BufferedOutputStream(ClientConnection.getOutputStream()));
         
         //read filename from first input line "GET /filename.html ..."
         String s = in.readLine();
         System.out.println(s);//log the request

         String filename = "";
         StringTokenizer st = new StringTokenizer(s);

         try
         {
            //get the filename from the get command
            if(st.hasMoreElements() && st.nextToken().equalsIgnoreCase("GET") && st.hasMoreElements())
               filename = st.nextToken();
            else
               throw new FileNotFoundException();

            if(filename.endsWith("/"))
               filename += "index.html";

            while(filename.indexOf("/") == 0)
               filename = filename.substring(1);

            if(new File(filename).isDirectory())
            {
               filename = filename.replace("\\", "/");
               out.print("HTTP/1.0 301 moved permanently\r\n"+"Location: /"+filename+"\r\n\r\n");
               out.close();
               return;
            }
            InputStream f = new FileInputStream(filename);
            String mimeType = "text/plain";
            if(filename.endsWith(".html") || filename.endsWith(".htm"))
               mimeType = "text/html";
            out.print("HTTP/1.0 200 OK\r\n"+"Content-type: "+mimeType+"\r\n\r\n");
            //Send file contents to client, then close the connection
            byte[] a = new byte[4096];

            int n;
            while((n=f.read(a)) > 0)
               out.write(a, 0, n);
            out.close();
         }
         catch(FileNotFoundException e)
         { 
             out.println("HTTP/1.0 404 Not Found\r\n"+
               "Content-type: text/html\r\n\r\n"+
               "<html><head></head><body><strong>404 Not Found<strong><hr>"+filename+" not found</body></html>\n");
             out.close();
         }

      }
      catch(Exception e)
      {
         System.out.println(e);
      }
   }
}
class HttpServer
{
   public static void main(String args[])throws Exception
   {
      System.out.println("\n\n\t\tthe Http Server is running....");
      System.out.println("\n\n\t\tPress Ctrl+C to stop the server");
      ServerSocket soc = new ServerSocket(8080);

      try
      {
            while(true)
            {
               Socket in = soc.accept();
               new ClientHandler(in);
            } 
      }
      catch(Exception e)
      {
         System.out.println(e);
      }
   }
}