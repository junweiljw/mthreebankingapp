package com.softra.bankingapp.entities;

import com.softra.bankingapp.exceptions.InsufficientFundsException;
import com.softra.bankingapp.interfaces.CurrencyConvertor;

// Task 6,7: Savings Account Class
public class SavingsAccount extends Account implements CurrencyConvertor {
	
	private boolean isSalaryAccount = false;
	private final double minBalance = 100;
	
	public SavingsAccount() {
		
	}
	
	public SavingsAccount(double balance, boolean isSalaryAccount) {
		System.out.println("==================================\nCreating new account...");
		if (!isSalaryAccount) {
			try {
				validateBalance(balance, 0);
				this.setAccountType("Non-Salary Account");
			} catch (InsufficientFundsException e) {
				System.out.println(e+": Insufficient Balance for account creation. Minimum balance is Rs.100");
			}
		} else {
			this.setAccountType("Salary Account");
		}
		this.setAccountNumber(Account.currNumber);
		this.setBalance(balance);
		this.isSalaryAccount = isSalaryAccount;
		Account.currNumber++;
		System.out.println("Salary Account "+this.getAccountNumber()+" with balance: "+this.getBalance()+" successfully created\n==================================");					
	}
	
	public SavingsAccount(int accountNumber, double balance, String dateOpened, String accountType) {
		this.setAccountNumber(accountNumber);
		this.setBalance(balance);
		this.setDateOpened(dateOpened);
		this.setAccountType(accountType);
	}
	
	private void validateBalance(double amount, int validationType) throws InsufficientFundsException {
		switch (validationType) {
		case 0:
			if (amount < this.minBalance) {
				throw new InsufficientFundsException();
			}
			break;
		case 1:
			// Withdrawal amount has to be less than current balance - 100$
			if ((this.getBalance() - this.minBalance) < amount) {
				throw new InsufficientFundsException();
			}
			break;
		case 2:
			if (this.getBalance() < amount) {
				throw new InsufficientFundsException();
			}
			break;
		default:
			// Do nothing
		}			
	}
	
	@Override
	public boolean withdraw(double withdrawalAmt) {
		try {
			if (!this.isSalaryAccount) {
				validateBalance(withdrawalAmt, 1);
			} else {
				validateBalance(withdrawalAmt, 2);
			}
			super.withdraw(withdrawalAmt);
			return true;
		} catch (InsufficientFundsException e) {
			System.out.println(e+": Withdrawal amount exceeds current balance, please try again");
			return false;
		}
	}
	
	@Override
	public double calculateInterest() {
		return 0.04*this.getBalance();
	}

	@Override
	public double convertUSDToAUD(double USD) {
		double conversionRate = 1.35;
		double conversionFee = checkConversionFee();
		
		return USD*conversionRate*(1-conversionFee);
	}

	@Override
	public double convertAUDToUSD(double AUD) {
		double conversionRate = 0.74;
		double conversionFee = checkConversionFee();
		
		return AUD*conversionRate*(1-conversionFee);
	}

	@Override
	public double convertUSDToSGD(double USD) {
		double conversionRate = 1.35;
		double conversionFee = checkConversionFee();
		
		return USD*conversionRate*(1-conversionFee);
	}

	@Override
	public double convertSGDToUSD(double SGD) {
		double conversionRate = 0.74;
		double conversionFee = checkConversionFee();
		
		return SGD*conversionRate*(1-conversionFee);
	}
	
	protected double checkConversionFee() {
		if (this.isSalaryAccount) {
			return 0.01;
		}
		return 0.02;
	}
}
