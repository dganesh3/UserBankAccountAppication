package com.user.CustomerProfile.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.CustomerProfile.Repository.UserBankAccountRepository;
import com.user.CustomerProfile.entity.UserBankAccount;

@Service
public class PaymentService {

	@Autowired
    private UserBankAccountRepository accountRepository;

    

    public UserBankAccount deposit(Integer accountId, BigDecimal amount) {
        UserBankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() == null) account.setBalance(BigDecimal.ZERO);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        return account;
    }

    public UserBankAccount withdraw(Integer accountId, BigDecimal amount) {
        UserBankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() == null) account.setBalance(BigDecimal.ZERO);
        if (account.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        return account;
    }

    public Map<String, UserBankAccount> transfer(Integer fromId, Integer toId, BigDecimal amount) {
        UserBankAccount from = accountRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("From Account not found"));
        UserBankAccount to = accountRepository.findById(toId)
                .orElseThrow(() -> new RuntimeException("To Account not found"));

        if (from.getBalance() == null) from.setBalance(BigDecimal.ZERO);
        if (to.getBalance() == null) to.setBalance(BigDecimal.ZERO);
        if (from.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accountRepository.save(from);
        accountRepository.save(to);

        

        Map<String, UserBankAccount> result = new HashMap<>();
        result.put("from", from);
        result.put("to", to);
        return result;
    }

   

//    public List<PaymentTransaction> getHistory(Integer userId) {
//        return transactionRepository.findByUserId(userId);
//    }

    public List<UserBankAccount> getAccountsByUser(Integer userBankId) {
        return accountRepository.findByUserBank_Id(userBankId);
    }
    
    public List<UserBankAccount> getAllAccounts()
    {
    	return accountRepository.findAll();
    }
}

