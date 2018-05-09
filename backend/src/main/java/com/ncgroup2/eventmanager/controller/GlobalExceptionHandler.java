//package com.ncgroup2.eventmanager.controller;
//
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.multipart.MultipartException;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MultipartException.class)
//    public String handleError(MultipartException e, RedirectAttributes attributes) {
//        attributes.addFlashAttribute("message", e.getCause().getMessage());
//
//        return "redirect:/profile/edit/upload/status";
//    }
//}
