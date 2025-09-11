package com.user.CustomerProfile.Service.impl;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.CustomerProfile.Dto.ResponseStructure;
import com.user.CustomerProfile.Repository.ContactRepository;
import com.user.CustomerProfile.Repository.UserBankAccountRepository;
import com.user.CustomerProfile.Repository.UserBankRepository;
import com.user.CustomerProfile.Repository.UserRepository;
import com.user.CustomerProfile.Service.UserService;
import com.user.CustomerProfile.entity.Contact;
import com.user.CustomerProfile.entity.User;
import com.user.CustomerProfile.entity.UserBank;
import com.user.CustomerProfile.entity.UserBankAccount;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserBankRepository userBankRepository;
	@Autowired
	private UserBankAccountRepository accountRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private FileStorageService fileStorageService;
	
	
	
	
	public List<UserBankAccount> getAccountsByUserBankId(Integer userBankId) {
        return accountRepository.findByUserBank_Id(userBankId);
    }

	// GET BY ID
	@Override
	public User getUserById(int userId) {
		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {
			ResponseStructure<User> notFound = new ResponseStructure<>();
			notFound.setStatusCode(HttpStatus.NOT_FOUND.value());
			notFound.setMessage("User not found");
			notFound.setData(null);
			return optionalUser.orElse(null);
		}

		ResponseStructure<User> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("User data fetched successfully");
		structure.setData(optionalUser.get());
		return optionalUser.orElse(null);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<User>>> getAllUsers() {
		List<User> users = userRepository.findAll();

		ResponseStructure<List<User>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("All user profiles fetched successfully");
		structure.setData(users);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// DELETE
	@Override
	@Transactional
	public ResponseEntity<ResponseStructure<String>> deleteUserById(int userId) {
		Optional<User> optionalUser = userRepository.findById(userId);

		ResponseStructure<String> response = new ResponseStructure<>();

		if (optionalUser.isEmpty()) {
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("User not found");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		User user = optionalUser.get();

		// Remove associated profile image using service (decoupled logic)
		fileStorageService.deleteFileIfExists(user.getProfileImage());

		// Cascade delete: User → Banks → Accounts (handled by JPA annotations)
		userRepository.delete(user);

		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("User deleted successfully");
		response.setData("User ID " + userId + " removed from system.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//    @Transactional 
//	@Override
//	public String saveOrUpdateUser(User user_UI, User user_DB, String action) {
//		
//		if ("create".equals(action)) {
//			
//			userRepository.save(user_UI);
//			
//			List<UserBank> banks_UI = user_UI.getBanks();
//			
//			banks_UI.forEach(x -> {
//				userBankRepository.save(x);
//				
//				List<UserBankAccount> accounts_UI = x.getAccounts();
//				
//				accounts_UI.forEach(y -> {
//					
//					y.setUserBank(x);
//					
////					accountRepository.save(y);
//				});
//				
//			});
//		
//		
//		
//		} else if("edit".equals(action)) {
//			
//			List<UserBank> banks_UI = user_UI.getBanks();
//			List<UserBank> banks_DB = user_DB.getBanks();
//			
//			userRepository.save(user_UI);
//			
//			user_DB.getBanks().stream();
//			
//			banks_DB.forEach(x -> {
//				if (!banks_UI.contains(x)) {
//					userBankRepository.delete(x);
//				}
//			});
//			
//			banks_UI.forEach(x -> {
//				x.setUser(user_UI);
//				userBankRepository.save(x);
//			});
//
//		}
//		return null;
//	}

//	@Transactional
//	@Override
//	public String saveOrUpdateUser(User user_UI, User user_DB, String action) {
//
//	    if ("create".equalsIgnoreCase(action)) {
//	        return userRepository.save(user_UI) != null ? "User created successfully" : "User not created";
//	    }
//
//	    else if ("edit".equalsIgnoreCase(action)) {
//	        if (user_DB == null) {
//	            throw new IllegalArgumentException("User not found for update.");
//	        }
//
//	        // Only need to update the ID (Hibernate uses the existing persistent entity)
//	        user_UI.setId(user_DB.getId());
//
//	        // Hibernate will compare based on equals/hashCode and perform dirty checking
//	        return userRepository.save(user_UI) != null ? "User updated successfully" : "User update failed";
//	    }
//
//	    throw new IllegalArgumentException("Invalid action: " + action);
//	}

	@Transactional
	@Override
	public String saveOrUpdateUser(User user_UI, User user_DB, String action) {

		if ("create".equalsIgnoreCase(action)) {
			User savedUser = userRepository.save(user_UI);

			if (user_UI.getBanks() != null) {
				for (UserBank bank : user_UI.getBanks()) {
					if (Boolean.TRUE.equals(bank.getDeleted()))
						continue;

					bank.setUser(savedUser);
					UserBank savedBank = userBankRepository.save(bank);

					if (bank.getAccounts() != null) {
						for (UserBankAccount account : bank.getAccounts()) {
							if (Boolean.TRUE.equals(account.getDeleted()))
								continue;

							account.setUserBank(savedBank);
						}
						accountRepository.saveAll(
								bank.getAccounts().stream().filter(a -> !Boolean.TRUE.equals(a.getDeleted())).toList());
					}
				}
				if (user_UI.getContacts() != null) {
					for (Contact contact : user_UI.getContacts()) {
						if (Boolean.TRUE.equals(contact.getDeleted()))
							continue;
						contact.setUserContacts(savedUser);
						contactRepository.save(contact);
					}
				}

			}
			return "User created successfully";
		}

		else if ("edit".equalsIgnoreCase(action)) {
			if (user_DB == null) {
				throw new IllegalArgumentException("User not found.");
			}

			user_UI.setId(user_DB.getId());
			User updatedUser = userRepository.save(user_UI);

			if (user_UI.getBanks() != null) {
				for (UserBank incomingBank : user_UI.getBanks()) {

					// Delete Bank
					if (Boolean.TRUE.equals(incomingBank.getDeleted()) && incomingBank.getId() != null) {
						userBankRepository.deleteById(incomingBank.getId());
						continue;
					}

					incomingBank.setUser(updatedUser);
					UserBank savedBank = userBankRepository.save(incomingBank);

					if (incomingBank.getAccounts() != null) {
						for (UserBankAccount account : incomingBank.getAccounts()) {

							// Delete Account
							if (Boolean.TRUE.equals(account.getDeleted()) && account.getId() != null) {
								accountRepository.deleteById(account.getId());
								continue;
							}

							account.setUserBank(savedBank);
							accountRepository.save(account);
						}
					}
				}

				if (user_UI.getContacts() != null) {
					for (Contact incomingContact : user_UI.getContacts()) {
						if (Boolean.TRUE.equals(incomingContact.getDeleted()) && incomingContact.getId() != null) {
							contactRepository.deleteById(incomingContact.getId());
							continue;
						}
						incomingContact.setUserContacts(updatedUser);
						contactRepository.save(incomingContact);
					}
				}
			}

			return "User updated successfully";
		}

		throw new IllegalArgumentException("Invalid action: " + action);
	}

	@Override
	public List<User> getByUser(Integer startIndex, Integer lastIndex) {
		return userRepository.findByUsers(startIndex, lastIndex);
	}

	@Override
	public Integer getByUserCount() {
		return userRepository.getByUsersCount();
	}
	
	
	
	
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
