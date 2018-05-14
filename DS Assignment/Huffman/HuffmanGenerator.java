package com.huffman;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HuffmanGenerator {

	//declaring variable
	private String text;
	private HashMap<Character,Integer> frequencies;
	public PriorityQueue<HuffmanNode> pq;
	public HuffmanNode treeRoot;
	public HashMap<Character,String> encodings;
	
	public HuffmanGenerator(String filterText) {
		this.text=filterText;
		frequencies=new HashMap<Character,Integer>();
		pq=new PriorityQueue();
		encodings=new HashMap();
	}
	public void calculateFrequency() {
		for(int i=0;i<text.length();i++) {
			if(frequencies.containsKey(text.charAt(i))) {
				int currFreq=frequencies.get(text.charAt(i));
				currFreq=currFreq+1;
				frequencies.put(text.charAt(i), currFreq);
			}
			else {	
				frequencies.put(text.charAt(i),1);
			}
		}
		
	}
	public void createPriorityQueue() {
		for(char c:frequencies.keySet()) {
			pq.add(new HuffmanNode(c,frequencies.get(c)));
		}
	}
	public void getTreeRoot() {
		treeRoot=buildHuffmanTree();
	}
	public HuffmanNode buildHuffmanTree() {
		HuffmanNode nh;
		while(pq.size()>1) {
			HuffmanNode h1=pq.poll();
			HuffmanNode h2=pq.poll();
			int newfreq=h1.getFrequency()+h2.getFrequency();
			nh=new HuffmanNode('$',newfreq);
			nh.setRight(h1);
			nh.setLeft(h2);
			pq.add(nh);
			buildHuffmanTree();
		}
		return pq.peek();
	}
	public void createEncoding(HuffmanNode root,String code) {
		if(Character.isAlphabetic(root.item)||Character.isDigit(root.item)) {
			encodings.put(root.item, code);
			return;
		}
		if(null!=root.left) {
			createEncoding(root.left,code+"0");
		}
		if(null!=root.right) {
			createEncoding(root.right,code+"1");
		}
	}
	public void writeOutput() {
		int textLength=text.length();
		Object[] freqArray=frequencies.entrySet().toArray();
		Arrays.sort(freqArray,new Comparator() {
			public int compare(Object item1,Object item2) {
				return ((Map.Entry<Character, Integer>) item2).getValue().compareTo(((Map.Entry<Character, Integer>)item1).getValue());
			}
		});
		Object[] codeArray=encodings.entrySet().toArray();
		Arrays.sort(codeArray,new Comparator() {
			public int compare(Object item1,Object item2) {
				int l1=((Map.Entry<Character, String>) item2).getValue().length();
				int l2=((Map.Entry<Character, String>) item1).getValue().length();
				return Integer.compare(l1, l2);
			}
		});
		String finalString=new String();
		int totalBit=0;
		for(int i=0;i<textLength;i++) {
			String code=encodings.get(text.charAt(i));
			totalBit=totalBit+code.length();
			finalString=finalString+code;
		}
		//initialize scanner
		Scanner input=new Scanner(System.in);
		//Input file name from user
  		System.out.println("Enter the output file name: ");  
  		String outfileName=input.next();
  		File f = new File(outfileName);
  		//delete if exists
  		if (f.exists()){
  		   f.delete();
  		}
  		DecimalFormat df = new DecimalFormat("#.####");
  		BufferedWriter bw=null;
        try {
        	bw = new BufferedWriter(new FileWriter(outfileName));
			bw.write("--------------------------");
			bw.newLine();
        	//writing frequency table column headers
	        bw.write("|Symbol  |   Frequency   ");
	        bw.newLine();
	        bw.write("--------------------------");
            bw.newLine();
        	//writing frequency for each character
            for (Object obj: freqArray) {
            	Double d=Double.valueOf(((Map.Entry<String, Integer>)obj).getValue());
            	bw.write("|   "+((Map.Entry<Character,Integer>)obj).getKey()+"    |    "+df.format(d*100.0/Double.valueOf(textLength))+"%");
            	bw.newLine();
            }
            bw.write("--------------------------");
            bw.newLine();
            //writing code table column headers
	        bw.write("|Symbol  |  Huffman Codes ");
	        bw.newLine();
	        bw.write("--------------------------");
            bw.newLine();
            //Print encoding for each character
            for (Object obj: codeArray) {
            	bw.write("|   "+((Map.Entry<Character,Integer>)obj).getKey()+"    |    "+((Map.Entry<String, Integer>)obj).getValue());
            	bw.newLine();
            }
            bw.write("--------------------------");
            bw.newLine();
            //writing the encoded String length
            bw.write("|Total Bits: " + totalBit);
            bw.newLine();
            bw.write("--------------------------");
            
        }catch (IOException e) {
	    	e.printStackTrace();
	    }
        try {
			if (bw!= null)
				bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        //close scanner
        input.close();
	}
	public void huffmanControl() {
		int textLength=text.length();
		calculateFrequency();
		createPriorityQueue();
		getTreeRoot();
		createEncoding(treeRoot,"");
		writeOutput();
	}
}
