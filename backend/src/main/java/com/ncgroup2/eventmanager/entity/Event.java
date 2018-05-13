package com.ncgroup2.eventmanager.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Event extends Entity {

    private String groupId;
    private String folderId;
    private String creatorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private String visibility;
    private String description;
    private String status;

    public Event() {
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.getName(),
                this.getGroupId(),
                this.getFolderId(),
                this.getCreatorId(),
                this.getStartTime(),
                this.getEndTime(),
                this.getVisibility(),
                this.getDescription(),
                this.getStatus()
        };
    }
}
