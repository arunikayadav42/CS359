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

public class problem1_part_c
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
		double packet_loss_rate;
		int number_of_packets_lost_till_now;
        double time_last_packet_was_dropped;
		public Source(int id, double rate)
		{
			this.id = id;
			this.rate = rate;
			packet_generation_start_time = -1;
			self_delay = 0.0;
			packet_loss_rate = 0.0;
			time_last_packet_was_dropped = 0.0;
			number_of_packets_lost_till_now = 0;
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
        double packet_loss_rate;
        double rate;
        public Result(double packet_loss_rate, double rate)
        {
            this.packet_loss_rate = packet_loss_rate;
            this.rate = rate;
        }
    }

	public static void printPacketsQueue(ArrayList<Packet> queue)
	{
		for(Packet i: queue){
			System.out.println(i.packet_id+", "+i.source_id + ", "+i.occ_time_ideal);
		}
		System.out.println();         
	}
    
    public static void printPresentOccurrenceTime(ArrayList<Packet> queue)
	{
		for(Packet i: queue){
			System.out.print("(s"+Integer.parseInt(i.source_id.substring(1))+", "+i.packet_id+", "+i.occ_time_actual+") | ");
		}
		System.out.println();         
	}

	public static double nextTime(double rateParameter)
	{
		return -Math.log(1.0 - Math.random()) / rateParameter;
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
		Source array_of_source_objects[] = new Source[n_of_sources+1];
		for(int i = 1; i <= n_of_sources; i++)
		{
			System.out.println("Enter rate for source "+(i));
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
		int temp_no_of_sources = 1;
		System.out.println("Enter the size of the buffer at the switch");
		int size_of_buffer = obj.nextInt();
		ArrayList<Packet> packetsqueue = new ArrayList<>();
		ArrayList<Packet> finite_sized_queue = new ArrayList<>();
		int temp = 1;
        while(temp_no_of_sources != n_of_sources+1)
        {
            System.out.println("Enter total number of packets to be sent for source " + temp_no_of_sources);
		    n_of_packets = obj.nextInt();
		    temp = 1;
		    double lastTime = 0.0;
		    while(temp != n_of_packets+1)
			{
				double packet_generation_start_time = array_of_source_objects[temp_no_of_sources].packet_generation_start_time;
				if(array_of_source_objects[temp_no_of_sources].packet_generation_start_time == -1)
				{
					  array_of_source_objects[temp_no_of_sources].packet_generation_start_time = 0;
					  packet_generation_start_time = 0;  
					  Packet new_packet = new Packet(packet_generation_start_time, 0, 0, temp, "S"+temp_no_of_sources);
				      packetsqueue.add(new_packet);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
			    }
			    else
			    {
					  Packet new_packet = new Packet(packet_generation_start_time, 0, packet_generation_start_time, temp, "S"+temp_no_of_sources);
				      packetsqueue.add(new_packet);
				      // Collections.sort(new_pak);
			    }
				Collections.sort(packetsqueue); 
				array_of_source_objects[temp_no_of_sources].packet_generation_start_time += nextTime(array_of_source_objects[temp_no_of_sources].rate) + lastTime;
				lastTime = array_of_source_objects[temp_no_of_sources].packet_generation_start_time;
			    temp++;
			   //System.out.println("Here");
			}
			temp_no_of_sources++;
        }
		//PriorityQueue<Packet> packetsqueue = new PriorityQueue<>(temp, new Sorted());
		float time = 0;
        printPacketsQueue(packetsqueue);
		//---------------------------------------populating the queue of packets
		
		double packet_loss_rate = 0.0;

        double tt[] = new double[3];
        Packet current_packet = null;
		while(!packetsqueue.isEmpty())
		{
			current_packet = packetsqueue.get(0);
			Packet packet_from_buffer = !finite_sized_queue.isEmpty() ? finite_sized_queue.get(0) : null; 
			int source_id = Integer.parseInt(current_packet.source_id.substring(1));
			
            //packetsqueue.update();
        	if(current_packet.Sent(current_packet))
            {
                    current_packet.occ_time_actual = Math.max(tt[0], current_packet.occ_time_actual) + link1.transfer_time;
                	if(finite_sized_queue.size() == size_of_buffer)
                	{
                        array_of_source_objects[source_id].number_of_packets_lost_till_now++;
                        array_of_source_objects[source_id].time_last_packet_was_dropped = current_packet.occ_time_actual;
                	    System.out.println(current_packet.packet_id+" from source "+current_packet.source_id+" was dropped, reached the queue at "+current_packet.occ_time_actual);
                	}
                	else
                	{
                		System.out.println(current_packet.packet_id+" has been Sent by the source s"+source_id);
		            	current_packet.type = 1;
		                finite_sized_queue.add(0, current_packet);
		                Collections.sort(finite_sized_queue);
		                
                	}
                	packetsqueue.remove(current_packet);
                	tt[0] = current_packet.occ_time_actual;	                                    
            }
            
            if(packet_from_buffer != null && tt[1] <= tt[0])
            {
               System.out.println(packet_from_buffer.packet_id+" processed by the switch");
               packet_from_buffer.occ_time_actual = Math.max(tt[1], packet_from_buffer.occ_time_actual) + link2.transfer_time;
               packet_from_buffer.type = 2;
               tt[1] = packet_from_buffer.occ_time_actual;
               finite_sized_queue.remove(packet_from_buffer);
            }
          //   if(finite_sized_queue.size() != 0)
          //   {
          //   	System.out.println("\n\n\n-------------------------------------------------");
		        // System.out.println("...| (Source_id, Packet_Number, TimeStamp)|...");
		        // printPresentOccurrenceTime(packetsqueue);
		        // System.out.println("-------------------------------------------------\n\n");
          //   }
          //   
          if(!finite_sized_queue.isEmpty())
          {
          	System.out.println("Printing finite sized queue");
            printPresentOccurrenceTime(finite_sized_queue);
          }
          else
          	System.out.println("finite sized queue is empty");
		}
		for(int i = 1; i <= n_of_sources; i++)
		{
            ArrayList<Result> list = map.get(i);
            System.out.println("Time last packet was dropped "+array_of_source_objects[i].time_last_packet_was_dropped);
            if(array_of_source_objects[i].time_last_packet_was_dropped == 0)
            	packet_loss_rate = 0.0;
            else
            packet_loss_rate = array_of_source_objects[i].time_last_packet_was_dropped / array_of_source_objects[i].number_of_packets_lost_till_now;
			if(list == null)
			{
               list = new ArrayList<Result>();
               
			}
			list.add(new Result(packet_loss_rate, array_of_source_objects[i].rate));
            map.put(i, list);
		}
		}
		String eol = System.getProperty("line.separator");

		for(int i = 1; i <= n_of_sources; i++)
		{
			try (Writer writer = new FileWriter("problem1_part_c_source"+i+".csv"))
			{
               writer.append("Packet Sending Rate").append(",").append("Packet Loss Rate").append(eol); 
		       ArrayList<Result> list = map.get(i);
		       for(Result r: list)
		       {
		       	   writer.append(r.rate+"").append(",").append(r.packet_loss_rate+"").append(eol);
		       }
		       
		    } 
			catch (IOException ex) 
			{
			  ex.printStackTrace(System.err);
			}
		}
		}
	}