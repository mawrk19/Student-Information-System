package enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseManager;
import javafx.fxml.FXML;
import students.Students;

public class Transaction {

	private int transacID;
	private String MOP;
	private int amountDue;
	private int amount;
	private String late;
	private int total;
	private int balance;
	private int miscTotal;
	private int tuitionTotal;
	private String encoder;
	private String studCode;

	public Transaction(int transacID, String MOP, int amountDue, int amount, String late, int total, int balance,
			int miscTotal, int tuitionTotal, String encoder, String studCode) {
		this.transacID = transacID;
		this.MOP = MOP;
		this.amountDue = amountDue;
		this.amount = amount;
		this.late = late;
		this.total = total;
		this.balance = balance;
		this.miscTotal = miscTotal;
		this.tuitionTotal = tuitionTotal;
		this.encoder = encoder;
		this.studCode = studCode;
	}

	@FXML
	void setTransaction() {
		try (Connection con = DatabaseManager.getConnection();
				PreparedStatement stmt = con.prepareStatement("SELECT * FROM transaction")) {

			try (ResultSet resultSet = stmt.executeQuery()) {
				while (resultSet.next()) {

					// Retrieve values from the result set and set them using setters
					setTransacID(resultSet.getInt("transaction_ID"));
					setMOP(resultSet.getString("payment_mode"));
					setAmountDue(resultSet.getInt("amt_due"));
					setAmount(resultSet.getInt("amount"));
					setLate(resultSet.getString("late"));
					setTotal(resultSet.getInt("total"));
					setBalance(resultSet.getInt("balance"));
					setMiscTotal(resultSet.getInt("misc_total"));
					setTuitionTotal(resultSet.getInt("tuition_total"));
					setEncoder(resultSet.getString("encoder"));
					setStudCode(resultSet.getString("scode"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception as needed
		}
	}

	public int getTransacID() {
		return transacID;
	}

	public void setTransacID(int transacID) {
		this.transacID = transacID;
	}

	public String getMOP() {
		return MOP;
	}

	public void setMOP(String MOP) {
		this.MOP = MOP;
	}

	public int getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(int amountDue) {
		this.amountDue = amountDue;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getLate() {
		return late;
	}

	public void setLate(String late) {
		this.late = late;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getMiscTotal() {
		return miscTotal;
	}

	public void setMiscTotal(int miscTotal) {
		this.miscTotal = miscTotal;
	}

	public int getTuitionTotal() {
		return tuitionTotal;
	}

	public void setTuitionTotal(int tuitionTotal) {
		this.tuitionTotal = tuitionTotal;
	}

	public String getEncoder() {
		return encoder;
	}

	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}

	public String getStudCode() {
		return studCode;
	}

	public void setStudCode(String studCode) {
		this.studCode = studCode;
	}

}
