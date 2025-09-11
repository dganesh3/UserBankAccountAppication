package com.user.CustomerProfile.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.user.CustomerProfile.Repository.UserBankRepository;
import com.user.CustomerProfile.entity.UserBank;

@Repository
public class UserBankDao 
{
	
	@Autowired
	private UserBankRepository userBankRepository;
	
	public UserBank saveUserBank(UserBank userBank)
	{
		return userBankRepository.save(userBank);
	}
	
	public List<UserBank> getAllUserBank()
	{
		return userBankRepository.findAll();
	}
	
	public Optional<UserBank> getUserBankById(int id)
	{
		return userBankRepository.findById(id);
		
	}
	public void deleteUserbank(int id)
	{
		userBankRepository.deleteById(id);
	}
	
	public UserBank updateUserBank(UserBank userBank)
	{
		return userBankRepository.save(userBank);
	}

}
