package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Entity;

import java.util.Collection;

public interface DAO <E extends Entity, K > {

    Collection<E> getAll();

    E getById(K id);

    E getEntityByField(String fieldName, K fieldValue);

    Collection<E> getEntitiesByField(String fieldName, K fieldValue);

    void update(E entity);

    void updateField(K id, String fieldName, K fieldValue);

    void delete(K id);

    void create(E entity);


}
