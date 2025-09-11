package com.user.CustomerProfile.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.CustomerProfile.entity.Signup;

public interface SignupReporitory extends JpaRepository<Signup, Integer>
{
	 Signup findByUsernameOrEmail(String username, String email);

	Optional<Signup> findByUsername(String username);
	
	
	Optional<Signup> findByEmail(String email);
    boolean existsByEmail(String email);
}
