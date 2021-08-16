package com.softra.bankingapp.exceptions;

public class AccountNotFoundException extends Exception {

	public AccountNotFoundException() {
	}
	
	public AccountNotFoundException(String msg) {
		super(msg);
	}
}
