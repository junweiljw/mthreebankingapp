package com.softra.bankingapp.utilities;

import java.util.Comparator;

import com.softra.bankingapp.entities.Customer;

public class CustomerNameComparator implements Comparator<Customer> {

	@Override
	public int compare(Customer cust1, Customer cust2) {
		return cust1.getName().compareToIgnoreCase(cust2.getName());
	}
}
