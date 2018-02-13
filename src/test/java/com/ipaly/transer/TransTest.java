package com.ipaly.transer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
public class TransTest extends TestCase {
	
	public TransTest(String testName) {
		super(testName);
	}
	
	/**
     * @return the suite of tests being tested
     */
    public static Test suite(){
        return new TestSuite( TransTest.class );
    }

    public void testTrans(){
    	
    	Transer transer = new Transer();
    	
    	From from = new From();
    	
    	from.setTestAlia(12);
    	
    	from.setB("Hello");
    	
    	List<String> strs = new ArrayList<>();
    	strs.add("1");
    	strs.add("2");
    	strs.add("3");
    	from.setC(strs);
    	
    	try {
			To to = transer.trans(from, To.class);
			System.out.println(to);
		} catch (IllegalArgumentException | TransProcessException e) {
			e.printStackTrace();
		}
    	
    }
	
}
