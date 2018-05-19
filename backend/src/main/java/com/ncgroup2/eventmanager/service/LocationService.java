package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Location;

public interface LocationService {

    Location getById(String locationId);

    Location getByEventId(String eventId);

    void create(Location location);

    void update(Location location);

    void updateByField(Object locationId, String fieldName, Object fieldValue);

    void delete(String id);

}

