package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.FolderDao;
import com.ncgroup2.eventmanager.entity.Folder;
import com.ncgroup2.eventmanager.service.FolderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
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
}
