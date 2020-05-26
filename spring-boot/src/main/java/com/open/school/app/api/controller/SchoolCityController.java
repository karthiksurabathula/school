
package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.entity.SchoolCityEntity;
import com.open.school.app.api.service.SchoolCityServiceImpl;

@RestController
@RequestMapping("api/")
public class SchoolCityController {

	@Autowired
	private SchoolCityServiceImpl schoolCityServiceImp;

	@PreAuthorize("hasPermission('schoolCity', 'GET')")
	@RequestMapping(value = "schoolCity", method = RequestMethod.GET)
	public ResponseEntity<?> getCities() {
		return schoolCityServiceImp.getCities();
	}

	@PreAuthorize("hasPermission('schoolCity', 'POST')")
	@RequestMapping(value = "schoolCity", method = RequestMethod.POST)
	public ResponseEntity<?> createCities(@RequestBody SchoolCityEntity schoolCity) {
		return schoolCityServiceImp.saveCity(schoolCity);
	}

	@PreAuthorize("hasPermission('schoolCity', 'PUT')")
	@RequestMapping(value = "schoolCity", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCities(@RequestBody SchoolCityEntity schoolCity) {
		return schoolCityServiceImp.updateCity(schoolCity);
	}

	@PreAuthorize("hasPermission('schoolCity', 'DELETE')")
	@RequestMapping(value = "schoolCity", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCity(@RequestParam long cityId) {
		return schoolCityServiceImp.removeCity(cityId);
	}

}
