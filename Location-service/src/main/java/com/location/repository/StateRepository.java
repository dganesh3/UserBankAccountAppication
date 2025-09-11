package com.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.location.entity.State;

public interface StateRepository extends JpaRepository<State, Integer>
{
	
	 List<State> findByCountryId(Integer countryId);
}
