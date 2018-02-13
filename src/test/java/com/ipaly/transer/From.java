package com.ipaly.transer;

import java.util.List;

@Trans
public class From {
	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public List<String> getC() {
		return c;
	}

	public void setC(List<String> c) {
		this.c = c;
	}
	
	@FieldAlias(from = "a")
	private int testAlia;
	
	public int getTestAlia() {
		return testAlia;
	}

	public void setTestAlia(int testAlia) {
		this.testAlia = testAlia;
	}

	private String b;
	
	private List<String> c;
}
