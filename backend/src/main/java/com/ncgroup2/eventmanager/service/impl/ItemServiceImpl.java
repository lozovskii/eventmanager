package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.ItemDao;
import com.ncgroup2.eventmanager.dto.ItemTagDto;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    public Item getItemById(String itemId){
        return itemDao.getById(itemId);
    }

    public Collection<Item> getCreatedItems(String creatorId){
        return itemDao.getCreatedItems(creatorId);
    }

    public void createItem(Item item){
        itemDao.create(item);
    }

    public void createItems(Collection<Item> items){
        itemDao.createItems((List<Item>) items);
    }

    public void updateItemByField(String itemId, String fieldName, String fieldValue){
        itemDao.updateField(itemId, fieldName, fieldValue);
    }

    public void update(Item item){
        itemDao.update(item);
    }

    public void removeTags(Collection<ItemTagDto> trash){
        itemDao.deleteTags((List<ItemTagDto>)trash);
    }

    public void addTags(Collection<ItemTagDto> tags, String itemId){
        itemDao.addTags((List<ItemTagDto>)tags, itemId);
    }

    public void deleteItems(Collection<Item> trash){
        itemDao.deleteItems(trash);
    }

}
