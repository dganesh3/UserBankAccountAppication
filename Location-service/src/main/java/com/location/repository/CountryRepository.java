package com.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.location.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>
{
	   List<Country> findByNameContainingIgnoreCase(String keyword);
}
