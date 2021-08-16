package com.softra.bankingapp.utilities;

import java.util.Comparator;

import com.softra.bankingapp.entities.Customer;

public class CustomerComparator implements Comparator<Customer> {

	@Override
	public int compare(Customer cust1, Customer cust2) {
		if (cust1.getCustomerId() < cust2.getCustomerId()) {
			return -1;
		} else if (cust1.getCustomerId() == cust2.getCustomerId()) {
			return 0;
		} else {
			return 1;
		}
	}
}
