package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.LocationDao;
import com.ncgroup2.eventmanager.entity.Location;
import com.ncgroup2.eventmanager.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationDao locationDao;

    @Autowired
    public LocationServiceImpl(LocationDao locationDao) {

        this.locationDao = locationDao;
    }

    @Override
    public Location getById(String locationId) {

        return locationDao.getById(locationId);
    }

    @Override
    public Location getByEventId(String eventId) {

        return locationDao.getByEventId(eventId);
    }

    @Override
    public void create(Location location) {

        locationDao.create(location);
    }

    @Override
    public void update(Location location) {

        locationDao.update(location);
    }

    @Override
    public void updateByField(Object locationId, String fieldName, Object fieldValue) {

        locationDao.updateField(locationId, fieldName, fieldValue);
    }

    @Override
    public void delete(String id) {
        locationDao.delete(id);

    }
}
