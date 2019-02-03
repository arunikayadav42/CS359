import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

public class Server
{
	public List<Thread> threads;
	private ServerProperties properties;
	private FileLocks filelocks;

	public Server()
	{
       threads = new ArrayList<Thread>();
       properties = new ServerProperties();
       filelocks = new FileLocks();
	}

	public void run()
	{
		for(int i = 0; i < properties.getNumThreads(); i++)
		{
			Connection connection = new Connection(properties, i, filelocks);
			connection.start();
			threads.add(connection);
		}
		while(true) {
			try {
				Thread.sleep(2000);
				for (int i=threads.size()-1; i>=0; i--) {
					if (!threads.get(i).isAlive()) {
						threads.remove(i);
						Connection connection = new Connection(properties, i, filelocks);
						connection.start();
						threads.add(connection);
					}
				}
			} catch (InterruptedException e) {
				// Error on sleep
			}
		}
	}

	public static void main(String args[])
	{
		Server server = new Server();
		server.run();
	}
}


class HttpSocket
{
	private int portNumber;
	private int maxConnections;
	private ServerSocket serverSocket = null;
	private static HttpSocket instance = null;

	private HttpSocket(int portNumber, int maxConnections)
	{
		this.portNumber = portNumber;
		this.maxConnections = maxConnections;

		try
		{
			serverSocket = new ServerSocket(this.portNumber, this.maxConnections);
		}
		catch(IOException e)
		{
			System.err.println("Error while opening socket on port "+portNumber);
			System.err.println(e.getMessage());
		}
	}

	public static HttpSocket getInstance(int portNumber, int maxConnections)
	{
		if(instance == null)
			instance = new HttpSocket(portNumber, maxConnections);

		return instance;
	}

	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}
}

class Connection extends Thread
{
	private int portNumber = 8000;
	private int executorNumber;
	private HttpSocket httpSocket;
	private Boolean error = false;
	private String root;
	private FileLocks filelocks;
	private ServerProperties properties;
	private List<String> blocked_sites;

	public Connection(ServerProperties properties, int executorNumber, FileLocks filelocks)
	{
		this.portNumber = properties.getPort();
		this.executorNumber = executorNumber;
		this.root = properties.getRoot();
		this.filelocks = filelocks;
		this.blocked_sites = properties.getBlockedSites();
		this.httpSocket = HttpSocket.getInstance(portNumber, properties.getMaxConnections());
	}

