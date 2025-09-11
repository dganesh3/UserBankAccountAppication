package com.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.location.entity.District;

public interface DistrictRepository extends JpaRepository<District, Integer>
{
	  List<District> findByStateId(Integer stateId);
}
