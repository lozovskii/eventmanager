package com.ncgroup2.eventmanager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message extends Entity {
    private String chatId;
    private String authorId;
    private String authorName;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;


    public Message() {
    }

    @Override
    public Object[] getParams() {

        return new Object[]{
                this.getChatId(),
                this.getAuthorId(),
                this.getAuthorName(),
                this.getContent(),
                this.getDate()
        };

    }

}
