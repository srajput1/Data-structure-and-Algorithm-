package com.trie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {

	private TrieNode root;
	private HashMap<String,Integer> nameMap;
	private HashMap<String,Integer> nameRelevanceMap;
	private int currentPosition;
	private boolean doubleMatch;
	private String doubleMatchName;
	private int articleWordCount;
	
	public Trie() {
		root = new TrieNode(' ');
		nameMap=new HashMap<String,Integer>();
		nameRelevanceMap=new HashMap<String,Integer>();
		currentPosition=0;
		doubleMatch=false;
		doubleMatchName=new String();
		articleWordCount=0;
	}
	//method to add new company name
	public void add(String name,String fName) {
		TrieNode trie=root;
		if(name==null) {
			return;
		}
		char[] characters=name.toCharArray();
		int pos=0;
		insertName(trie,characters,pos,fName);
	}
	//method to recursively store company name to trie
	public void insertName(TrieNode trie, char[] chars, int position, String finName) {
		List<TrieNode> children=trie.getChildren();
		if(position<chars.length) {
			if(trie.childrenMatch(chars[position])) {
				TrieNode tn=trie.getChild(chars[position]);
				position++;
				insertName(tn,chars,position,finName);
			}
			else {
				TrieNode newChild=new TrieNode(chars[position]);
				trie.addChildren(newChild);
				if(position==chars.length-1) {
					newChild.setIsName(true);
					newChild.setFinalName(finName);
				}
				position++;
				trie=newChild;
				insertName(trie,chars,position,finName);
			}
		}
	}
	//function to create trie
    public void createCompanyTrie(){
		try {
			BufferedReader brd=new BufferedReader(new FileReader("company.dat"));
			String inputLine=new String();
			try {
				inputLine=brd.readLine();
				while (inputLine!= null) {
					if(inputLine.contains("\t")) {
						String[] company=inputLine.split("\t");
						String finalName=company[0];
						for(String str: company) {
							add(str,finalName);
						}
					}
					else {
						add(inputLine,inputLine);
					}
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
			System.out.println("No such file exists.");
		}
    }
    //method to search article
    public void searchArticle(String article) {
    	String[] words=article.split("\\s+");
    	articleWordCount=words.length;
    	TrieNode node=root;
    	char[] articleCharSet=article.toCharArray();
    	int articleSetLength=articleCharSet.length;
    	int charPos=0;
    	boolean searchStartFlag=false;
    	while(charPos<articleSetLength) {
    		searchStartFlag=node.childrenMatch(articleCharSet[charPos]);
    		if(searchStartFlag) {
    			searchName(charPos,articleCharSet,node,articleSetLength);
    			int checkPos=charPos;
    			for(int x=currentPosition;x<articleSetLength;x++) {
    				if(articleCharSet[x]==' ') {
    					charPos=x+1;
    					break;
    				}
    			}
    			if(charPos==checkPos) {
    				charPos=articleSetLength;
    			}
    		}
    		else {
    			int checkPos=charPos;
    			for(int x=charPos+1;x<articleSetLength;x++) {
    				if(articleCharSet[x]==' ') {
    					charPos=x+1;
    					break;
    				}
    			}
    			if(charPos==checkPos) {
    				charPos=articleSetLength;
    			}
    		}
    	}
    	showOutput();
    }
    //method to search for company name
    public void searchName(int pos,char[] charSet,TrieNode node,int length) {
    	try {
    		//if next character matches
        	if(node.childrenMatch(charSet[pos])) {
        		node=node.getChild(charSet[pos]);
        		//if it is a matching name
        		if(node.isName()) {
        			if(pos==length) {
        				currentPosition=length;
        			}
        			//if trie has children for this node
        			if(node.getChildren().size()>0) {
        				int nextState=node.getChildState(charSet[++pos]);
        				if(nextState==1) {
        					TrieNode next=node.getChild(charSet[pos]);
        					if(next.getValue()==' ') {
                				int tn=next.getChildState(charSet[++pos]);
                				if(tn==0) {
                					String comName=node.getFinalName();
                    				if(nameMap.containsKey(comName)) {
                    					int val=nameMap.get(comName);
                    					val++;
                    					nameMap.put(comName, val);
                    				}
                    				else {
                    					nameMap.put(comName,1);
                    				}
                    				pos--;
                    				currentPosition=pos;
                				}
                				else {
                					pos--;
                					doubleMatch=true;
                					doubleMatchName=node.getFinalName();
                					searchName(pos,charSet,node,length);
                				}
                			}
                			else if(Character.isAlphabetic(next.getValue())) {
                				currentPosition=pos;
                			}
                			else {
                				if(doubleMatch==true) doubleMatch=false;
                				currentPosition=pos;
                			}
        				}
        				else {
        					
        					String comName=node.getFinalName();
            				if(nameMap.containsKey(comName)) {
            					int val=nameMap.get(comName);
            					val++;
            					nameMap.put(comName, val);
            				}
            				else {
            					nameMap.put(comName,1);
            				}
            				currentPosition=pos;
        				}
        			}
        			//end of trie branch 
        			else {
        				if(doubleMatch==true) doubleMatch=false;
        				String comName=node.getFinalName();
        				if(nameMap.containsKey(comName)) {
        					int val=nameMap.get(comName);
        					val++;
        					nameMap.put(comName, val);
        				}
        				else {
        					nameMap.put(comName,1);
        				}
        				currentPosition=pos;
        			}
        			
        		}
        		else {
        			if(length==pos) {
        				currentPosition=length;
        			}
        			pos++;
        			searchName(pos,charSet,node,length);
        		}
        	}
        	else {
        		if(doubleMatch==true) {
        			if(nameMap.containsKey(doubleMatchName)) {
    					int val=nameMap.get(doubleMatchName);
    					val++;
    					nameMap.put(doubleMatchName, val);
    				}
    				else {
    					nameMap.put(doubleMatchName,1);
    				}
        			doubleMatch=false;
        		}
        		currentPosition=pos;
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    //method to write output to file
    public void showOutput() {
    	Object[] nameArray=nameMap.entrySet().toArray();
		Arrays.sort(nameArray,new Comparator() {
			public int compare(Object item1,Object item2) {
				int l1=((Map.Entry<String, Integer>) item2).getValue();
				int l2=((Map.Entry<String, Integer>) item1).getValue();
				return Integer.compare(l1, l2);
			}
		});
		DecimalFormat df = new DecimalFormat("#.####");
		int totalHit=0;
		double totalRel=0.0;
		System.out.printf("%-42s\n","--------------------------------------------");
		System.out.printf("|%-20s|%-10s|%-10s| \n","Company","Hit Count","Relevance");
		System.out.printf("|%-42s|\n","------------------------------------------");
		for (Object obj: nameArray) {
			String key=((Map.Entry<String,Integer>)obj).getKey();
			int value=((Map.Entry<String, Integer>)obj).getValue();
			totalHit+=value;
        	Double d=Double.valueOf(((Map.Entry<String, Integer>)obj).getValue());
        	Double rel=Double.valueOf(df.format(d*100.0/Double.valueOf(articleWordCount)));
        	totalRel+=rel;
        	//System.out.println("|"+key+"|"+value+"|"+rel+"%\t|");
        	System.out.printf("|%-20s|%-10s|%-10s| \n",key,value,rel+"%");
        	System.out.printf("|%-42s|\n","------------------------------------------");
        }
		System.out.printf("|%-20s|%-10s|%-10s| \n","Total",totalHit,totalRel+"%");
		System.out.printf("|%-42s|\n","------------------------------------------");
		System.out.printf("|%-20s|%-21s|\n","Total Words",articleWordCount,"");
		System.out.printf("%-42s\n","--------------------------------------------");
    }
}
