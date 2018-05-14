package com.trie;

import java.util.Scanner;

public class TrieArticles {

	
    //main method
	public static void main(String[] args) {
		String submittedString=new String();
		String line="";
		Scanner stdin=new Scanner(System.in);
		
		Trie newTrie = new Trie();
		//Input file name from user
  		System.out.println("Enter the article: ");  
		while (stdin.hasNextLine() && !(line=stdin.nextLine()).equals(".")){
			submittedString+= line;
			if(line.length()>2){
				String checkPeriod=line.substring(line.length()-2);
				if(checkPeriod.equals("..")) {
					break;
				}
			}
		}
		newTrie.createCompanyTrie();
		newTrie.searchArticle(submittedString);
		//closing standard input
		stdin.close();
	}
}

