package com.ncgroup2.eventmanager.entity;

import com.ncgroup2.eventmanager.objects.ExtendedTag;
import com.ncgroup2.eventmanager.objects.Item_Rater;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Item extends Entity {

    private String creator_customer_login;
    private String description;
    private String image;
    private String link;
    private LocalDate dueDate;

    private List<ExtendedTag> tags;
    private List<Item_Rater> raters;

    @Override
    public String toString() {
        return super.toString() +
                "\nItem{" +
                "creator_customer_login=" + creator_customer_login +
                " description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", dueDate='"+ dueDate + '\'' +
                ", \ntags='" + getTags() + '\'' +
                ", \nraters='"+ getRaters();
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.creator_customer_login,
                this.name,
                this.description,
                this.image,
                this.link,
                this.dueDate,
                this.getId()
        };
    }
}
