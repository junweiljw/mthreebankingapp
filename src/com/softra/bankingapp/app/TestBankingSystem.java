package com.softra.bankingapp.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import com.softra.bankingapp.entities.*;
import com.softra.bankingapp.exceptions.*;
import com.softra.bankingapp.utilities.AccountComparator;
import com.softra.bankingapp.utilities.CustomerComparator;
import com.softra.bankingapp.utilities.CustomerNameComparator;

// Task 3: Stand-alone desktop app
public class TestBankingSystem {

	private static void printInstances() {
//		Customer[] custArr = new Customer[3];
//		custArr[0] = new Customer("Low Jun Wei", 25, 94518323, "S1234567890I");
//		custArr[1] = new Customer("John Smith", 30, 91302705, "S0987654321Z");
//		// Failing name case
//		custArr[2] = new Customer("$andwich Tan", 12, 90908080, "S5432167890B");
//
//		Account[] acctArr = new Account[3];
//		try {
//			acctArr[0] = new Account(100);
//			acctArr[1] = new SavingsAccount(200);
//			// Failing balance case
//			acctArr[2] = new Account(10);
//		} catch (InsufficientFundException e) {
//			System.out.println(e.getMessage());
//		}
//		
//		acctArr[0].setDateOpened(today);
//		// Failing date case
//		acctArr[1].setDateOpened("13/13/2019");
//		
//		for (Customer c : custArr) {
//			System.out.println(c);
//		}	
//		for (Account a : acctArr) {
//			System.out.println(a);
//		}
	}
	
	// Task 10: Create accounts and perform transactions
	private static void task10Demo() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(new Date());
		
		Customer cust1 = new Customer("Low Jun Wei", 25, 94518323, "S1234567890I");
		Customer cust2 = new Customer("John Smith", 30, 91302705, "S0987654321Z");
		Customer cust3 = new Customer("Sandwich Tan", 65, 90908080, "S5432167890B");
		Account salaryAcct = null;
		Account nonSalaryAcct = null;
		Account fixedDepositAcct = null;
		Account seniorFD = null;
		Account normalFD = null;

		salaryAcct = new SavingsAccount(50, true);
		nonSalaryAcct = new SavingsAccount(200, false);
		fixedDepositAcct = new FixedDeposit(5, 50, 65);
		
		try {
			salaryAcct.withdraw(50);
			salaryAcct.deposit(500);
			nonSalaryAcct.deposit(100);
			nonSalaryAcct.withdraw(200);
			fixedDepositAcct.withdraw(5);
			fixedDepositAcct.deposit(5);
		} catch (InsufficientFundsException e) {
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
		System.out.println("Salary account has interest of: "+salaryAcct.calculateInterest());
		System.out.println("Non-salary account has interest of: "+nonSalaryAcct.calculateInterest());
		System.out.println("Fixed deposit account has interst of: "+fixedDepositAcct.calculateInterest());
		
		for (int i = 1; i<=5; i+=2) {
			seniorFD = new FixedDeposit(i, 50, 65);
			normalFD = new FixedDeposit(i, 50, 25);
			
			System.out.println("Senior FD with "+i+" years tenure and deposit "+seniorFD.getBalance()+" has interest: "+seniorFD.calculateInterest());
			System.out.println("Normal FD with "+i+" years tenure and deposit "+normalFD.getBalance()+" has interest: "+normalFD.calculateInterest());
		}
	}

	// Task 13: Search
	private static void searchCustomerDemo(DatabaseInterface db) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the ID of the Customer to search:");
		int custId = sc.nextInt();
		Customer cust = db.getCustomer(custId);
		if (cust!=null) {
			System.out.println(cust);
		}
	}

	// Task 13: Search
	private static void searchAccountDemo(DatabaseInterface db) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the number of the Account to search:");
		int acctNum = sc.nextInt();
		Account acct = db.getAccount(acctNum);
		if (acct!=null) {
			System.out.println(acct);
		}
	}
	
	private static void task15Demo(DatabaseInterface db) {
		// Task 14: Using collections and comparators to sort
		Comparator cc = new CustomerComparator();
		Comparator cnc = new CustomerNameComparator();
		Comparator ac = new AccountComparator();
		
		Set<Customer> custSet = new TreeSet<>(cc);
		Set<Account> acctSet = new TreeSet<>(ac);
		
		Customer cust1 = new Customer("Low Jun Wei", 25, 94518323, "S1234567890I");
		Customer cust2 = new Customer("John Smith", 30, 91302705, "S0987654321Z");
		
		Account acct1 = new SavingsAccount(50, true);
		Account acct2 = new SavingsAccount(200, false);
		Account acct3 = new FixedDeposit(5, 50, 65);

		custSet.add(cust1);
		custSet.add(cust2);
		acctSet.add(acct1);
		acctSet.add(acct2);
		acctSet.add(acct3);
		
		// Adding using enhanced for-loop
//		for (Customer c : custSet) {
//			db.addCustomer(c);
//		}
//		
//		for (Account a : acctSet) {
//			db.addAccount(a);
//		}
		
		// Adding using list created with Collections 
		db.addAllCustomers(new ArrayList<Customer>(custSet));
		db.addAllAccounts(new ArrayList<Account>(acctSet));
		db.setCustomerAcct(cust1.getCustomerId(), acct1);
		db.setCustomerAcct(cust2.getCustomerId(), acct2);
		db.accountWithdraw(acct1, 50);
	}
	
	public static void main(String[] args) {

		DatabaseInterface db = new DatabaseInterface("./././././config/dbconfig.properties");
//		printInstances();
//		task10Demo();		// Creating instances and validating transactions
//		searchCustomerDemo(db);
//		searchAccountDemo(db);
		task15Demo(db);		// Creating instances with database persistence
//		db.dropAll();
		db.closeAll();
	}
}
