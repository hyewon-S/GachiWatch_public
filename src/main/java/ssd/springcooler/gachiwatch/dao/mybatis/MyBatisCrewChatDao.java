package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.CrewChatDao;

import java.util.Date;
import java.util.List;

@Repository
public class MyBatisCrewChatDao implements CrewChatDao {
    @Override
    public List<String> getChat(int crewId) {
        return null;
    }

    @Override
    public boolean insertChat(int crewId, String chat, Date date) {
        return false;
    }
}
