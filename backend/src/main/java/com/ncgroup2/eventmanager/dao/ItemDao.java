package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Page;
import com.ncgroup2.eventmanager.objects.ExtendedTag;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.util.sort.Sort;

import java.util.Collection;

public interface ItemDao extends DAO<Item, Object>{

    Page<Item> getAll(int pageNo, int pageSize, Sort sort);

    Collection<Item> getPopularItems();

    void addTags(Collection<ExtendedTag> tags, Object item_id);

    void createItems(Collection<Item> items);

    void deleteItems(Collection<Item> trash);

    void deleteTags(Collection<ExtendedTag> trash);

    void updateRating(Object itemId, Object customerLogin);

}
