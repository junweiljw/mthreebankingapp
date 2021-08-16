package com.softra.bankingapp.entities;

import static com.softra.bankingapp.utilities.Utility.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.softra.bankingapp.exceptions.*;

// Task 2: Account Class
public abstract class Account {

	private int accountNumber;
	private double balance;
	private String dateOpened;
	private String accountType;
	
	protected static int currNumber = 1;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	public Account() {
		Date today = new Date();
		this.setDateOpened(df.format(today));
	}
	
	public Account(double balance) {
		this.accountNumber = Account.currNumber;
		this.balance = balance;
		currNumber++;
	}

	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String getDateOpened() {
		return this.dateOpened;
	}
	public void setDateOpened(String dateOpened) {
		try {
			validateDate(dateOpened);
			this.dateOpened = dateOpened;
		} catch (InvalidDateException e) {
			System.out.println(e.getMessage() + "Please re-enter the date of opening");
		}
	}
	
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public boolean withdraw(double withdrawalAmt) throws InsufficientFundsException {
		this.balance -= withdrawalAmt;
		System.out.println(withdrawalAmt+"$ withdrawn from "+this.accountType+" "+this.accountNumber+" "+". Updated balance: "+this.getBalance());
		return true;
	}
	
	public void deposit(double depositAmt) {
		this.balance += depositAmt;
		System.out.println(depositAmt+"$ deposited in "+this.accountType+" "+this.accountNumber+" "+". Updated balance: "+this.balance);
	}
	
	// Task 9: Abstract method to calculate interest
	public abstract double calculateInterest();
	
	@Override
	public String toString() {
		return "========================================\n"
				+"Account No: "+this.accountNumber
				+"\nBalance: "+String.format("%.2f", this.balance)
				+"\nDate Opened: "+this.dateOpened
				+"\nAccount Type: "+this.accountType;
	}
}
