package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.objects.ExtendedTag;
import com.ncgroup2.eventmanager.entity.Item;

import java.util.Collection;

public interface ItemService {

    Item getItemById(String itemId);

    Collection<Item> getCreatedItems(String creatorId);

    Collection<Item> getAllItems();

    void createItem(Item item);

    void createItems(Collection<Item> items);

    void updateItemByField(String itemId, String fieldName, String fieldValue);

    void update(Item item);

    void deleteItems(Collection<Item> trash);

    void removeTags(Collection<ExtendedTag> trash);

    void addTags(Collection<ExtendedTag> tags, String itemId);

    void updateRating(String itemId, String customerLogin);
}
