package com.airbnb.service;


import com.airbnb.dto.BookingDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class PDFService {

    private static final String PDF_DIRECTORY = "/path/to/your/pdf/directory/";

    public boolean generatePDF(String fileName, BookingDto dto){
        try{
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            // Create a table with 3 columns
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100); // Make the table take up the whole page width

            // Add table headers
            PdfPCell cell = new PdfPCell(new Phrase("Booking Configuration", font));
            cell.setColspan(2); // Span across 2 columns
            table.addCell(cell);

            // Add data to the table
            table.addCell(new Phrase("Guest Name:", font));
            table.addCell(new Phrase(dto.getGuestName(), font));
            table.addCell(new Phrase("Price per Night:", font));
            table.addCell(new Phrase(String.valueOf(dto.getPrice()), font));
            table.addCell(new Phrase("Total Price:", font));
            table.addCell(new Phrase(String.valueOf(dto.getTotalPrice()), font));

            // Add the table to the document
            document.add(table);

            document.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
//@Service
//public class PDFService {
//
//    private static final String PDF_DIRECTORY = "/path/to/your/pdf/directory/";
//
//    public boolean generatePDF(String fileName, BookingDto dto){
//        try{
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(fileName));
//
//            document.open();
//            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
//
//            Chunk bookingConfiguration = new Chunk("Booking Configuration", font);
//            Chunk guestName = new Chunk("Guest Name:"+dto.getGuestName(), font);
//            Chunk price = new Chunk("Price per Night:"+dto.getPrice(), font);
//            Chunk totalPrice = new Chunk("Total Price:"+dto.getTotalPrice(), font);
//
//            document.add(bookingConfiguration);
//            document.add(new Paragraph("\n"));
//
//            document.add(guestName);
//            document.add(new Paragraph("\n"));
//
//            document.add(price);
//            document.add(new Paragraph("\n"));
//
//            document.add(totalPrice);
//
//            document.close();
//            return true;
//        } catch (Exception e){
//            e.printStackTrace();
//
//        }
//        return false;
//    }
//
//}
