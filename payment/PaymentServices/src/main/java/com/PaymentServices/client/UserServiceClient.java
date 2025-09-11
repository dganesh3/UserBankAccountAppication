package com.PaymentServices.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("users/deposit")
    Map<String, Object> deposit(@RequestBody Map<String, Object> req);

    @PostMapping("users/withdraw")
    Map<String, Object> withdraw(@RequestBody Map<String, Object> req);

    @PostMapping("users/transfer")
    Map<String, Object> transfer(@RequestBody Map<String, Object> req);

    @GetMapping("users/{accountId}/accounts")
    Map<String, Object> getAccounts(@PathVariable("accountId") Integer userId);

    @GetMapping("users/accounts")
    Map<String, Object> getAllAccounts();

//    @GetMapping("users/{userId}/transactions")
//    Map<String, Object> getHistory(@PathVariable("userId") Integer userId);

    @PutMapping("users/{accountId}/balance")
    void updateBalance(@PathVariable Integer accountId, @RequestParam BigDecimal balance);
}