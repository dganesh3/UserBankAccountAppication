package com.user.CustomerProfile.Service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.CustomerProfile.Repository.UserBankAccountRepository;
import com.user.CustomerProfile.entity.UserBankAccount;

@Service
public class UserBankAccountService {

	@Autowired
    private  UserBankAccountRepository accountRepository;

    

    public List<UserBankAccount> getAccountsByUserBankId(Integer userBankId) {
        return accountRepository.findByUserBank_Id(userBankId);
    }

    public UserBankAccount getAccountById(Integer accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public void save(UserBankAccount account) {
        accountRepository.save(account);
    }
}