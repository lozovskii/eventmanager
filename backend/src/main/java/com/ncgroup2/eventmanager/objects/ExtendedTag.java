package com.ncgroup2.eventmanager.objects;

import com.ncgroup2.eventmanager.entity.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ExtendedTag {
    Tag tag;
    String itemTagId;
}
