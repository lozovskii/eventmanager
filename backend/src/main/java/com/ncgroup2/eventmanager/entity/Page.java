package com.ncgroup2.eventmanager.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Page<E> {
    private int pageNumber;
    private int pagesAvailable;
    private List<E> pageItems = new ArrayList<>();
}