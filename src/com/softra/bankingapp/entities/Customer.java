package com.softra.bankingapp.entities;

import static com.softra.bankingapp.utilities.Utility.*;

import com.softra.bankingapp.exceptions.InvalidNameException;


// Task 1: Customer Class
public class Customer {
	private int customerId;
	private String name;
	private int age;
	private int mobileNumber;
	private String aadharNumber;
	// Task 4: Customer HAS-A Account
	private Account acct;
	
	private static int currId = 1;
	
	public Customer() {
	}
	
	public Customer(String name, int age, int mobileNumber, String aadharNumber) {	
		try {
			validateName(name);
			this.customerId = currId;
			this.name = name;
			this.age = age;
			this.mobileNumber = mobileNumber;
			this.aadharNumber = aadharNumber;
			
			System.out.println("New customer "+this.name+" successfully created");
			currId++;
		} catch (InvalidNameException e) {
			System.out.println(e.getMessage() + "\nPlease try creating the profile again");
		}
	}
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		try {
			validateName(name);
			this.name = name;
		} catch (InvalidNameException e) {
			System.out.println(e.getMessage() + "\nPlease set a valid name");
		}		
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public int getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(int mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getAadharNumber() {
		return aadharNumber;
	}
	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	
	public Account getAcct() {
		return this.acct;
	}
	
	public void setAcct(Account acct) {
		this.acct = acct;
	}
	
	@Override
	public String toString() {
		return "========================================\n"
				+"ID: "+this.customerId
				+"\nName: "+this.name
				+"\nAge: "+this.age
				+"\nMobile No: "+this.mobileNumber
				+"\nAadhar No: "+this.aadharNumber;
	}
}
