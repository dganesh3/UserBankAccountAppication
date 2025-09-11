package com.user.CustomerProfile.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.CustomerProfile.entity.UserBankAccount;

public interface UserBankAccountRepository extends JpaRepository<UserBankAccount, Integer> {
	void deleteByUserBank_User_Id(Integer userId);
	List<UserBankAccount> findByUserBank_Id(Integer userBankId);
	Optional<UserBankAccount> findById(Long id);

}
