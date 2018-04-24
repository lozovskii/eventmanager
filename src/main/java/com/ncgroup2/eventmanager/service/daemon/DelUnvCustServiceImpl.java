package com.ncgroup2.eventmanager.service.daemon;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DelUnvCustServiceImpl {

    @Autowired
    CustomerDao customerDao;

    @Scheduled(fixedDelay = 3600000)
    public void DeleteUnverifiedCustomers() {
        customerDao.deleteUnverifiedCustomers();
    }
}
