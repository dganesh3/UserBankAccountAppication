package com.Transactions_History.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    private Integer accountId;
	    private Long accountNumber;
	    private String accountHolderName;
	    private String bankName;

	    private Integer toAccountId;
	    private Long toAccountNumber;
	    private String toAccountHolderName;
	    private String toBankName;

	    private BigDecimal amount;
	    private String transactionType;
	    @Column(name = "transaction_date", columnDefinition = "datetime") // plain datetime
	    private LocalDateTime transactionDate;
}
