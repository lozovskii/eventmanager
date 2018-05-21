package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Location;
import com.ncgroup2.eventmanager.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private LocationService locationService;


    @Autowired
    public  LocationController(LocationService locationService) {this.locationService = locationService; }

    @PostMapping(value = "/create")
    public void createLocation(@RequestBody Location location) {
        System.out.println("In Location Controller!");
        System.out.println(location);
        locationService.create(location);
    }

    @PostMapping(value = "/update")
    public void updateLocation(@RequestBody Location location) {
        locationService.update(location);
    }

    @PostMapping(value = "/delete")
    public void deleteLocation(@RequestBody String locationId) {
        locationService.delete(locationId);
    }

    @GetMapping(value = "/getByLocId")
    public ResponseEntity<Location> getLocation(String locationId) {
        if (locationId.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Location location = locationService.getById(locationId);

            if (location == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(location, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getByEventId")
    public ResponseEntity<Location> getLocationByEvent(String locationId) {
        if (locationId.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Location location = locationService.getByEventId(locationId);

            if (location == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(location, HttpStatus.OK);
        }
    }


}
