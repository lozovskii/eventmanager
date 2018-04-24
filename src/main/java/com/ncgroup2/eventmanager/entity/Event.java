package com.ncgroup2.eventmanager.entity;

import java.time.Instant;

public class Event extends Entity {

    private Long folderId;
    private Long creatorId;
    private Instant startTime;
    private Instant endTime;
    private String priority;
    private boolean isPublic;
    private String frequency;
    private String status;

    public Event() {

    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isPublic() { return isPublic; }

    public void setPublic(boolean aPublic) { isPublic = aPublic; }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", folderId=" + folderId +
                ", creatorId=" + creatorId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", priority='" + priority + '\'' +
                ", isPublic=" + isPublic +
                ", frequency='" + frequency + '\'' +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
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
                this.isPublic(),
                this.getFrequency(),
                this.getStatus()
        };
    }
}
