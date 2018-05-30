package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.MessageDao;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Message;
import com.ncgroup2.eventmanager.util.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class MessageDaoImpl extends JdbcDaoSupport implements MessageDao {
    @Autowired
    DataSource dataSource;
    @Autowired
    private QueryService queryService;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Message> getAllByChatId(String chatId) {
        String query = queryService.getQuery("message.getAllByChatId");
        Object[] queryParams = new Object[]{chatId};
        return this.getJdbcTemplate().query(query, queryParams, new BeanPropertyRowMapper(Message.class));
    }

    @Override
    public void create(Message message) {
        String query = queryService.getQuery("message.create");
        Object[] queryParams = new Object[] {
                message.getChatId(),
                message.getAuthorId(),
                message.getContent()
        };
        this.getJdbcTemplate().update(query, queryParams);
    }
}
