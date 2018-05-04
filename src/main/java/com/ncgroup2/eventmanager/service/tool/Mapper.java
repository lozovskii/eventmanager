package com.ncgroup2.eventmanager.service.tool;

import com.ncgroup2.eventmanager.dto.ItemTagDto;
import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.entity.WishList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mapper {

    public static Collection<WishList> mapWishListToCollection(Map<String, List<ItemWishListDto>> wishListMap) {

        return wishListMap.entrySet()
                .stream()
                .map(temp -> {
                    WishList wishList = new WishList();
                    wishList.setId(temp.getKey());
                    wishList.setItems(temp.getValue());
                    return wishList;
                })
                .collect(
                        Collectors.toList());
    }
    
    public static Collection<Item> mapItemToCollection(Map<Item, List<ItemTagDto>> itemMap ){

        return itemMap.entrySet()
                .stream()
                .map( temp -> {
                    Item item = temp.getKey();
                    item.setTags(temp.getValue());
                    return item;
                })
                .collect(
                        Collectors.toList());
        
    }

    public static Collection<Item> mapDtoItemToItemCollection(List<ItemWishListDto> itemWishListDtos ){

        return itemWishListDtos
                .stream()
                .map(ItemWishListDto::getItem)
                .collect(Collectors.toList());
    }
}
