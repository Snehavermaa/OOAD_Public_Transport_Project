package com.transport.transport_system.repository;

import com.transport.transport_system.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {

    private final DataSource dataSource;

    @Autowired
    public TransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        initTable();
    }

    private void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "user_id INT, " +
                "booking_id INT, " +
                "amount DOUBLE, " +
                "payment_method VARCHAR(50), " +
                "status VARCHAR(20), " +
                "timestamp DATETIME" +
                ")";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Transaction table verified using JDBC.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error initializing Transaction table: " + e.getMessage());
        }
    }

    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, booking_id, amount, payment_method, status, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setObject(1, transaction.getUserId());
            pstmt.setObject(2, transaction.getBookingId());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getPaymentMethod());
            pstmt.setString(5, transaction.getStatus());
            pstmt.setTimestamp(6, new java.sql.Timestamp(transaction.getTimestamp().getTime()));

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> findByUserId(Long userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY timestamp DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(amount) FROM transactions WHERE status = 'SUCCESS'";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getLong("transaction_id"));
        transaction.setUserId(rs.getObject("user_id", Long.class));
        transaction.setBookingId(rs.getObject("booking_id", Long.class));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setPaymentMethod(rs.getString("payment_method"));
        transaction.setStatus(rs.getString("status"));
        transaction.setTimestamp(rs.getTimestamp("timestamp"));
        return transaction;
    }
}
