package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Folder;

import java.util.Collection;

public interface FolderDao {

    void addFolder(Folder folder);

    void deleteFolder(Folder folder);

    Folder getByField(String fieldName, String fieldValue);

    Collection<Folder> getFolders(String fieldName, String fieldValue);

    Collection<Folder> getFolders();

    void updateFolder(Folder folder);

    void updateField(Folder folder, String fieldName, Object fieldValue);
}
