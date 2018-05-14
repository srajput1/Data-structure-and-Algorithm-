package com.trie;

import java.util.ArrayList;
import java.util.List;

public class TrieNode {

	private char value;
	private List<TrieNode> children;
	private boolean isName;
	private String finalName;
	
	public TrieNode(char val) {
		this.value=val;
		this.children= new ArrayList<TrieNode>();
		this.isName=false;
		this.finalName=new String();
	}
	
	public List<TrieNode> getChildren(){
		return children;
	}
	
	public char getValue() {
        return value;
    }
	
	public boolean isName() {
		return isName;
	}
	
	public void setIsName(boolean val) {
		isName=val;
	}
	public void addChildren(TrieNode newNode) {
		children.add(newNode);
	}
	public boolean childrenMatch(char item) {
		for(TrieNode tn: children) {
			if(tn.getValue()==item)
				return true;
		}
		return false;
	}
	public TrieNode getChild(char item) {
		for(TrieNode tn: children) {
			if(tn.getValue()==item)
				return tn;
		}
		return null;
	}
	public int getChildState(char item) {
		for(TrieNode tn: children) {
			if(tn.getValue()==item)
				return 1;
		}
		return 0;
	}
	public String getFinalName() {
		return finalName;
	}

	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}
}
