package com.user.CustomerProfile.Controller;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.user.CustomerProfile.Dto.ResponseStructure;
import com.user.CustomerProfile.Repository.UserBankAccountRepository;
import com.user.CustomerProfile.Service.UserService;
import com.user.CustomerProfile.entity.User;
import com.user.CustomerProfile.entity.UserBankAccount;


//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
    private UserService userService;
	
	@Autowired
	 private UserBankAccountRepository accountRepository;


    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure> getUserById(@PathVariable int id) {
    	User userById = userService.getUserById(id);
    	ResponseStructure<User> responseStructure = new ResponseStructure<User>();
    	responseStructure.setData(userById);
        return ResponseEntity.ok(responseStructure);
    }

    
//    @GetMapping("/{userBankId}/accounts")
//    public List<UserBankAccount> getAccounts(@PathVariable Integer userBankId) {
//        return userService.getAccountsByUserBankId(userBankId);
//    }
//   
    @GetMapping("/all")
    public ResponseEntity<ResponseStructure<List<User>>> getAllUsers() {
        return userService.getAllUsers();
    }

   
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteUser(@PathVariable int id) {
        return userService.deleteUserById(id);
    }
    
    @GetMapping("/getByUsers/{startIndex}/{lastIndex}")
    public List<User> getByUsers(@PathVariable Integer startIndex,@PathVariable Integer lastIndex){
    	return userService.getByUser(startIndex, lastIndex);
    }
    
    
    @GetMapping("/getByUserCount")
    public Integer getByUserCount()
    {
    	return userService.getByUserCount();
    }
    

    
    @PostMapping("/saveOrUpdateUser")
    public ResponseEntity<String> saveOrUpdateUser(
            @RequestBody User user_UI,
            @RequestParam("action") String action) {

        try {
            User user_DB = null;

            if ("edit".equalsIgnoreCase(action)) {
                user_DB = userService.getUserById(user_UI.getId());
                if (user_DB == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("User not found with ID: " + user_UI.getId());
                }
            }

            String result = userService.saveOrUpdateUser(user_UI, user_DB, action);
            HttpStatus status = action.equalsIgnoreCase("create") ? HttpStatus.CREATED : HttpStatus.OK;

            return ResponseEntity.status(status).body(result);

        } catch (Exception e) {
            e.printStackTrace(); // ❗ In real scenarios, use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to " + action + " user: " + e.getMessage());
        }
    }
    
    
    
    
    
    @GetMapping("/{accountId}/accounts")
	public ResponseEntity<Map<String, Object>> getAccount(@PathVariable Integer accountId) {
		List<UserBankAccount> accounts = userService.getAccountsByUser(accountId);
		return ResponseEntity.ok(Map.of("data", accounts));
	}

	@GetMapping("/accounts")
	public ResponseEntity<Map<String, Object>> getAllAccounts() {
		List<UserBankAccount> accounts = userService.getAllAccounts();
		return ResponseEntity.ok(Map.of("data", accounts));
	}

	@PostMapping("/deposit")
	public ResponseEntity<Map<String, Object>> deposit(@RequestBody Map<String, Object> req) {
		Integer accountId = ((Number) req.get("accountId")).intValue(); // convert Long → Integer
		BigDecimal amount = new BigDecimal(req.get("amount").toString());

		UserBankAccount account = userService.deposit(accountId, amount);
		return ResponseEntity.ok(Map.of("message", "Deposit successful", "newBalance", account.getBalance()));
	}

	@PostMapping("/withdraw")
	public ResponseEntity<Map<String, Object>> withdraw(@RequestBody Map<String, Object> req) {
		Integer accountId = ((Number) req.get("accountId")).intValue();
		BigDecimal amount = new BigDecimal(req.get("amount").toString());

		UserBankAccount account = userService.withdraw(accountId, amount);
		return ResponseEntity.ok(Map.of("message", "Withdrawal successful", "newBalance", account.getBalance()));
	}

	@PostMapping("/transfer")
	public ResponseEntity<Map<String, Object>> transfer(@RequestBody Map<String, Object> req) {
		Integer fromId = ((Number) req.get("fromAccountId")).intValue();
		Integer toId = ((Number) req.get("toAccountId")).intValue();
		BigDecimal amount = new BigDecimal(req.get("amount").toString());

		Map<String, UserBankAccount> result = userService.transfer(fromId, toId, amount);

		return ResponseEntity.ok(Map.of("message", "Transfer successful", "fromNewBalance",
				result.get("from").getBalance(), "toNewBalance", result.get("to").getBalance()));
	}

//	@GetMapping("/users/{userId}/transactions")
//	public ResponseEntity<Map<String, Object>> getHistory(@PathVariable Integer userId) {
//		List<PaymentTransaction> history = paymentService.getHistory(userId);
//		return ResponseEntity.ok(Map.of("data", history));
//	}

	@PutMapping("/{accountId}/balance")
	public ResponseEntity<Void> updateBalance(@PathVariable Integer accountId, @RequestParam BigDecimal balance) {
		UserBankAccount account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found"));
		account.setBalance(balance);
		accountRepository.save(account);
		return ResponseEntity.ok().build();
	}
    
    

}
