package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Folder;

import java.util.List;

public interface FolderService {

    void create(Folder folder);

    List<Folder> getAllByCustId(String custId);

    List<Event> getNotesByCustIdByFolderId(String custId, String folderId);

    void moveNoteByNoteIdByFolderName(String noteId, String folderName);
}
