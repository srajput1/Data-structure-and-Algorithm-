package com.huffman;

public class HuffmanNode implements Comparable<HuffmanNode>{

	public char item;
	public int frequency;
	public HuffmanNode right;
	public HuffmanNode left;
	
	public HuffmanNode(char item,int frequency) {
		this.setItem(item);
		this.setFrequency(frequency);
		right=null;
		left=null;
	}

	public char getItem() {
		return item;
	}

	public void setItem(char item) {
		this.item = item;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public HuffmanNode getRight() {
		return right;
	}

	public void setRight(HuffmanNode right) {
		this.right = right;
	}
	public HuffmanNode getLeft() {
		return left;
	}

	public void setLeft(HuffmanNode left) {
		this.left = left;
	}
	@Override
    public int compareTo(HuffmanNode otherHuffmanNode){
            return Integer.compare(frequency, otherHuffmanNode.getFrequency());
    }
	@Override
    public String toString(){
            return "HuffmanNode [item= " + item + ", frequency=" + frequency + "]";
    }

}
