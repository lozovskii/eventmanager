package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Folder;
import com.ncgroup2.eventmanager.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public void create(@RequestBody Folder folder){
        System.out.println(folder);
        folderService.create(folder);
    }
}