package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.dto.ItemTagDto;
import com.ncgroup2.eventmanager.entity.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    Item getItemById(String itemId);

    Collection<Item> getCreatedItems(String creatorId);

    void createItem(Item item);

    void createItems(Collection<Item> items);

    void updateItemByField(String itemId, String fieldName, String fieldValue);

    void removeTags(Collection<ItemTagDto> trash);

    void addTags(Collection<ItemTagDto> tags, String itemId);
}
