package com.ncgroup2.eventmanager.entity;

public class Item extends Entity {

    private String description;
    private String image;
    private String link;

    public Item() {
    }

    public Item(String name,String description, String image, String link) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.link = link;
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.name,
                this.description,
                this.image,
                this.link
        };
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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
                "description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
