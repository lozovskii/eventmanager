package com.ncgroup2.eventmanager.dao;
import com.ncgroup2.eventmanager.entity.Folder;

import java.util.List;

public interface FolderDao extends DAO<Folder, Object> {

    List<Folder> getAllByCustId(String custId);
}
