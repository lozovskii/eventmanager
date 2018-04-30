package com.ncgroup2.eventmanager.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item extends Entity {

    private String description;
    private String image;
    private String link;

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.getId(),
                this.name,
                this.description,
                this.image,
                this.link
        };
    }
}
