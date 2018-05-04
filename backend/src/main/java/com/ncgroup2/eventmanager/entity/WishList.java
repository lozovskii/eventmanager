package com.ncgroup2.eventmanager.entity;

import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import lombok.Data;

import java.util.List;

@Data
public class WishList extends Entity{

    List<ItemWishListDto> items;

    @Override
    public Object[] getParams() {

        return new Object[]{
                this.getId()
        };
    }
}
