package com.Transactions_History.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Transactions_History.entity.Transaction;

import feign.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
//    List<Transaction> findByAccountIdOrFromAccountIdOrToAccountId(Integer accountId, Integer fromId, Integer toId);
	
	@Query("SELECT t FROM Transaction t WHERE t.id BETWEEN :startId AND :endId")
	List<Transaction> findByTransactionHistory(@Param("startId") Integer startId,
	                                           @Param("endId") Integer endId);
	
	@Query("SELECT COUNT(t) FROM Transaction t ")
	Integer getByCount();

}