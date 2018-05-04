package com.ncgroup2.eventmanager.service.entityservice.impl;

import com.ncgroup2.eventmanager.dao.impl.postgres.ItemDaoImpl;
import com.ncgroup2.eventmanager.dto.ItemTagDto;
import com.ncgroup2.eventmanager.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl {

    @Autowired
    ItemDaoImpl itemDao;

    public Item getItemById(String itemId){

        return itemDao.getById(itemId);
    }

    public void createItem(Item item){

        itemDao.create(item);
    }

    public void createItems(List<Item> items){

        itemDao.createItems(items);
    }

    public void updateItemByField(String itemId, String fieldName, String fieldValue){

        itemDao.updateField(itemId, fieldName, fieldValue);
    }

    public void removeTags(List<ItemTagDto> trash){

        itemDao.deleteTags(trash);
    }

    public void addTags(List<ItemTagDto> tags, String itemId){

        itemDao.addTags(tags, itemId);
    }

}
