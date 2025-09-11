package com.Transactions_History.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
	private Integer accountId;
    private Long accountNumber;
    private String holderName;
    private String bankName;

    private Integer toAccountId;
    private Long toAccountNumber;
    private String toHolderName;
    private String toBankName;

    private BigDecimal amount;
    private String type;
    
    
    private LocalDateTime timestamp;


   
    
    
} 