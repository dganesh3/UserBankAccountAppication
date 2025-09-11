package com.user.CustomerProfile.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.user.CustomerProfile.Repository.UserBankAccountRepository;
import com.user.CustomerProfile.entity.UserBankAccount;

@Repository
public class UserBankAccountDao 
{
	@Autowired
	private UserBankAccountRepository accountRepository;
	
	public UserBankAccount saveUserBankAccount(UserBankAccount bankAccount)
	{
		return accountRepository.save(bankAccount);
	}
	public List<UserBankAccount> getAllUserBankAccount()
	{
		return accountRepository.findAll();
	}

	public Optional<UserBankAccount> getUserBankAccountById(int id)
	{
		return accountRepository.findById(id);
		
	}
	
	public void deleteUserBankAccount(int id)
	{
		accountRepository.deleteById(id);
	}
	public UserBankAccount updateUserBankAccount(UserBankAccount account)
	{
		return accountRepository.save(account);
	}
}
