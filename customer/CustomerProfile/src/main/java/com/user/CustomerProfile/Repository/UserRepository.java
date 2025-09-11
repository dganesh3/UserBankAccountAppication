package com.user.CustomerProfile.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.user.CustomerProfile.entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	
	 boolean existsByEmail(String email);
	 
	 
	 @Query("SELECT u FROM User u WHERE u.id BETWEEN :startId AND :endId")
		List<User> findByUsers(@Param("startId") Integer startId,
		                                           @Param("endId") Integer endId);
	
	 
	 
		@Query("SELECT COUNT(u) FROM User u ")
		Integer getByUsersCount();

	 
}
