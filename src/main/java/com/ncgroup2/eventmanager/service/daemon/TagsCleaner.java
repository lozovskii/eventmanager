package com.ncgroup2.eventmanager.service.daemon;

import com.ncgroup2.eventmanager.dao.impl.postgres.ItemDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TagsCleaner {

    @Autowired
    ItemDaoImpl itemDao;

    @Scheduled(fixedDelay = 3600000)
    public void DeleteUnverifiedCustomers() {

        itemDao.deleteUnusedTags();
        System.out.println("Unused tags (which use less than 1 time) successfully deleted!");
    }
}
