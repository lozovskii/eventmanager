package com.ncgroup2.eventmanager.util;

import com.ncgroup2.eventmanager.objects.WishListItem;
import com.ncgroup2.eventmanager.entity.Tag;
import com.ncgroup2.eventmanager.entity.WishList;
import com.ncgroup2.eventmanager.objects.Item_Rater;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mapper {

    public static List<WishList> getWishLists(Map<String, List<WishListItem>> wishListMap) {

        return wishListMap.entrySet()
                .parallelStream()
                .map(temp -> {
                    WishList wishList = new WishList();
                    wishList.setId(temp.getKey());
                    wishList.setItems(temp.getValue());
                    return wishList;
                })
                .collect(Collectors.toList());
    }

    public static List<Tag> getTags(Object[] array) {

        return Stream.of(array)
                .map(t -> {
                    String[] tagData = String.valueOf(t)
                            .replaceAll("[ \\/()]", "")
                            .split(",");
                    Tag tag = new Tag();
                    tag.setId(tagData[0]);
                    tag.setName(tagData[1]);
                    tag.setCount(Integer.valueOf(tagData[2]));
                    return tag;
                })
                .collect(Collectors.toList());
    }

    public static List<Item_Rater> getRaters(Object[] array) {

        return Stream.of(array)
                .map(t -> {
                    String[] tagData = String.valueOf(t)
                            .replaceAll("[ \\/()]", "")
                            .split(",");
                    Item_Rater rater = new Item_Rater();
                    rater.setId(tagData[0]);
                    rater.setCustomer_login(tagData[1]);
                    return rater;
                })
                .collect(Collectors.toList());
    }

    public static String[] getStringArray(Object[] array) {

        return Arrays.toString(array)
                .replaceAll("[\\[|\\]]", "")
                .split(", ");
    }

//    public static Collection<Item> mapDtoItemToItemCollection(List<ItemWishListDto> itemWishListDtos ){
//
//        return itemWishListDtos
//                .stream()
//                .map(ItemWishListDto::getItem)
//                .collect(Collectors.toList());
//    }
}
