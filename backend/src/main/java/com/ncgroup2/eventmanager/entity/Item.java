package com.ncgroup2.eventmanager.entity;

import java.util.Arrays;

public class Item extends Entity {

    private String description;
    private byte[] image;
    private String link;

    public Item() {
    }

    public Item(String name, String description, byte[] image, String link) {
        super.name = name;
        this.description = description;
        this.image = image;
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image=" + Arrays.toString(image) +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.getName(),
                this.getDescription(),
                this.getImage(),
                this.getLink()
        };
    }

}
