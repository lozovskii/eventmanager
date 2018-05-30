package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.dto.MessageDTO;
import com.ncgroup2.eventmanager.entity.Message;
import com.ncgroup2.eventmanager.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private MessageService massageService;

    @Autowired
    public MessageController (MessageService massageService){this.massageService = massageService;}

    @GetMapping(value = "/chat_messages")
    public List<Message> getAllMessage(@RequestParam String chatId) {
        return massageService.getAllByChatId(chatId);
    }

    @MessageMapping("/api/messages")
    @SendTo("/chat")
    public Message chat(MessageDTO messageDTO) {

        massageService.create(messageDTO.getMessage());

        return messageDTO.getMessage();
    }


}
