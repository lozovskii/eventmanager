package com.ncgroup2.eventmanager.entity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Event extends Entity {

    private String folderId;
    private String creatorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private String priority;
    private String visibility;
    private String frequency;
    private String description;
    private byte status;

    public Event() {
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }



    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public byte getStatus() { return status; }

    public void setStatus(byte status) { this.status = status; }

    @Override
    public String toString() {
        return "Event{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                "folderId='" + folderId + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", priority='" + priority + '\'' +
                ", isPublic=" + visibility +
                ", frequency='" + frequency + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.getName(),
                this.getFolderId(),
                this.getCreatorId(),
                this.getStartTime(),
                this.getEndTime(),
                this.getPriority(),
                this.getVisibility(),
                this.getFrequency(),
                this.getDescription(),
                this.getStatus()
        };
    }
}
