package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageDao {
    List<Message> getAllByChatId(String chatId);
    void create(Message message);
    UUID getChatId(UUID eventId);
}
