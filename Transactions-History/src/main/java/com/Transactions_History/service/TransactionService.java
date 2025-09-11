package com.Transactions_History.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Transactions_History.entity.Transaction;
import com.Transactions_History.repository.TransactionRepository;
import com.banking.sharedDto.TransactionDto;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
	@Autowired
    private  TransactionRepository transactionRepository;
	@Transactional
    public Transaction saveTransaction(TransactionDto txDto) {
        Transaction tx = new Transaction();

        // Mapping DTO → Entity
        
        tx.setAccountId(txDto.getAccountId());
        tx.setAccountNumber(txDto.getAccountNumber());
        tx.setAccountHolderName(txDto.getHolderName());
        tx.setBankName(txDto.getBankName());

        tx.setToAccountId(txDto.getToAccountId());
        tx.setToAccountNumber(txDto.getToAccountNumber());
        tx.setToAccountHolderName(txDto.getToHolderName());
        tx.setToBankName(txDto.getToBankName());

        tx.setAmount(txDto.getAmount());
        tx.setTransactionType(txDto.getType());
        tx.setTransactionDate(txDto.getTimestamp());

        return transactionRepository.save(tx);
    }

//    public List<Transaction> getTransactionHistory(Integer accountId) {
//        return repository.findByAccountIdOrFromAccountIdOrToAccountId(accountId, accountId, accountId);
//    }
//    
    public List<Transaction> getAllTransaction()
    {
    	return transactionRepository.findAll();
    }
    
    
    public List<Transaction> getAllTransactionHistory(Integer startIndex,Integer lastIndex){
    	return transactionRepository.findByTransactionHistory(startIndex, lastIndex);
    }
    
    
    public Integer getByCount()
    {
    	return transactionRepository.getByCount();
    }
}