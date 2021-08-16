package com.softra.bankingapp.utilities;

import com.softra.bankingapp.exceptions.InvalidDateException;
import com.softra.bankingapp.exceptions.InvalidNameException;

// Task 5: Utility validation Class
public class Utility {
	
	public static void validateName(String name) throws InvalidNameException {
		char ch;
		// Removing whitespaces that will otherwise throw Exception
		name = name.replaceAll("\\s+","");
		for (int i = 0; i < name.length(); i++) {
			ch = name.charAt(i);
			// Using character ASCII comparisons
			if (ch < 'A' || (ch > 'Z' && ch < 'a') || ch > 'z') {
				throw new InvalidNameException("Invalid character detected, please do not include special characters or numbers in your name");
			}
		}
	}
	
	public static void validateDate(String date) throws InvalidDateException {
		String[] dateSplit = date.split("/");
		int day = Integer.parseInt(dateSplit[0]);
		int month = Integer.parseInt(dateSplit[1]);
		int year = Integer.parseInt(dateSplit[2]);
		
		// @TODO: Further validation of specific days per month
		// Simple validation of days/months/years
		if (day < 1 || day > 31) {
			throw new InvalidDateException("Please enter a day between 1-31\n");
		} else if (month < 1 || month > 12) {
			throw new InvalidDateException("Please enter a month between 1-12\n");
		} else if (year < 1000 || year > 9999) {
			throw new InvalidDateException("Please enter the current year\n");
		}
		// do nothing
	}
}
