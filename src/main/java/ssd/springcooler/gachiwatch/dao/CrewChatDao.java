package ssd.springcooler.gachiwatch.dao;
import ssd.springcooler.gachiwatch.domain.CrewChat;

public interface CrewChatDao {
    List<String> getChat(int crewId);
    boolean insertChat(int crewId, String chat, Date date);
}
