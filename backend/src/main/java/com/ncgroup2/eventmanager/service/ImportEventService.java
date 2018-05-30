package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.dto.ImportEventDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface ImportEventService {
    List<ImportEventDTO> getCustomerEvents();
    PDDocument createPDF(List<ImportEventDTO> data, String email);
}