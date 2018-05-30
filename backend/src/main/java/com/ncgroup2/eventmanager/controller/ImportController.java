package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.service.ImportEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import/")
public class ImportController {

    private final ImportEventService ImportEventService;

    @Autowired
    public ImportController(ImportEventService ImportEventService) {
        this.ImportEventService = ImportEventService;
    }

    @GetMapping(value = "pdf", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpStatus importEventsToPDF(@RequestParam String email) {
        ImportEventService.createPDF(ImportEventService.getCustomerEvents(), email);
        return HttpStatus.OK;
    }
}