import java.io.*;
import java.util.*;
import java.lang.*;

import java.io.File;  
import java.io.FileWriter;
import java.io.IOException;

class Packet implements Comparable<Packet>{
		int packet_id;
		String source_id;
		double occ_time_actual;
		double occ_time_ideal;
		int type;
        
        public int compareTo(Packet p2) 
            {
            	int id_1 = Integer.parseInt(this.source_id.substring(1));
            	int id_2 = Integer.parseInt(p2.source_id.substring(1));
                if (this.occ_time_actual > p2.occ_time_actual) {
                    return 1; 
                }
                else if (this.occ_time_actual < p2.occ_time_actual) 
                    return -1; 
                else {
                	if(this.type > p2.type || id_1 > id_2)
                		return 1;
                }
                return 0; 
            }  

		public Packet(double occ_time_ideal, int type, double occ_time_actual, int packet_id, String source_id)
		{
			this.occ_time_ideal = occ_time_ideal;
			this.type = type;
			this.occ_time_actual = occ_time_actual;
			this.packet_id = packet_id;
			this.source_id = source_id;
		}

		public boolean Sent(Packet e)
		{
	      if(e.type == 0)
	      	return true;
	      return false;
		}
		public boolean Processing(Packet e)
		{
	       if(e.type == 1)
	      	return true;
	      return false;
		}
		public boolean Dispatch(Packet e)
		{
	        if(e.type == 2)
	      	   return true;
	        return false;
		}
	}

public class ques1
{
	static class FastReader 
    { 
        BufferedReader br; 
        StringTokenizer st; 
  
        public FastReader() 
        { 
            br = new BufferedReader(new InputStreamReader(System.in)); 
        } 
  
        String next() 
        { 
            while (st == null || !st.hasMoreElements()) 
            { 
                try
                { 
                    st = new StringTokenizer(br.readLine()); 
                } 
                catch (IOException  e) 
                { 
                    e.printStackTrace(); 
                } 
            } 
            return st.nextToken(); 
        } 
  
        int nextInt() 
        { 
            return Integer.parseInt(next()); 
        } 
  
        long nextLong() 
        { 
            return Long.parseLong(next()); 
        } 
  
        double nextDouble() 
        { 
            return Double.parseDouble(next()); 
        } 
  
        String nextLine() 
        { 
            String str = ""; 
            try
            { 
                str = br.readLine(); 
            } 
            catch (IOException e) 
            { 
                e.printStackTrace(); 
            } 
            return str; 
        } 
    }
	public static class Source
	{
		int id;
		double rate;
		double packet_generation_start_time;
		double self_delay;
		public Source(int id, double rate)
		{
			this.id = id;
			this.rate = rate;
			packet_generation_start_time = -1;
			self_delay = 0.0;
		}
	}

	public static class Link
	{
		double transfer_time;
		public Link(double transfer_time)
		{
			this.transfer_time = transfer_time;
		}
	}

	public static class Result
    {
        double average_delay;
        double rate;
        public Result(double average_delay, double rate)
        {
            this.average_delay = average_delay;
            this.rate = rate;
        }
    }
	
	public static void printPacketsQueue(ArrayList<Packet> queue)
	{
		for(Packet i: queue){
			System.out.print(i.packet_id+", ");
		}
		System.out.println();         
	}
    
    public static void printPresentOccurrenceTime(ArrayList<Packet> queue)
	{
		for(Packet i: queue){
			System.out.print("("+Integer.parseInt(i.source_id.substring(1))+", "+i.packet_id+", "+i.occ_time_actual+") | ");
		}
		System.out.println();         
	}

