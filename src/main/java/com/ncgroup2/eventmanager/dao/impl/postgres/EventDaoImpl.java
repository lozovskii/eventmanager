package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

public class EventDaoImpl extends JdbcDaoSupport implements EventDao {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void createEvent(String creatorId, Event event) {

        String query = "INSERT INTO \"Event\" " +
                     "(id,name,folder_id, creator_id ,start_time,end_time,priority,isPublic,frequency,status)" +
                     "VALUES(uuid_generate_v1()," + event.getName() + "," + null + "," + creatorId + "," +
                      event.getStartTime() + "," + event.getEndTime()  + "," + event.getPriority() + "," +
                      event.isPublic() + "," + event.getFrequency() + "," +
                      event.getStatus() + ")";

        this.getJdbcTemplate().update(query);
    }

    @Override
    public void deleteEvent(Event event){
        String query = "DELETE FROM Event WHERE id =" + event.getId();
        this.getJdbcTemplate().update(query);
    }
}
