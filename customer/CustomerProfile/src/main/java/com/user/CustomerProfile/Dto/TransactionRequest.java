package com.user.CustomerProfile.Dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionRequest {
	  private Integer userBankId;
	    private Integer accountId;
	    private Integer toAccountId; // only for transfer
	    private BigDecimal amount;
}
