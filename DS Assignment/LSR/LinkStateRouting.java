package com.LSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LinkStateRouting {
	static int routerCount=0;
	static int[][] point=new int [50][50];
	static Router[] routers=new Router[50];
	
	public static class Router{
		private String routerId;
		private String networkName;
		int sequenceNumber=1;
		int tick=0;
		int networkCost=1;
		int directConnections[][];
		
		public void receivePacket(LinkStatePackage a){ 
			if(tick==0){
				if(a.getTTL()>0){
					a.setTTL(a.getTTL() - 1);
					for(int j=0;j<10;j++){
						if(directConnections[j][1]!=0&&a.getTTL()>0){
							routers[j].receivePacket(a);
						}
					}
				}
			}
		}
		public void originatePacket(){
			if(tick==0){
				LinkStatePackage packet=new LinkStatePackage();
				packet.setOriginatingRouterId(routerId);
				packet.setSequenceNumber(this.sequenceNumber);
				this.sequenceNumber++;
				packet.setTTL(10);
				for(int j=0;j<10;j++){
					if(directConnections[j][1]!=0){
						routers[j].receivePacket(packet);
					}
				}
			}
		}
	}

	public static void DijkstraShortestPath(int start){
		int[] distance=new int[10];
		int[] previous=new int[10];
		Boolean[] state=new Boolean[10];
		int n=routerCount;

		for(int i=0; i<n; ++i){
			distance[i] =routers[start].directConnections[i][1];
			state[i] = false;                                
			if(distance[i] == 0)    
				previous[i] = -1;
			else 
				previous[i] = start;
	    }
		distance[start] = 0;
		state[start] = true;
		for(int i=0;i<distance.length;i++){
			if(i!=start&&distance[i]==0){
				distance[i]=32767;
			}
		}
		for(int i=1; i<n; i++){
			int mindist = 32767;
			int u = start;                           
			for(int j=0; j<n; ++j){
				if((!state[j]) && distance[j]<mindist&&distance[j]>0){
					u = j;                             
					mindist = distance[j];
				}
			}
			state[u] = true;  

			for(int j=0; j<routerCount; j++){
				if((!state[j]) && routers[u].directConnections[j][1]<10&& routers[u].directConnections[j][1]>0){  
					if(distance[u] + routers[u].directConnections[j][1] < distance[j]){
						distance[j] = distance[u] + routers[u].directConnections[j][1];    
						previous[j] =u;
					}             
				}	            
			}
		} 
		int min=32767;
		int z=0;
		for(int i=0;i<routerCount;i++){
			if(previous[i]==start&&routers[start].directConnections[i][1]<min){
				min=routers[start].directConnections[i][1];
				z=i;
			}
		  
		} 
		System.out.println(routers[start].networkName+"   "+min+"  "+z);
	}
	public static void setupRouters(){
		int  MAXINT = 32767;
		//initialize scanner
		Scanner input=new Scanner(System.in);
		//Input file name from user
		System.out.println("Enter the file name: "); 
		String fileName=input.next();
		File file = new File(fileName);
		BufferedReader br = null;

		try {

			br = new BufferedReader(new FileReader(file));
			String [] temp = new String[100];
			int num = 0;
			int line=1;
			String[] items=null; 

			while ((temp[line] = br.readLine()) != null) {
				items=temp[line].split(" ");
				if(!items[0].equals("")){
					num++;
					routerCount++;
					routers[num-1]=new Router();
					routers[num-1].routerId=items[0]; 
					routers[num-1].directConnections=new int[10][10];
					routers[num-1].networkName=items[1];
				}
				if(items[0].equals("")){
					int CR=Integer.parseInt(items[1]);
					if(items.length==3){
						routers[num-1].directConnections[CR][1]=Integer.parseInt(items[2]);
					}
					else{
						routers[num-1].directConnections[CR][1]=1;
					}
				}
				line++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//main function
	public static void main(String[] args) {
		//setup routers
		setupRouters();
		//running loop for user input
		while(true){
			System.out.println("Choose from the following actions:");
			System.out.println("1. Press C to Continue");
			System.out.println("2. Press Q to Quit");
			System.out.println("3. Press P to print routing table");
			System.out.println("4. Press S followed by router ID to shut down router");
			System.out.println("4. Press T followed by router ID to start up router");
			System.out.println("---------------------------------------------------------");
			Scanner s = new Scanner(System.in);
			String input = s.nextLine();
			if(input.equals("C")){ 
				for(int i=0;i<routerCount;i++){
					routers[i].originatePacket();
				}
			}
			if(input.equals("Q")){
				break;
			}
			if(input.equals("P")){
				for(int i=0;i<routerCount;i++){
					DijkstraShortestPath(i);
				}
			}
			if(input.equals("S")){
				System.out.println("Please enter router's id");
				String routerId = s.nextLine();
				int m=Integer.parseInt(routerId);
				routers[m].tick=1;
				for(int i=0;i<routerCount;i++){
					point[m][i]=routers[m].directConnections[i][1];
					routers[m].directConnections[i][1]=32767;
				}
			}
			if(input.equals("T")){
				System.out.println("Please enter router's id");
				String routerId = s.nextLine();
				int m=Integer.parseInt(routerId);
				routers[m].tick=0;
				for(int i=0;i<routerCount;i++){
					routers[m].directConnections[i][1]=point[m][i];
				}
			}
		}
	}
}
