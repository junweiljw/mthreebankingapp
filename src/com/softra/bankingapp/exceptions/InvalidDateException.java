package com.softra.bankingapp.exceptions;

public class InvalidDateException extends Exception {
	
	public InvalidDateException() {
	}
	
	public InvalidDateException(String msg) {
		super(msg);
	}
}
