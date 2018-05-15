package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.dto.ItemTagDto;
import com.ncgroup2.eventmanager.entity.Item;

import java.util.Collection;
import java.util.List;

public interface ItemDao extends DAO<Item, Object>{

    void addTags(Collection<ItemTagDto> tags, Object item_id);

    Collection<Item> getCreatedItems(String creator_customer_login);

    void createItems(Collection<Item> items);

    void deleteItems(Collection<Item> trash);

    void deleteTags(Collection<ItemTagDto> trash);

}
