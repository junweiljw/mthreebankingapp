package com.softra.bankingapp.entities;

import com.softra.bankingapp.exceptions.InsufficientFundsException;
import com.softra.bankingapp.exceptions.InvalidTenureException;

// Task 6,8: Fixed Deposit Class
public class FixedDeposit extends Account {

	// Minimum 50$
	private final double depositAmount = 50;
	// 1 - 5 years
	private int tenure;
	private int age;
	private double interestRate;
	
	public FixedDeposit() {
		
	}
	
	public FixedDeposit(int tenure, double depositAmount, int age) {
		System.out.println("==================================\nCreating new account...");
		// Validation try-catch block
		try {
			determineInterestRate(tenure, age);
			validateBalance(depositAmount, 0);
		} catch (InvalidTenureException e) {
			System.out.println(e.getMessage()+"\nPlease try creating the account again");
		} catch (InsufficientFundsException e) {
			System.out.println(e.getMessage()+"\nPlease try creating the account again");
		}
		this.setAccountNumber(Account.currNumber);
		this.tenure = tenure;
		this.setBalance(depositAmount);
		this.age = age;
		this.setAccountType("Fixed Deposit Account");
		Account.currNumber++;
		System.out.println("Fixed Deposit Account "+this.getAccountNumber()+" with balance: "+this.getBalance()+" successfully created\n==================================");
	}
	
	public FixedDeposit(int accountNumber, double balance, String dateOpened, String accountType) {
		this.setAccountNumber(accountNumber);
		this.setBalance(balance);
		this.setDateOpened(dateOpened);
		this.setAccountNumber(accountNumber);
	}
	
	public int getTenure() {
		return this.tenure;
	}
	
	public int getAge() {
		return this.age;
	}
	
	private void validateBalance(double amount, int validationType) throws InsufficientFundsException {
		switch (validationType) {
		case 0:
			if (amount < 50) {
				throw new InsufficientFundsException("Minimum deposit amount is 50$");
			}
			break;
		case 1:
			if (this.getBalance() < amount) {
				throw new InsufficientFundsException("Withdrawal amount exceeds current balance, please try again");
			}
			break;
		default:
			// Do nothing
		}
	}
	
	@Override
	public boolean withdraw(double withdrawalAmt) throws InsufficientFundsException {
		try {
			validateBalance(withdrawalAmt, 1);
			super.withdraw(withdrawalAmt);
			return true;
		} catch (InsufficientFundsException e) {
			System.out.println(e+": Withdrawal amount exceeds current balance, please try again");
			return false;
		}
	}
	
	private void determineInterestRate(int tenure, int age) throws InvalidTenureException {
		switch (tenure) {
		case 1:		// Fall through
		case 2:
			if (age < 65) {
				this.interestRate = 0.04;
			} else {
				this.interestRate = 0.045;
			}
			break;
		case 3:		// Fall through
		case 4:
			if (age < 65) {
				this.interestRate = 0.05;
			} else {
				this.interestRate = 0.055;
			}
			break;
		case 5:
			if (age < 65) {
				this.interestRate = 0.06;
			} else {
				this.interestRate = 0.065;
			}
			break;
		default:	// Invalid tenure
			throw new InvalidTenureException("Tenure has to be between 1-5 years");
		}
	}
	
	@Override
	public double calculateInterest() {
		return interestRate*this.getBalance();
	}
}
