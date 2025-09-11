package com.user.CustomerProfile.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.CustomerProfile.Repository.UserBankAccountRepository;
import com.user.CustomerProfile.Service.PaymentService;
import com.user.CustomerProfile.entity.UserBankAccount;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UserBankAccountRepository accountRepository;

	@GetMapping("/users/{accountId}/accounts")
	public ResponseEntity<Map<String, Object>> getAccounts(@PathVariable Integer accountId) {
		List<UserBankAccount> accounts = paymentService.getAccountsByUser(accountId);
		return ResponseEntity.ok(Map.of("data", accounts));
	}

	@GetMapping("/accounts")
	public ResponseEntity<Map<String, Object>> getAllAccounts() {
		List<UserBankAccount> accounts = paymentService.getAllAccounts();
		return ResponseEntity.ok(Map.of("data", accounts));
	}

	@PostMapping("/deposit")
	public ResponseEntity<Map<String, Object>> deposit(@RequestBody Map<String, Object> req) {
		Integer accountId = ((Number) req.get("accountId")).intValue(); // convert Long → Integer
		BigDecimal amount = new BigDecimal(req.get("amount").toString());

		UserBankAccount account = paymentService.deposit(accountId, amount);
		return ResponseEntity.ok(Map.of("message", "Deposit successful", "newBalance", account.getBalance()));
	}

	@PostMapping("/withdraw")
	public ResponseEntity<Map<String, Object>> withdraw(@RequestBody Map<String, Object> req) {
		Integer accountId = ((Number) req.get("accountId")).intValue();
		BigDecimal amount = new BigDecimal(req.get("amount").toString());

		UserBankAccount account = paymentService.withdraw(accountId, amount);
		return ResponseEntity.ok(Map.of("message", "Withdrawal successful", "newBalance", account.getBalance()));
	}

	@PostMapping("/transfer")
	public ResponseEntity<Map<String, Object>> transfer(@RequestBody Map<String, Object> req) {
		Integer fromId = ((Number) req.get("fromAccountId")).intValue();
		Integer toId = ((Number) req.get("toAccountId")).intValue();
		BigDecimal amount = new BigDecimal(req.get("amount").toString());

		Map<String, UserBankAccount> result = paymentService.transfer(fromId, toId, amount);

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
