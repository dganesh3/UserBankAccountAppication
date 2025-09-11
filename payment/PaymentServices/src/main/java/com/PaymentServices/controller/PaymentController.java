package com.PaymentServices.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PaymentServices.services.PaymentService;
import com.banking.sharedDto.TransactionDto;


@RestController
@RequestMapping("/payments")
//@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

	@Autowired
    private  PaymentService paymentService;

    @PostMapping("/{id}/deposit")
    public Map<String, Object> deposit(@PathVariable Integer id, @RequestBody TransactionDto transactionDto ) {
        return paymentService.deposit(id, transactionDto.getAmount());
    }

    @PostMapping("/{id}/withdraw")
    public Map<String, Object> withdraw(@PathVariable Integer id,@RequestBody TransactionDto transactionDto) {
        return paymentService.withdraw(id, transactionDto.getAmount());
    }

    @PostMapping("/transfer")
    public Map<String, Object> transfer(@RequestBody TransactionDto request) {
        return paymentService.transfer(request.getAccountId(), request.getToAccountId(), request.getAmount());
    }
    @GetMapping("/users/{userId}/accounts")
    public Map<String, Object> getAccount(@PathVariable Integer userId) {
        return paymentService.getAccounts(userId);
    }

    @GetMapping("/accounts")
    public Map<String, Object> getAllAccounts() {
        return paymentService.getAllAccounts();
    }

//    @GetMapping("/users/{userId}/history")
//    public Map<String, Object> getHistory(@PathVariable Integer userId) {
//        return paymentService.getHistory(userId);
//    }
}