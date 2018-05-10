package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.dto.ItemTagDto;
import com.ncgroup2.eventmanager.entity.Item;

import java.util.Collection;
import java.util.List;

public interface ItemDao extends DAO<Item, Object>{

    void addTags(List<ItemTagDto> tags, Object item_id);

    Collection<Item> getCreatedItems(String creator_customer_login);

    void createItems(List<Item> items);

    void deleteTags(List<ItemTagDto> trash);

}