	public static void main(String[] args)
	{
		LinkedHashMap<Integer, ArrayList<Result>> map = new LinkedHashMap<>();
		int n_of_sources = 0, n_of_packets = 0;
		while(true)
		{
			FastReader obj = new FastReader();
            System.out.println("Do you want to enter further data?");
            System.out.println("Enter Y or N");
            String s = obj.next();
            if(s.compareTo("N") == 0 || s.compareTo("n") == 0)
            	{
            		System.out.println("Exiting");
            		break;
            	}
        
		System.out.println("Enter number of sources");
		n_of_sources = obj.nextInt();
		//making sources
		Source array_of_source_objects[] = new Source[n_of_sources];
		for(int i = 0; i < n_of_sources; i++)
		{
			System.out.println("Enter rate for source "+(i+1));
			double rate = 0.0;
			rate = obj.nextDouble();
			Source new_source = new Source(i, rate);
			array_of_source_objects[i] = new_source;
		}
		System.out.println("Enter time for transfer for link between source and switch");
		Link link1 = new Link(obj.nextDouble());

		System.out.println("Enter time for transfer for link between switch and sink");
		Link link2 = new Link(obj.nextDouble());

		// System.out.println("Enter the processing time by the switch");
		// Link switch_link = new Link(obj.nextDouble());

		System.out.println("Enter total number of packets to be sent");
		n_of_packets = obj.nextInt();
		int temp = 1;
		//PriorityQueue<Packet> packetsqueue = new PriorityQueue<>(temp, new Sorted());
		ArrayList<Packet> packetsqueue = new ArrayList<>();
		ArrayList<Packet> new_pak = new ArrayList<>();
		float time = 0;

		//---------------------------------------populating the queue of packets
		while(temp != n_of_packets+1)
		{
			if(array_of_source_objects[0].packet_generation_start_time == -1)
			{
				  array_of_source_objects[0].packet_generation_start_time = 0;  
				  Packet new_packet = new Packet(array_of_source_objects[0].packet_generation_start_time, 0, 0, temp, "S"+0);
			      packetsqueue.add(new_packet);
			                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		    }
		    else
		    {
				  Packet new_packet = new Packet(array_of_source_objects[0].packet_generation_start_time, 0, array_of_source_objects[0].packet_generation_start_time, temp, "S"+0);
			      packetsqueue.add(new_packet);
			      // Collections.sort(new_pak);
			      //Collections.sort(packetsqueue); 
		    }
			temp++;
			array_of_source_objects[0].packet_generation_start_time += array_of_source_objects[0].rate;
		   //System.out.println("Here");

		}
		
		// if(array_of_source_objects[0].packet_generation_start_time == -1){
		// 	  array_of_source_objects[0].packet_generation_start_time = 0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
		// }
		// Packet new_packet = new Packet(array_of_source_objects[0].packet_generation_start_time, 0);
		// packetsqueue.add(new_packet);
		temp = n_of_packets;
		double delay = 0.0;
        printPacketsQueue(packetsqueue);
        double tt[] = new double[3];
		while(!packetsqueue.isEmpty())
		{
			Packet current_packet = packetsqueue.get(0);
			packetsqueue.remove(current_packet);
			int source_id = Integer.parseInt(current_packet.source_id.substring(1));
            //packetsqueue.update();
            
            if(current_packet.Sent(current_packet) && tt[0] >= current_packet.occ_time_actual)
            {
            	System.out.println(current_packet.packet_id+" has been Sent by the source ");
                current_packet.occ_time_actual = tt[0] + link1.transfer_time;
                current_packet.type = 1;
                tt[0] = current_packet.occ_time_actual;
                packetsqueue.add(0, current_packet);
                Collections.sort(packetsqueue);
            }
            // else if(current_packet.Processing(current_packet))
            // {
            //    System.out.println(current_packet.packet_id+" reached point 1");
            //    current_packet.occ_time_actual = Math.max(tt[1], current_packet.occ_time_actual) + switch_link.transfer_time;
            //    current_packet.type = 2;
            //    tt[1] = current_packet.occ_time_actual;
            //    packetsqueue.add(0, current_packet);
            //    Collections.sort(packetsqueue);
            // }
            else if(current_packet.Processing(current_packet))
            {
               System.out.println(current_packet.packet_id+" reached point 1");
               current_packet.occ_time_actual = Math.max(tt[1], current_packet.occ_time_actual) + link2.transfer_time;
               current_packet.type = 2;
               tt[1] = current_packet.occ_time_actual;
               packetsqueue.add(0, current_packet);
               Collections.sort(packetsqueue);
            }
            else if(current_packet.Dispatch(current_packet)){
            	System.out.println(current_packet.packet_id+" reached point 2");
                delay = current_packet.occ_time_actual - (current_packet.occ_time_ideal + link1.transfer_time + link2.transfer_time);
                array_of_source_objects[source_id].self_delay += delay;
                tt[2] = current_packet.occ_time_actual;
                System.out.println(current_packet.packet_id+" delay calculated");
            }
            if(packetsqueue.size() != 0)
            {
            	System.out.println("\n\n\n-------------------------------------------------");
		        System.out.println("...| (Packet_Number, TimeStamp)|...");
		        printPresentOccurrenceTime(packetsqueue);
		        System.out.println("-------------------------------------------------\n\n");
            }
			
		}
		System.out.println("-------------------------------\n\n"+"\033[0;1m"+"The delay is " + delay);
        ArrayList<Result> list = map.get(0);
        delay = array_of_source_objects[0].self_delay / n_of_packets;
		if(list == null)
		{
           list = new ArrayList<Result>();
           
		}
		list.add(new Result(delay, array_of_source_objects[0].rate));
        map.put(0, list);
		}
		String eol = System.getProperty("line.separator");

		// for(int i = 0; i <= n_of_sources; i++)
		// {
			try (Writer writer = new FileWriter("ques1_source"+0+".csv"))
			{
               writer.append("Packet Sending Rate").append(",").append("Average Delay").append(eol); 
		       ArrayList<Result> list = map.get(0);
		       for(Result r: list)
		       {
		       	   writer.append(r.rate+"").append(",").append(r.average_delay+"").append(eol);
		       }
		       
		    } 
			catch (IOException ex) 
			{
			  ex.printStackTrace(System.err);
			}
		// }
		// JSONObject json = new JSONObject();
	 //    json.putAll(map);
	 //    System.out.printf( "JSON: %s", json.toString(2) );
		}
}