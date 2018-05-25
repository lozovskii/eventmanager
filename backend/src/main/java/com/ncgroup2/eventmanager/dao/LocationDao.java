package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Location;

public interface LocationDao extends DAO<Location, Object> {

    Location getByEventId(String evId);
}
