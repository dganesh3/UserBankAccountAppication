package com.location.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.location.dto.CountryDto;
import com.location.dto.CountryListDto;
import com.location.dto.StateDto;
import com.location.dto.StateListDto;
import com.location.entity.Country;
import com.location.entity.District;
import com.location.entity.PinCode;
import com.location.entity.State;
import com.location.service.LocationService;

@RestController
@RequestMapping("/location")
public class LocationController 
{
	
	@Autowired
	private LocationService locationService;
	
	 @GetMapping("/countries")
	    public CountryListDto getAllCountries() {
	        return locationService.getAllCountries();
	    }

	    @GetMapping("/countries/{id}")
	    public ResponseEntity<CountryDto> getCountryById(@PathVariable Integer id) {
	    	System.out.println("calling from db");
	        return ResponseEntity.ok(locationService.getCountryById(id));
	    }

	    @GetMapping("/countries/search")
	    public List<Country> searchCountries(@RequestParam String keyword) {
	        return locationService.searchCountries(keyword);
	    }

	    @PostMapping("/countries")
	    public ResponseEntity<Country> addCountry(@RequestBody Country country) {
	        return ResponseEntity.ok(locationService.saveCountry(country));
	    }

	    @DeleteMapping("/countries/{id}")
	    public ResponseEntity<Void> deleteCountry(@PathVariable Integer id) {
	        locationService.deleteCountry(id);
	        return ResponseEntity.noContent().build();
	    }

	    // ------------------ State ------------------

	    @GetMapping("/countries/{countryId}/states")
	    public StateListDto getStatesByCountry(@PathVariable Integer countryId) {
	        return locationService.getStatesByCountry(countryId);
	    }

	    @GetMapping("/states/{id}")
	    public ResponseEntity<StateDto> getStateById(@PathVariable Integer id) {
	        return ResponseEntity.ok(locationService.getStateById(id));
	    }

	    @PostMapping("/countries/{countryId}/states/bulk")
	    public ResponseEntity<List<State>> saveStates(
	            @PathVariable Integer countryId,
	            @RequestBody List<State> states) {
	        
	        List<State> savedStates = states.stream()
	                .map(state -> locationService.saveState(countryId, state))
	                .toList();
	        
	        return ResponseEntity.ok(savedStates);
	    }


	    @DeleteMapping("/states/{id}")
	    public ResponseEntity<Void> deleteState(@PathVariable Integer id) {
	        locationService.deleteState(id);
	        return ResponseEntity.noContent().build();
	    }
	    
	    @GetMapping("/states")
	    public ResponseEntity<StateListDto> getAllStates()
	    {
	    	return ResponseEntity.ok(locationService.getAllStates());
	    }

	    // ------------------ District ------------------

	    @GetMapping("/states/{stateId}/districts")
	    public List<District> getDistrictsByState(@PathVariable Integer stateId) {
	        return locationService.getDistrictsByState(stateId);
	    }

	    @GetMapping("/districts/{id}")
	    public ResponseEntity<District> getDistrictById(@PathVariable Integer id) {
	        return ResponseEntity.ok(locationService.getDistrictById(id));
	    }

	    @PostMapping("/districts/{stateId}")
	    public ResponseEntity<List<District>> addDistrict(
	    		@PathVariable Integer stateId,
	    		@RequestBody List<District> districts) {
	    	
	    	List<District> savedDistricts = districts.stream()
	                .map(district -> locationService.saveDistrict(stateId, district))
	                .toList();
	    	
//	    	 List<District> savedDistricts = new ArrayList<>();
//
//	    	    for (District district : districts) {
//	    	        District saved = locationService.saveDistrict(stateId, district);
//	    	        savedDistricts.add(saved);
//	    	    }
	    	
	    	
	        return ResponseEntity.ok(savedDistricts);
	    }

	    @DeleteMapping("/districts/{id}")
	    public ResponseEntity<Void> deleteDistrict(@PathVariable Integer id) {
	        locationService.deleteDistrict(id);
	        return ResponseEntity.noContent().build();
	    }

	    // ------------------ Pincode ------------------

	    @GetMapping("/districts/{districtId}/pincode")
	    public ResponseEntity<PinCode> getPincodeByDistrict(@PathVariable Integer districtId) {
	        return ResponseEntity.ok(locationService.getPincodeByDistrict(districtId));
	    }

	    @PostMapping("/pincodes/{districtId}")
	    public ResponseEntity<PinCode> addPincode(@PathVariable Integer districtId,@RequestBody PinCode pincode) {
	    	
	    	
	    	
	        return ResponseEntity.ok(locationService.savePincode(districtId,pincode));
	    }

	    @DeleteMapping("/pincodes/{id}")
	    public ResponseEntity<Void> deletePincode(@PathVariable Integer id) {
	        locationService.deletePincode(id);
	        return ResponseEntity.noContent().build();
	    }
	

}
