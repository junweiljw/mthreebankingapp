package com.softra.bankingapp.exceptions;

public class InvalidNameException extends Exception {
	
	public InvalidNameException() {
	}
	
	public InvalidNameException(String msg) {
		super(msg);
	}
}
