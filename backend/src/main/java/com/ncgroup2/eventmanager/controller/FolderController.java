package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Folder;
import com.ncgroup2.eventmanager.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        folderService.create(folder);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAllByCustId(@RequestParam String customerId) {
        List<Folder> foldersByCustId = folderService.getAllByCustId(customerId);
        return new ResponseEntity<>(foldersByCustId, HttpStatus.OK);
    }

    @GetMapping("/notes")
    public ResponseEntity<List<Event>> getNotesByCustIdByFolderId(@RequestParam String custId, @RequestParam String folderId){
        List<Event> notesByCustId = folderService.getNotesByCustIdByFolderId(custId,folderId);
        return new ResponseEntity<>(notesByCustId, HttpStatus.OK);
    }
}
