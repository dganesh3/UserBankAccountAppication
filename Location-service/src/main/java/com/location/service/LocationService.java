package com.location.service;

import java.util.*;

import com.location.dto.CountryDto;
import com.location.dto.CountryListDto;
import com.location.dto.StateDto;
import com.location.dto.StateListDto;
import com.location.entity.Country;
import com.location.entity.District;
import com.location.entity.PinCode;
import com.location.entity.State;

public interface LocationService {
	CountryListDto getAllCountries();
	    CountryDto getCountryById(Integer id);
	    List<Country> searchCountries(String keyword);
	    Country saveCountry(Country country);
	    void deleteCountry(Integer id);

	    // ---- States ----
	    StateListDto getStatesByCountry(Integer countryId);
	    StateDto getStateById(Integer id);
	    StateListDto getAllStates();
	    State saveState(Integer countryId,State state);
	    void deleteState(Integer id);

	    // ---- Districts ----
	    List<District> getDistrictsByState(Integer stateId);
	    District getDistrictById(Integer id);
	    District saveDistrict(Integer stateId,District district);
	    void deleteDistrict(Integer id);

	    // ---- Pincode ----
	    PinCode getPincodeByDistrict(Integer districtId);
	    PinCode savePincode(Integer districtId,PinCode pincode);
	    void deletePincode(Integer id);
}