	public void run()
	{
		BufferedReader in = null;
		PrintStream out = null;
		while(true)
		{
			try
            {    //Open connections to the socket
            	System.out.println("#" + executorNumber + ": Waiting for connection");
				Socket clientSocket = httpSocket.getServerSocket().accept();
				InetAddress address = InetAddress.getByName("localhost");
				System.out.println(address);
				String ip = clientSocket.getRemoteSocketAddress().toString();
				//System.out.println(ip.split(":")[0].split("/")[1]);
				System.out.println("New client request received");
				System.out.println("Accepted on #" + executorNumber);
		         in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		         out = new PrintStream(new BufferedOutputStream(clientSocket.getOutputStream()));
		         
		         //read filename from first input line "GET /filename.html ..."
		         String s = in.readLine();
		         System.out.println("Request: "+s);//log the request
		         // if(s == null)
		         // {
		         // 	Connection new_connection = new Connection(properties, executorNumber, filelocks);
           //          new_connection.start();
		         // }
		        if(blocked_sites.contains(ip.split(":")[0].split("/")[1]))
				{
					out.println("HTTP/1.0 404 Not Found\r\n"+
		               "Content-type: text/html\r\n\r\n"+
		               "<html><head></head><body><strong>IP Address blocked by the Admin</strong></body></html>\n");
				    out.close();
				    System.out.println("The site is blocked by the admin");
				    continue;
				}
                Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override 
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println(t.getName()+": "+e);
                        Connection new_connection = new Connection(properties, executorNumber, filelocks);
                        new_connection.start();
                    }
                });
		         String filename = "";
		         StringTokenizer st = new StringTokenizer(s);
                 Semaphore lock = null;
		         try
		         {
		            //get the filename from the get command
		            if(st.hasMoreElements() && st.nextToken().equalsIgnoreCase("GET") && st.hasMoreElements())
		               filename = st.nextToken();
		            else
		               throw new FileNotFoundException();
		           //System.out.println(root);
		            lock = filelocks.getLock(filename);
		            if(filename.endsWith("/"))
		               filename += root;

		            while(filename.indexOf("/") == 0)
		               filename = filename.substring(1);
                    //System.out.println(filename);
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
		               "<html><head></head><body>404 Not Found<br><hr>"+filename+" not found</body></html>\n");
		             out.close();
		         }
		         finally
		         {
		            lock.release();	
		         }
		         try
		         {
		         	System.out.println("Thread-"+executorNumber+" is busy...");
		         	Thread.sleep(20000);
		         }
		         catch(InterruptedException e)
		         {
		         	System.out.println(e.getMessage());
		         }
		         System.out.println("Thread-"+executorNumber+" is now free");

                
	      }
	      catch(IOException e)
	      {
	         System.err.println("#" + executorNumber + ": Error while reading data on port " + portNumber);
			 System.err.println(e.getMessage());
			 try {
					in.close();
					out.close();
				}
				catch (Exception ef) {
					// Nothing to do here
				}
	          }

			}
	    }
}

class ServerProperties
{
	private String root;
	private int numThreads;
	private int port;
	private int maxConnections;
	private List<String> blocked_sites;

	public ServerProperties()
	{
		Properties prop = new Properties();
		InputStream inputProperties = null;

		try
		{
           inputProperties = new FileInputStream("config/server.properties");
           prop.load(inputProperties);

           this.root = prop.getProperty("root", "www/index.html");
           String numThreadStr = prop.getProperty("numThreads", "10");
           this.numThreads = Integer.parseInt(numThreadStr);
           String portStr = prop.getProperty("port", "8080");
           this.port = Integer.parseInt(portStr);
           String backLogStr = prop.getProperty("maxConnections", "65536");
           this.maxConnections = Integer.parseInt(backLogStr);
           String blocked_sites = getPropertyList(prop, "blocked_sites");
           this.blocked_sites = Arrays.asList(blocked_sites.split("\\s*,\\s*"));
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Properties file not found. "+e.getMessage());
		}
		catch(IOException e)
		{
			System.err.println("Error while parsing properties file "+e.getMessage());
		}

	}
	public static String getPropertyList(Properties properties, String name) 
	{
	    String s = "";
	    System.out.println("Reached here");
	    for (Map.Entry<Object, Object> entry : properties.entrySet())
	    {
	        if (((String)entry.getKey()).equals(name))
	        {
	        	s = (String)entry.getValue();
	        }
	    }
	    return s;
	}
	public String getRoot()
	{
		return root;
	}
	public void setRoot(String root)
	{
       this.root = root;
	}
	public int getNumThreads() {
		return numThreads;
	}
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getMaxConnections() {
		return maxConnections;
	}
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}
	public void setBlockedSites(List<String> blocked_sites)
	{
		this.blocked_sites = blocked_sites;
	}
	public List<String> getBlockedSites()
	{
		return blocked_sites;
	}
}

class FileLocks
{
	private Map<String, Semaphore> locks;

	public FileLocks()
	{
		locks = new HashMap<String, Semaphore>();
	}

	public synchronized Semaphore getLock(String filename)
	{
		Semaphore lock = locks.get(filename);
		if(lock == null)
		{
			lock = new Semaphore(1);
			locks.put(filename, lock);
		}
		return lock;
	}
	public synchronized void removeLock(String filename)
	{
		locks.remove(filename);
	}
}