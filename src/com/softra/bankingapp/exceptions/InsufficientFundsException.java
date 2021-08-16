package com.softra.bankingapp.exceptions;

public class InsufficientFundsException extends Exception {

	public InsufficientFundsException() {
	}
	
	public InsufficientFundsException(String msg) {
		super(msg);
	}
}
