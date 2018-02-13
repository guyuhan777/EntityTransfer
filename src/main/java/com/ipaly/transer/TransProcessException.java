package com.ipaly.transer;

public class TransProcessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8998485573695773762L;
	
	public TransProcessException() {
		super();
	}
	
	public TransProcessException(String reason) {
		super(reason);
	}
}
