package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.ImportEventDao;
import com.ncgroup2.eventmanager.dto.ImportEventDTO;
import com.ncgroup2.eventmanager.service.ImportEventService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportEventServiceImpl implements ImportEventService {

    private final ImportEventDao ImportEventDao;

    @Autowired
    public ImportEventServiceImpl(ImportEventDao ImportEventDao) {
        this.ImportEventDao = ImportEventDao;
    }

    @Override
    public List<ImportEventDTO> getCustomerEvents() {
        return ImportEventDao.getCustomerEvents();
    }

    @Override
    public PDDocument createPDF(List<ImportEventDTO> data) {
        return ImportEventDao.createPDF(data);
    }

    @Override
    public Workbook createXLS(List<ImportEventDTO> data) {
        return ImportEventDao.createXLS(data);
    }
}