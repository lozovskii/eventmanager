package com.ncgroup2.eventmanager.dao;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Folder;

import java.util.List;

public interface FolderDao extends DAO<Folder, Object> {

    List<Folder> getAllByCustId(String custId);

    List<Folder> getSharedByCustLogin(String custLogin);

    List<Event> getNotesByFolderId(String folderId);

    void moveNoteByNoteIdByFolderName(String custId, String folderId);

    void moveNoteByNoteIdByFolderDefault(String noteId);

    int getCountByName(String folderName);

    void deleteById(String folderId);

    void moveNotesToDefaultFromFolderId(String folderId);

    void updateFolderNameById(String folderId, String folderName);
}
