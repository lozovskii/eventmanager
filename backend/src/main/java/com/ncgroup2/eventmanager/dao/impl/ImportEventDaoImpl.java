package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.ImportEventDao;
import com.ncgroup2.eventmanager.dto.ImportEventDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class ImportEventDaoImpl extends JdbcDaoSupport implements ImportEventDao {

    private final DataSource dataSource;

    @Autowired
    public ImportEventDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<ImportEventDTO> getCustomerEvents() {
        String customerEvents = "SELECT e.name,e.description,e.start_time,ev.name as visibility FROM \"Customer_Event\" c " +
                "INNER JOIN \"Event\" e ON c.event_id = e.id " +
                "INNER JOIN \"Event_Visibility\" ev ON e.visibility = ev.id " +
                "WHERE customer_id = (SELECT id FROM \"Customer\" WHERE login = ?) " +
                "ORDER BY e.start_time";

        Object[] params = new Object[] {SecurityContextHolder.getContext().getAuthentication().getName()};

        return this.getJdbcTemplate() != null ? this.getJdbcTemplate().query(customerEvents, params, new BeanPropertyRowMapper<>(ImportEventDTO.class)) : null;
    }

    @Override
    public PDDocument createPDF(List<ImportEventDTO> data) {
        PDDocument document = null;

        try {
            document = new PDDocument();

            for (int i = 0; i < data.size(); i++) {
                PDPage page = new PDPage();
                document.addPage(page);
            }

            for (int i = 0; i < data.size(); i++) {
                String name = data.get(i).getName();

                PDPage page = document.getPage(i);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                PDFont font = PDType1Font.TIMES_BOLD;
                int fontSize = 48;
                float titleWidth = font.getStringWidth(name) / 1000 * fontSize;
                float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

                contentStream.beginText();
                contentStream.setFont(font, 48);
                contentStream.setStrokingColor(30,144,255);
                contentStream.moveTextPositionByAmount((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 30 - titleHeight);
                contentStream.showText(name);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 28);
                contentStream.newLineAtOffset(25,650);
                String description = data.get(i).getDescription().replaceAll("<.*?>", "");
                contentStream.showText(description);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
                contentStream.newLineAtOffset(25,600);
                String start = data.get(i).getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                contentStream.showText(start);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
                contentStream.newLineAtOffset(25,550);
                String visibility = data.get(i).getVisibility();
                contentStream.showText(visibility);
                contentStream.endText();

                contentStream.close();
            }

            setDocumentProperties(document);

            document.save("/home/akybenko/test.pdf");
            System.out.println("Document saved!");
        } catch (IOException e) {
            System.out.println("Document not saved!");
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return document;
    }

    @Override
    public Workbook createXLS(List<ImportEventDTO> data) {
        return null;
    }

    private void setDocumentProperties(PDDocument document) {
        PDDocumentInformation information = document.getDocumentInformation();

        information.setAuthor("Event Manager");
        information.setTitle("Your events");
        information.setCreator("Event Manager");
        information.setSubject("Event List");
    }
}