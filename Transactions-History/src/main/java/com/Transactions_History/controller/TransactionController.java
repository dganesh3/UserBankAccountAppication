package com.Transactions_History.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Transactions_History.entity.Transaction;
import com.Transactions_History.service.TransactionService;
import com.banking.sharedDto.TransactionDto;

@RestController
@RequestMapping("/transactions")
//@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

	  @Autowired
	    private TransactionService transactionService;

	  @PostMapping("/save")
	    public ResponseEntity<Transaction> saveTransaction(@RequestBody TransactionDto txDto) {
	        Transaction savedTx = transactionService.saveTransaction(txDto);
	        return ResponseEntity.ok(savedTx);
	    }

//	    @GetMapping("/history/{accountId}")
//	    public List<Transaction> getHistory(@PathVariable Integer accountId) {
//	        return transactionService.getTransactionHistory(accountId);
//	    }
//	    
	    
	    @GetMapping("/history")
	    public List<Transaction> getAllHistory(){
	    	return transactionService.getAllTransaction();
	    }
	    
	    @GetMapping("/getAllTransactionHistory/{startIndex}/{lastIndex}")
	    public List<Transaction>  getAllTransactionHistory(@PathVariable Integer startIndex, @PathVariable Integer lastIndex){
	    	return transactionService.getAllTransactionHistory(startIndex, lastIndex);
	    }
	    
	    @GetMapping("/count")
	    public Integer getTransactionCount()
	    {
	    	return transactionService.getByCount();
	    }
}
