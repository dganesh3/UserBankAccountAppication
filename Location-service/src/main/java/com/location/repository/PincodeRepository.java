package com.location.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.location.entity.PinCode;

public interface PincodeRepository extends JpaRepository<PinCode, Integer>
{
	Optional<PinCode> findByDistrictId(Integer districtId);
}
