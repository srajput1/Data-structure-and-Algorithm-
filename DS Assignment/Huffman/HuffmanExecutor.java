package com.huffman;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class HuffmanExecutor {

	//read file
	public String readFile(String x) {
		
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader brd=new BufferedReader(new FileReader(x));
			String inputLine=new String();
			try {
				inputLine=brd.readLine();
				while (inputLine  != null) {
					builder.append(inputLine);
					//builder.append("\n");
					inputLine=brd.readLine();
				}
				
			}catch(IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if(brd!=null) {
						brd.close();
					}
				}catch(IOException exc) {
					exc.printStackTrace();
				}
			}
		}catch(FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("No such file exists.");
		}
		return builder.toString();
	}
	//method to filter text to alphanumeric
	public String textFilter(String rawText) {
		//initialize strings
		String filterText=new String();
		if(rawText.isEmpty()) {
			return null;
		}
		
		return rawText.replaceAll("[^A-Za-z0-9]","");
	}
	//main method 
	public static void main(String[] args) {
		
		//initialize scanner
		Scanner input=new Scanner(System.in);
		
		//Input file name from user
		System.out.println("Enter the file name: ");  
		String fileName=input.next();
		
		//initialize class instance
		HuffmanExecutor h=new HuffmanExecutor();
		
		//reading text from file
		String inputText=h.readFile(fileName);
		
		//filtering text
		String filterText=h.textFilter(inputText);
		
		if(!filterText.isEmpty()) {
			HuffmanGenerator hg=new HuffmanGenerator(filterText);
			hg.huffmanControl();
		}
	}
}

