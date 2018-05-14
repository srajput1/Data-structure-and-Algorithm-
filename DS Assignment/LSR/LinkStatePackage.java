package com.LSP;

public class LinkStatePackage{
	private String originatingRouterId;
	private int sequenceNumber;
	private int TTL;
	
	public String getOriginatingRouterId() {
		return originatingRouterId;
	}
	public void setOriginatingRouterId(String originatingRouterId) {
		this.originatingRouterId = originatingRouterId;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public int getTTL() {
		return TTL;
	}
	public void setTTL(int tTL) {
		TTL = tTL;
	}
}