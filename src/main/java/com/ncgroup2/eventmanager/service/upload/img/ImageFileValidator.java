package com.ncgroup2.eventmanager.service.upload.img;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator {

    public static boolean isValid(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (isSupportedContentType(contentType)) {
            return true;
        } else return false;
    }

    private static boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }
}