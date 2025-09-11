package com.user.CustomerProfile.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.user.CustomerProfile.Dto.ResponseStructure;
import com.user.CustomerProfile.entity.*;

public interface UserService {


    User getUserById(int userId);

    ResponseEntity<ResponseStructure<List<User>>> getAllUsers();

	ResponseEntity<ResponseStructure<String>> deleteUserById(int userId);


	public List<UserBankAccount> getAccountsByUserBankId(Integer userBankId);
	String saveOrUpdateUser(User user_UI,User User_DB,String action);
	
	
	public List<User> getByUser(Integer startIndex,Integer lastIndex);
	
	public Integer getByUserCount();
	
	
	
	public UserBankAccount deposit(Integer accountId, BigDecimal amount);
	public UserBankAccount withdraw(Integer accountId, BigDecimal amount);
	public Map<String, UserBankAccount> transfer(Integer fromId, Integer toId, BigDecimal amount);
	public List<UserBankAccount> getAccountsByUser(Integer userBankId);
	public List<UserBankAccount> getAllAccounts();
	
	

}
