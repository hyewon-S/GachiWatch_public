package ssd.springcooler.gachiwatch.dao;

import java.util.Date;
import java.util.List;

public interface CrewChatDao {
    List<String> getChat(int crewId);
    boolean insertChat(int crewId, String chat, Date date);
}