package com.PaymentServices.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.PaymentServices.client.UserServiceClient;
import com.banking.sharedDto.TransactionDto;


@Service
public class PaymentService {
	@Autowired
	 private  UserServiceClient userServiceClient;

	@Autowired
    private RestTemplate restTemplate; // to call TransactionService

//    private static final String TRANSACTION_SERVICE_URL = "http://localhost:8082/transactions";

//    private  String transactionServiceUrl = "http://localhost:8082/transactions";
    private  String transactionServiceUrl = "http://localhost:8082/transactions";

    public Map<String, Object> deposit(Integer accountId, BigDecimal amount) {
        Map<String, Object> acc = getAccount(accountId);

        // Update balance in User Service
        BigDecimal currentBalance = new BigDecimal(acc.get("balance").toString());
        BigDecimal updatedBalance = currentBalance.add(amount);
        userServiceClient.updateBalance(accountId, updatedBalance);

        // Save transaction
        TransactionDto tx = buildTransaction(acc, null, null, null, amount, "DEPOSIT");
        TransactionDto saved = restTemplate.postForObject(transactionServiceUrl + "/save", tx, TransactionDto.class);

        return Map.of("status", "success", "transaction", saved);
    }

    // ---------------- WITHDRAW ----------------
    public Map<String, Object> withdraw(Integer accountId, BigDecimal amount) {
        Map<String, Object> acc = getAccount(accountId);

        BigDecimal currentBalance = new BigDecimal(acc.get("balance").toString());
        if (currentBalance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        BigDecimal updatedBalance = currentBalance.subtract(amount);
        userServiceClient.updateBalance(accountId, updatedBalance);

        // Save transaction
        TransactionDto tx = buildTransaction(acc, null, null, null, amount, "WITHDRAW");
        TransactionDto saved = restTemplate.postForObject(transactionServiceUrl + "/save", tx, TransactionDto.class);

        return Map.of("status", "success", "transaction", saved);
    }

    // ---------------- TRANSFER ----------------
    public Map<String, Object> transfer(Integer fromAccId, Integer toAccId, BigDecimal amount) {
        if (fromAccId == null || toAccId == null) {
            throw new IllegalArgumentException("fromAccId or toAccId cannot be null");
        }

        Map<String, Object> fromAcc = getAccount(fromAccId);
        Map<String, Object> toAcc = getAccount(toAccId);

        if (fromAcc == null || toAcc == null) {
            throw new IllegalArgumentException("From or To account not found");
        }

        BigDecimal fromBalance = new BigDecimal(fromAcc.get("balance").toString());
        BigDecimal toBalance = new BigDecimal(toAcc.get("balance").toString());

        if (fromBalance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        userServiceClient.updateBalance(fromAccId, fromBalance.subtract(amount));
        userServiceClient.updateBalance(toAccId, toBalance.add(amount));

        TransactionDto tx = buildTransaction(fromAcc, toAcc, fromAccId, toAccId, amount, "TRANSFER");
        TransactionDto saved = restTemplate.postForObject(transactionServiceUrl + "/save", tx, TransactionDto.class);

        if (saved == null) {
            throw new RuntimeException("Failed to save transaction");
        }

        return Map.of("status", "success", "transaction", saved);
    }


    // ---------------- HELPER ----------------
    private Map<String, Object> getAccount(Integer accountId) {
        Map<String, Object> response = userServiceClient.getAccounts(accountId);
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
        if (data == null || data.isEmpty()) throw new RuntimeException("Account not found: " + accountId);
        return data.get(0);
    }

    private TransactionDto buildTransaction(Map<String, Object> fromAcc, Map<String, Object> toAcc,
                                            Integer fromId, Integer toId,
                                            BigDecimal amount, String type) {
        TransactionDto tx = new TransactionDto();
        tx.setAccountId(fromId != null ? fromId : (Integer) fromAcc.get("id"));
        tx.setAccountNumber(Long.parseLong(fromAcc.get("accountNumber").toString()));
        tx.setHolderName((String) fromAcc.get("accountHolderName"));
        tx.setBankName((String) fromAcc.get("bankName"));

        if (toAcc != null) {
            tx.setToAccountId(toId);
            tx.setToAccountNumber(Long.parseLong(toAcc.get("accountNumber").toString()));
            tx.setToHolderName((String) toAcc.get("accountHolderName"));
            tx.setToBankName((String) toAcc.get("bankName"));
        }

        tx.setAmount(amount);
        tx.setType(type);
        tx.setTimestamp(LocalDateTime.now());
        return tx;
      }
	    public Map<String, Object> getAccounts(Integer userId) {
	        return userServiceClient.getAccounts(userId);
	    }

	    public Map<String, Object> getAllAccounts() {
	        return userServiceClient.getAllAccounts();
	    }


}
