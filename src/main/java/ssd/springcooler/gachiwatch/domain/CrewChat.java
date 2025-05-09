package ssd.springcooler.gachiwatch.domain;

import java.util.Date;

public class CrewChat {
    Date date;
    String chat;

    public CrewChat(String chat) {
        this.chat = chat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
