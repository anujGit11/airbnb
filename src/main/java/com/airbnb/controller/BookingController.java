package com.airbnb.controller;


import com.airbnb.dto.BookingDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.service.BucketService;
import com.airbnb.service.PDFService;
import com.airbnb.service.SmsService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;


    @Autowired
    private PDFService pdfService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private SmsService smsService;

    @PostMapping("/createBooking/{propertyId}")
    public ResponseEntity<?> createBooking(
            @RequestBody Booking booking,
            @AuthenticationPrincipal PropertyUser user,
            @PathVariable long propertyId

    ) throws IOException {
        booking.setPropertyUser(user);
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property = byId.get();
        int propertyPrice = property.getNightlyPrice();
        int totalNights = booking.getTotalNights();

        int totalPrice = propertyPrice * totalNights;

        booking.setProperty(property);
        booking.setTotalPrice(totalPrice);

        Booking savedBooking = bookingRepository.save(booking);

        BookingDto dto = new BookingDto();
        dto.setBookingId(savedBooking.getId());
        dto.setGuestName(savedBooking.getGuestName());
        dto.setPrice(propertyPrice);
        dto.setTotalPrice(savedBooking.getTotalPrice());

        boolean b = pdfService.generatePDF("F://april1//" + "booking-confirmation-id" + savedBooking.getId() + ".pdf", dto);
        if(b){

            MultipartFile file = BookingController.convert("F://april1//"+"booking-confirmation-id" + savedBooking.getId() + ".pdf");
            String uploadedFileUrl = bucketService.uploadFile(file, "air1bnb2");
            smsService.sendSms("+919584689847","Your booking is confirmed .Click for more information:"+uploadedFileUrl);

        }else{
            return new ResponseEntity<>("error",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(savedBooking,HttpStatus.CREATED);



    }

    public static MultipartFile convert(String filePath) throws IOException {
        File file = new File(filePath);

        byte[] fileContent = Files.readAllBytes(file.toPath());

        Resource resource = new ByteArrayResource(fileContent);

        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return fileContent.length == 0;
            }

            @Override
            public long getSize() {
                return fileContent.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

                Files.write(dest.toPath(), fileContent);
            }
        };
        return multipartFile;
    }

}






