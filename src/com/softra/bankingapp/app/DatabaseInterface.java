package com.softra.bankingapp.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import com.softra.bankingapp.entities.Account;
import com.softra.bankingapp.entities.Customer;
import com.softra.bankingapp.entities.FixedDeposit;
import com.softra.bankingapp.entities.SavingsAccount;
import com.softra.bankingapp.exceptions.AccountNotFoundException;
import com.softra.bankingapp.exceptions.CustomerNotFoundException;
import com.softra.bankingapp.exceptions.InsufficientFundsException;

public class DatabaseInterface {

	// To allow for closeAll() at the end
	private FileInputStream fis = null;
	private Connection con = null;
	
	DatabaseInterface(String propPath) {
		
		try {
			// Task 15: Persistence of data
			Properties prop = new Properties();
			System.out.println("Loading properties from: "+propPath);
			
			// Opening and reading properties file into InputStream
			this.fis = new FileInputStream(propPath);
			if (this.fis != null) {
				prop.load(this.fis);
			} else {
				throw new FileNotFoundException();
			}
			String jdbcDriver = prop.getProperty("driver");
			String databaseUrl = prop.getProperty("databaseUrl");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			
			System.out.println("Driver: "+jdbcDriver);
			System.out.println("Database URL: "+databaseUrl);
			System.out.println("User: "+username);
			
			// Initialize database connection
			Class.forName(jdbcDriver);
			this.con = DriverManager.getConnection(databaseUrl, username, password);
			
			// Create tables
			this.initDatabase();
		} catch (FileNotFoundException e) {
			System.out.println(e+" Properties file not found!");
		} catch (IOException e) {
			System.out.println("Unable to open properties file!");
		} catch (ClassNotFoundException e) {
			System.out.println(e+" Invalid JDBC driver found!");
		} catch (SQLException e) {
			System.out.println(e+" Faulty database URL or non-existent database!");
		}
		
		// this.fis.close() and this.con.close() have to be called by closeAll() method
	}
	
