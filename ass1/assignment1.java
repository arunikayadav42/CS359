import java.io.*;
import java.util.*;
import java.lang.*;


class Packet implements Comparable<Packet>{
		int packet_id;
		double occ_time_actual;
		double occ_time_ideal;
		int type;
        
        public int compareTo(Packet p2) 
            { 
                if (this.occ_time_actual < p2.occ_time_actual) {
                    return 1; 
                }
                else if (this.occ_time_actual > p2.occ_time_actual) 
                    return -1; 
                else {
                	if(this.packet_id < p2.packet_id)
                		return 1;
                }
                return 0; 
            } 

		public Packet(double occ_time_ideal, int type, double occ_time_actual, int packet_id)
		{
			this.occ_time_ideal = occ_time_ideal;
			this.type = type;
			this.occ_time_actual = occ_time_actual;
			this.packet_id = packet_id;
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

public class assignment1
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
    // static class Sorted implements Comparator<Packet>
	   //  { 
	              
    //         // Overriding compare()method of Comparator  
    //                     // for descending order of cgpa 
            
	   //  } 

	public static class Source
	{
		int id;
		double rate;
		double packet_generation_start_time;
		public Source(int id, double rate)
		{
			this.id = id;
			this.rate = rate;
			packet_generation_start_time = -1;
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

	
	
	public static void printPacketsQueue(ArrayList<Packet> queue)
	{
		for(Packet i: queue){
			System.out.print(i.packet_id+", ");
		}
		System.out.println();         
	}

	public static void main(String[] args)
	{
		int n_of_sources, n_of_packets;

		FastReader obj = new FastReader();
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

		System.out.println("Enter the processing time by the switch");
		Link switch_link = new Link(obj.nextDouble());

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
				  Packet new_packet = new Packet(array_of_source_objects[0].packet_generation_start_time, 0, 0, temp);
			      packetsqueue.add(new_packet);
			                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		    }
		    else
		    {
				  Packet new_packet = new Packet(array_of_source_objects[0].packet_generation_start_time, 0, -1, temp);
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
		while(!packetsqueue.isEmpty())
		{
			Packet current_packet = packetsqueue.get(0);
			packetsqueue.remove(current_packet);
            //packetsqueue.update();
            System.out.print(current_packet.packet_id+" removed ");
            printPacketsQueue(packetsqueue);
			if(current_packet.Sent(current_packet))
			{
				int flag = -1;
				System.out.println("Packet sent from source :"+ (current_packet.packet_id));
			     current_packet.type = 1;
			     current_packet.occ_time_actual += link1.transfer_time;
			     current_packet.occ_time_ideal += link1.transfer_time;
			     
			     ArrayList<Packet> temp_packet_storage = new ArrayList<>();
			     printPacketsQueue(packetsqueue);
			     while(!packetsqueue.isEmpty())
			     {
			     	Packet next_packet = packetsqueue.get(0);
                    packetsqueue.remove(next_packet);
			     	if(next_packet.occ_time_ideal < current_packet.occ_time_actual)
			     	{
			     		System.out.println("Next Packet to be sent from source:"+ (next_packet.packet_id));
			     		next_packet.occ_time_actual = current_packet.occ_time_actual + link1.transfer_time;
			     		next_packet.occ_time_ideal += link1.transfer_time;
			     		next_packet.type = 1;
				     	temp_packet_storage.add(next_packet);
				     	Collections.sort(temp_packet_storage);
				     	flag = 0;
			     	    break;
			     	}
			     	else
			     	{
			          temp_packet_storage.add(next_packet);
			          Collections.sort(temp_packet_storage);
			     	}

			     }
			     if(flag == -1 && temp_packet_storage.size() != 0){
			     	Packet i = temp_packet_storage.get(0);
			     	i.occ_time_actual = i.occ_time_ideal;
			     }
			     // packetsqueue.add(current_packet);
        //          Collections.sort(packetsqueue);
			     packetsqueue.addAll(temp_packet_storage);
                 Collections.sort(packetsqueue);	     
			}
			if(current_packet.Processing(current_packet))
			{
				 int flag = -1;
				 System.out.println("Packet being processed by the switch :"+ (current_packet.packet_id));
			     current_packet.type = 2;
			     current_packet.occ_time_actual += switch_link.transfer_time;
			     current_packet.occ_time_ideal += switch_link.transfer_time;
			     ArrayList<Packet> temp_packet_storage = new ArrayList<>();
			     while(!packetsqueue.isEmpty())
			     {
			     	Packet next_packet = packetsqueue.get(0);
                    packetsqueue.remove(next_packet);
			     	if(next_packet.occ_time_ideal < current_packet.occ_time_actual && next_packet.type == 1)
			     	{
			     		System.out.println("Next Packet being processed by the switch :"+ (next_packet.packet_id));
			     		next_packet.occ_time_actual += switch_link.transfer_time;
			     		next_packet.occ_time_ideal += switch_link.transfer_time;
				     	temp_packet_storage.add(next_packet);
				     	Collections.sort(temp_packet_storage);
				     	flag = 0;
			     	    break;
			     	}
			     	else if(next_packet.occ_time_ideal < current_packet.occ_time_actual && next_packet.type == 0)
			     	{
			     		System.out.println("Next Packet being processed by the switch :"+ (next_packet.packet_id));
			     		next_packet.occ_time_actual += link1.transfer_time;
			     		next_packet.occ_time_ideal += link1.transfer_time;
			     		next_packet.type = 0;
				     	temp_packet_storage.add(next_packet);
				     	Collections.sort(temp_packet_storage);
				     	flag = 0;
			     	    break;
			     	}
			    else
			     	{
			          temp_packet_storage.add(next_packet);
			          Collections.sort(temp_packet_storage);
			     	}
			     }
			     if(flag == -1 && temp_packet_storage.size() != 0){
			     	Packet i = temp_packet_storage.get(0);
			     	i.occ_time_actual = i.occ_time_ideal;
			     }
			     // packetsqueue.add(current_packet);
        //          Collections.sort(packetsqueue);
			     packetsqueue.addAll(temp_packet_storage);
                 Collections.sort(packetsqueue);
			     //printPacketsQueue(packetsqueue);
			}
			if(current_packet.Dispatch(current_packet))
			{
		        delay += current_packet.occ_time_actual - current_packet.occ_time_ideal;
                current_packet.occ_time_actual += link2.transfer_time;
			     current_packet.occ_time_ideal += link2.transfer_time;
		        System.out.println("Packet being dispatched is---------------"+current_packet.packet_id);
		        System.out.println("Actual Sink time : "+(current_packet.occ_time_actual+ link2.transfer_time)+" Ideal Sink Time :" + (current_packet.occ_time_ideal+ link2.transfer_time));
		        System.out.println("Delay is : "+delay);
		        ArrayList<Packet> temp_packet_storage = new ArrayList<>();
			     while(!packetsqueue.isEmpty())
			     {
			     	Packet next_packet = packetsqueue.get(0);
                    packetsqueue.remove(next_packet);
			     	if(next_packet.occ_time_ideal < current_packet.occ_time_actual && next_packet.type == 1)
			     	{
			     		next_packet.occ_time_actual += link2.transfer_time;
				     	temp_packet_storage.add(next_packet);
				     	Collections.sort(temp_packet_storage);
			     	    break;
			     	}
			    else
			     	{
			          temp_packet_storage.add(next_packet);
			          Collections.sort(temp_packet_storage);
			     	}
			     }
			     packetsqueue.addAll(temp_packet_storage);
                 Collections.sort(packetsqueue);
		        printPacketsQueue(packetsqueue);
			}
		}
		System.out.println("\033[0;1m"+"The delay is " + delay);
		}
}