package com.ncgroup2.eventmanager.dao;

import java.util.Collection;

public interface DAO <E, K> {

    Collection<E> getAllEntities();

    E getEntityById(K id);

    E getEntityByField(String fieldName, K fieldValue);

    Collection<E> getEntitiesByField(String fieldName, K fieldValue);

    void updateEntity(E entity);

    void updateEntityField(K id, String fieldName, K fieldValue);

    void deleteEntity(K id);

    void createEntity(E entity);


}