	private void initDatabase() {
		Statement st = null;
		String createAccounts = "CREATE TABLE IF NOT EXISTS Accounts ("
				+ "accountNumber INT PRIMARY KEY,"
				+ "balance FLOAT,"
				+ "dateOpened VARCHAR(15),"
				+ "accountType VARCHAR(30))";
	
		String createCustomers = "CREATE TABLE IF NOT EXISTS Customers ("
				+ "customerId INT PRIMARY KEY,"
				+ "name VARCHAR(50),"
				+ "age INT,"
				+ "mobileNumber INT,"
				+ "aadharNumber VARCHAR(15),"
				+ "accountNumber INT REFERENCES Accounts(accountNumber))";
		
		String createFixedDeposits = "CREATE TABLE IF NOT EXISTS FixedDepositAccts ("
				+ "accountNumber INT PRIMARY KEY REFERENCES Accounts (accountNumber) ON DELETE CASCADE,"
				+ "tenure INT,"
				+ "age INT)";
		
		String createSavingAccts = "CREATE TABLE IF NOT EXISTS SavingsAccts ("
				+ "accountNumber INT PRIMARY KEY,"
				+ "balance FLOAT,"
				+ "dateOpened VARCHAR(15),"
				+ "accountType VARCHAR(20))";
		
		try {
			st = this.con.createStatement();
			st.execute(createAccounts);
			st.execute(createCustomers);
			st.execute(createFixedDeposits);
//			st.execute(createSavingAccts);

			System.out.println("Tables successfully created!");
		} catch (SQLException e) {
			System.out.println("Erronous SQL Query when creating tables");
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				System.out.println(e+": Unable to close Statement!");
			}
		}
	}
	
	private void preparingStatement(String query) {
		try {
			PreparedStatement ps = this.con.prepareStatement(query);
		} catch (SQLException e) {
			System.out.println(e+" Erronous SQL Query: "+query);
		}
	}
	
	// Task 13: Search functionality and exception handling
	public Customer getCustomer(int customerId) {
		String query = "SELECT * FROM Customers WHERE customerId = ?";
		PreparedStatement ps = null;
		Customer cust = null;		
		try {
			ps = this.con.prepareStatement(query);
			ps.setInt(1, customerId);
			if (ps.execute()) {
				ResultSet rs = ps.getResultSet();
				while(rs.next()) {
					System.out.println("========================================\nCustomer found!");
					cust = new Customer();
					cust.setCustomerId(rs.getInt(1));
					cust.setName(rs.getString(2));
					cust.setAge(rs.getInt(3));
					cust.setMobileNumber(rs.getInt(4));
					cust.setAadharNumber(rs.getString(5));
				}
				rs.close();
				ps.close();
			} else {
				throw new CustomerNotFoundException();
			}
			if (cust == null) {
				throw new CustomerNotFoundException();
			}
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		} catch (CustomerNotFoundException e) {
			System.out.println(e+": Customer does not exist in the database!");
		}
		return cust;
	}
	
	// Task 13: Search functionality and exception handling
	public Account getAccount(int accountNumber) {
		String query = "SELECT * FROM Accounts WHERE accountNumber = ?";
		PreparedStatement ps = null;
		Account acct = null;
		try {
			ps = this.con.prepareStatement(query);
			ps.setInt(1, accountNumber);
			if (ps.execute()) {
				ResultSet rs = ps.getResultSet();
				while(rs.next()) {
					System.out.println("========================================\nAccount found!");
					if (rs.getString(4).equals("Fixed Deposit Account")) {
						acct = new FixedDeposit();
						acct.setAccountNumber(rs.getInt(1));
						acct.setBalance(rs.getFloat(2));
						acct.setDateOpened(rs.getString(3));
						acct.setAccountType(rs.getString(4));
					} else {
						acct = new SavingsAccount();
						acct.setAccountNumber(rs.getInt(1));
						acct.setBalance(rs.getFloat(2));
						acct.setDateOpened(rs.getString(3));
						acct.setAccountType(rs.getString(4));
					}
				}
				rs.close();
			} else {
				throw new AccountNotFoundException();
			}
			if (acct == null) {
				throw new AccountNotFoundException();
			}
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		} catch (AccountNotFoundException e) {
			System.out.println(e+": Account does not exist in the database!");
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println("Error closing PreparedStatement!");
			}
		}
		return acct;
	}
	
	public void addCustomer(Customer c) {
		String query = "INSERT INTO Customers (customerId, name, age, mobileNumber, aadharNumber)"
				+ " VALUES (?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = this.con.prepareStatement(query);
			ps.setInt(1, c.getCustomerId());
			ps.setString(2, c.getName());
			ps.setInt(3, c.getAge());
			ps.setInt(4, c.getMobileNumber());
			ps.setString(5, c.getAadharNumber());
			ps.execute();
			System.out.println("========================================\n"+c.getName()+" added to dabatase!");
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println("Error closing PreparedStatement!");
			}
		}
	}
	
	public void setCustomerAcct(int customerId, Account acct) {
		String query = "UPDATE Customers SET accountNumber = ? WHERE customerId = ?";
		PreparedStatement ps = null;
		try {
			ps = this.con.prepareStatement(query);
			ps.setInt(1, acct.getAccountNumber());
			ps.setInt(2, customerId);
			ps.execute();
			System.out.println("========================================\n"+acct.getAccountType()+" linked to Customer "+customerId+"!");
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println("Error closing PreparedStatement!");
			}
		}
	}
	
	public void deleteCustomer(Customer c) {
		String query = "DELETE FROM Customers WHERE customerId = ? AND name = ? AND age = ?"
				+ " AND mobileNumber = ? AND aadharNumber = ?";
		PreparedStatement ps = null;
		try {
			ps = this.con.prepareStatement(query);
			ps.setInt(1, c.getCustomerId());
			ps.setString(2, c.getName());
			ps.setInt(3, c.getAge());
			ps.setInt(4, c.getMobileNumber());
			ps.setString(5, c.getAadharNumber());
			ps.execute();
			System.out.println("========================================\n"+c.getName()+" deleted from dabatase!");
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println("Error closing PreparedStatement!");
			}
		}
	}
	
	public void addAllCustomers(List<Customer> customers) {
		for (Customer c: customers) {
			addCustomer(c);
		}
	}
	
	public void addAccount(Account acct) {
		String query = "INSERT INTO Accounts (accountNumber, balance, dateOpened, accountType)"
							+ " VALUES (?, ?, ?, ?)";
		PreparedStatement ps = null;
		
		try {
			ps = this.con.prepareStatement(query);
			ps.setInt(1, acct.getAccountNumber());
			ps.setFloat(2, (float) acct.getBalance());
			ps.setString(3, acct.getDateOpened());
			ps.setString(4, acct.getAccountType());
			ps.execute();
			
			// Cascade entry into FixedDepositAccts
			if (acct instanceof FixedDeposit) {
				query = "INSERT INTO FixedDepositAccts (accountNumber, tenure, age)"
						+ " VALUES (?, ?, ?)";
				ps = this.con.prepareStatement(query);
				ps.setInt(1, acct.getAccountNumber());
				ps.setInt(2, ((FixedDeposit) acct).getTenure());
				ps.setInt(3, ((FixedDeposit) acct).getAge());
				ps.execute();
			}
			System.out.println("========================================\n"+acct.getAccountType()+" "+
					acct.getAccountNumber()+" added to dabatase!");
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println("Error closing PreparedStatement!");
			}
		}
	}
	
	public void addAllAccounts(List<Account> accounts) {
		for (Account acct: accounts) {
			addAccount(acct);
		}
	}
	
	public void accountWithdraw(Account acct, double amount) {
		String query = "UPDATE Accounts SET BALANCE = ? WHERE accountNumber = ?";
		PreparedStatement ps = null;
		
		try {
			if (acct.withdraw(amount)) {
				ps = this.con.prepareStatement(query);
				ps.setDouble(1, acct.getBalance());
				ps.setInt(2, acct.getAccountNumber());
				ps.execute();
				System.out.printf("========================================\n"
						+"Balance of %s %d updated in database!\n", acct.getAccountType(), acct.getAccountNumber());
			}
		} catch (InsufficientFundsException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		} finally {
			if (ps!=null) {
				try {
					ps.close();
				} catch (SQLException e) {
					System.out.println("Error closing PreparedStatement!");
				}
			}
		}
	}
	
	public void deleteAccount(Account acct) {
		String query = "DELETE FROM Accounts WHERE accountNumber = ? AND dateOpened = ? AND accountType = ?";
		PreparedStatement ps = null;
		try {
			ps = this.con.prepareStatement(query);
			ps.setInt(1, acct.getAccountNumber());
			ps.setString(2, acct.getDateOpened());
			ps.setString(3, acct.getAccountType());
			ps.execute();
			System.out.println("========================================\n"+acct.getAccountType()+" "
									+acct.getAccountNumber()+" deleted from dabatase!");
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println("Error closing PreparedStatement!");
			}
		}
	}
	
	public void dropAll() {
		String query = "DROP TABLE IF EXISTS Accounts, Customers, FixedDepositAccts";
		try {
			Statement st = this.con.createStatement();
			st.execute(query);
			System.out.println("========================================\n"
						+"All tables successfully dropped!");
			st.close();
		} catch (SQLException e) {
			System.out.println(e+": Erronous SQL Query - "+query);
		}
	}
	
	public void closeAll() {
		try {
			this.fis.close();
			this.con.close();
			System.out.println("========================================\nFileInputStream and JDBCConnection closed!");
		} catch (IOException e) {
			// TODO: Add appropriate print
		} catch (SQLException e) {
			// TODO: Add appropriate print
		}
	}

}
