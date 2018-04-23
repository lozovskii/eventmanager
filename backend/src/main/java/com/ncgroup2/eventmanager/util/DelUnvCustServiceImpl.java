package com.ncgroup2.eventmanager.util;
import com.ncgroup2.eventmanager.dao.impl.postgres.CustomerDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DelUnvCustServiceImpl {

    @Autowired
    CustomerDaoImpl customerDaoImpl;

    @Scheduled(fixedDelay = 3600000)
    public void DeleteUnverifiedCustomers() {

        customerDaoImpl.deleteUnverifiedCustomers();
        System.out.println("Unverified customers (more then 24 hours)successfully deleted!");
    }
}
