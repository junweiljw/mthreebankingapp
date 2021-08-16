package com.softra.bankingapp.utilities;

import java.util.Comparator;

import com.softra.bankingapp.entities.Account;

public class AccountComparator implements Comparator<Account> {

	@Override
	public int compare(Account acct1, Account acct2) {
		if (acct1.getAccountNumber() < acct2.getAccountNumber()) {
			return -1;
		} else if (acct1.getAccountNumber() == acct2.getAccountNumber()) {
			return 0;
		} else {
			return 1;
		}
	}
}
