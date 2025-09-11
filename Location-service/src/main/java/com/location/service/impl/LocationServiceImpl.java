package com.location.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.location.dto.CountryDto;
import com.location.dto.CountryListDto;
import com.location.dto.StateDto;
import com.location.dto.StateListDto;
import com.location.entity.Country;
import com.location.entity.District;
import com.location.entity.PinCode;
import com.location.entity.State;
import com.location.repository.CountryRepository;
import com.location.repository.DistrictRepository;
import com.location.repository.PincodeRepository;
import com.location.repository.StateRepository;
import com.location.service.LocationService;

import jakarta.transaction.Transactional;

@Service
public class LocationServiceImpl implements LocationService {
		
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private PincodeRepository pincodeRepository;
	
	
	
	@Override
	@Cacheable(value = "countries")
	public CountryListDto getAllCountries() {
		List<Country> countries = countryRepository.findAll();
//	    List<CountryDto> dtos = countries.stream()
//	            .map(c -> new CountryDto(c.getId(), c.getName()))
//	            .toList();
		List<CountryDto> countryDtos=new ArrayList<>();
		
		for(Country country:countries)
		{
			countryDtos.add(new CountryDto(country.getId(), country.getName()));
		}

		
		
		
//		countries.forEach(data -> countryDtos.add(new CountryDto(data.getId(),data.getName())));
	    return new CountryListDto(countryDtos);
	}

	 @Override
	 @Cacheable(value = "country", key = "#id")
	public CountryDto getCountryById(Integer id) {
        Country country = countryRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Country not found"));
        // Map to DTO
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }

	 @Cacheable(value = "countriesCache", key = "#keyword")
	 public List<Country> searchCountries(String keyword) {
	      return countryRepository.findByNameContainingIgnoreCase(keyword);
	 }

	   @Override
	    @Transactional
	    @CacheEvict(value = {"countries", "country"}, allEntries = true)
	    public Country saveCountry(Country country) {
	        // Save country first
	        Country savedCountry = countryRepository.save(country);
	        	
	        if (country.getStates() != null) {
	            for (State state : country.getStates()) {
	                state.setCountry(savedCountry);
	                State savedState = stateRepository.save(state);

	                if (state.getDistricts() != null) {
	                    for (District district : state.getDistricts()) {
	                        district.setState(savedState);
	                        District savedDistrict = districtRepository.save(district);

	                        if (district.getPinCode() != null) {
	                            district.getPinCode().setDistrict(savedDistrict);
	                            pincodeRepository.save(district.getPinCode());
	                        }
	                    }
	                }
	            }
	        }

	        return savedCountry;
	    }
	

	@Override
	public void deleteCountry(Integer id) {
		if (!countryRepository.existsById(id)) {
            throw new RuntimeException("Country not found with id " + id);
        }
		countryRepository.deleteById(id);
	}

	@Override
	@Cacheable(value = "states", key = "#countryId")
	public StateListDto getStatesByCountry(Integer countryId) {
	    List<State> states = stateRepository.findByCountryId(countryId);

//	    List<StateDto> stateDtos = states.stream()
//	            .map(c -> new StateDto(c.getId(), c.getStateName()))
//	            .toList();
	    List<StateDto> stateDtos=new ArrayList<>();
//	    for(State state:states)
//	    {
//	    	stateDtos.add(new StateDto(state.getId(),state.getStateName()));
//	    }
	    
	    states.forEach(data -> stateDtos.add(new StateDto(data.getId(),data.getStateName())));
	   
	    
	    return new StateListDto(stateDtos);
	    
	}

	@Override
	@Cacheable(value = "state", key = "#id")
	public StateDto getStateById(Integer id) {
		System.out.println("calling from db");
		State state= stateRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("State not found with id " + id));
		StateDto stateDto=new StateDto();
		stateDto.setId(state.getId());
		stateDto.setStateName(state.getStateName());
		return stateDto;
	}
	@Override
	@Cacheable(value = "states")
	public StateListDto getAllStates() {
		List<State> states = stateRepository.findAll();
	    List<StateDto> stateDtos = states.stream()
	            .map(c -> new StateDto(c.getId(), c.getStateName()))
	            .toList();
	    return new StateListDto(stateDtos);
	}

	@Override
	@CacheEvict(value = "savestates", key = "#countryId")
	public State saveState(Integer countryId,State state) {
		Country country=countryRepository.findById(countryId).orElseThrow(()-> new RuntimeException(" country id not  found with :"+countryId));
		System.out.println("savestate method is calling");
		state.setCountry(country);
		return stateRepository.save(state);
	}
	

	@Override
	public void deleteState(Integer id)
	{
		 if (!stateRepository.existsById(id)) {
	            throw new RuntimeException("State not found with id " + id);
	        } 
		stateRepository.deleteById(id);
		
	}

	@Override
	@Cacheable(value = "Alldistrict")
	public List<District> getDistrictsByState(Integer stateId) {
		return districtRepository.findByStateId(stateId);
	}

	@Override
	@Cacheable(value = "districts",key="#id")
	public District getDistrictById(Integer id) {
		 return districtRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("District not found with id " + id));
	}

	@Override
	@CacheEvict(value = "savedistricts", key = "#stateId")
	public District saveDistrict(Integer stateId,District district) {
		 State state=stateRepository.findById(stateId).orElseThrow(()-> new RuntimeException("state not found with id :"+stateId));
		district.setState(state);
		return districtRepository.save(district);
	}

	@Override
	public void deleteDistrict(Integer id) {
		 if (!districtRepository.existsById(id)) {
	            throw new RuntimeException("District not found with id " + id);
	        }
		districtRepository.deleteById(id);
	}

	@Override
	@Cacheable(value="pincodedistricts")
	public PinCode getPincodeByDistrict(Integer districtId) {
		  return pincodeRepository.findByDistrictId(districtId)
	                .orElseThrow(() -> new RuntimeException("Pincode not found for district id " + districtId));
	}

	@Override
	@CacheEvict(value = "savepincode",key="#districtId")
	public PinCode savePincode(Integer districtId,PinCode pincode) {
		District district=districtRepository.findById(districtId).orElseThrow(()-> new RuntimeException("District id not found with :"+districtId));
		pincode.setDistrict(district);
		return pincodeRepository.save(pincode);
	}

	@Override
	public void deletePincode(Integer id) {
		if (!pincodeRepository.existsById(id)) {
            throw new RuntimeException("Pincode not found with id " + id);
        }
		pincodeRepository.deleteById(id);
	}

	

}
