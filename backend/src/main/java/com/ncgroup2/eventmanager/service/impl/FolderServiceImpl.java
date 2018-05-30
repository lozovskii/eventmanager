package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.FolderDao;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Folder;
import com.ncgroup2.eventmanager.service.FolderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
    private final String FOLDER_NAME_DEFAULT = "Default";

    @Autowired
    private FolderDao folderDao;

    @Override
    public void create(Folder folder) {
        folderDao.create(folder);
    }

    @Override
    public List<Folder> getAllByCustId(String custId) {
        return folderDao.getAllByCustId(custId);
    }

    @Override
    public List<Folder> getSharedByLogin(String custLogin) {
        return folderDao.getSharedByCustLogin(custLogin);
    }

    @Override
    public List<Event> getNotesByFolderId(String folderId){
        return folderDao.getNotesByFolderId(folderId);
    }

    @Override
    public void moveNoteByNoteIdByFolderName(String noteId, String folderName){
        if (folderName.equals(FOLDER_NAME_DEFAULT)) {
            folderDao.moveNoteByNoteIdByFolderDefault(noteId);
        }else{
            int countOfFolders = folderDao.getCountByName(folderName);
            if(countOfFolders == 1) {
                folderDao.moveNoteByNoteIdByFolderName(noteId, folderName);
            }
        }
    }

    @Override
    public void deleteFolderById(String folderId){
        folderDao.moveNotesToDefaultFromFolderId(folderId);
        folderDao.deleteById(folderId);
    }

    @Override
    public void updateFolderNameById(String folderId, String folderName){
        folderDao.updateFolderNameById(folderId,folderName);
    }

}



