package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.MessageDao;
import com.ncgroup2.eventmanager.entity.Message;
import com.ncgroup2.eventmanager.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public void create(Message message) {
        messageDao.create(message);
    }

    @Override
    public List<Message> getAllByChatId(String chatId) {
        return messageDao.getAllByChatId(chatId);
    }
}
