package com.ncgroup2.eventmanager.entity;

import com.ncgroup2.eventmanager.dto.ItemTagDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Item extends Entity {

    private String description;
    private String image;
    private String link;
    private LocalDate dueDate;
    private List<ItemTagDto> tags;

    @Override
    public String toString() {
        return super.toString() +
                "\nItem{" +
                "description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link;
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.name,
                this.description,
                this.image,
                this.link,
                this.dueDate,
                this.getId()
        };
    }
}