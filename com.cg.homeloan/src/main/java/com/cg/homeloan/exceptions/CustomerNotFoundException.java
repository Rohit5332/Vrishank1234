package com.cg.homeloan.exceptions;

public class CustomerNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException() {
}

	public CustomerNotFoundException(String message) {
		super(message);
		
	}

}
