package com.softra.bankingapp.exceptions;

public class CustomerNotFoundException extends Exception {

	public CustomerNotFoundException() {
	}
	
	public CustomerNotFoundException(String msg) {
		super(msg);
	}
}
