package com.user.CustomerProfile.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.CustomerProfile.entity.UserBank;

public interface UserBankRepository extends JpaRepository<UserBank, Integer> {
	void deleteByUser_Id(Integer userId);

}
