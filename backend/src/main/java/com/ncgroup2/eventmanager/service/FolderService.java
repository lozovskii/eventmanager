package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Folder;

import java.util.List;

public interface FolderService {

    void create(Folder folder);

    List<Folder> getAllByCustId(String custId);

    List<Folder> getSharedByLogin(String custLogin);

    List<Event> getNotesByFolderId(String folderId);

    void moveNoteByNoteIdByFolderName(String noteId, String folderName);

    void deleteFolderById(String folderId);

    void updateFolderNameById(String folderId, String folderName);
}
