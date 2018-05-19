package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Message;
import java.util.List;

public interface MessageService {
    void create(Message message);
    List<Message> getAllByChatId(String chatId);
}
