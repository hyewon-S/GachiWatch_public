package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.ChatDto;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;

import java.util.Date;

@Controller
public class ChatWebSocketController {
    @Autowired
    private CrewServiceImpl crewService;

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send/{crewId}")
    public ChatDto send(@Payload ChatDto message, @DestinationVariable Long crewId) {
        Crew crew = crewService.getCrew(crewId).get();
        Member member = memberService.getMember(message.getMember().getMemberId());
        CrewChat chat = new CrewChat(crew, new Date(), message.getChat(), member);
        crewService.insertCrewChat(chat);

        message.setDate(chat.getChatDate());
        messagingTemplate.convertAndSend("/topic/chatroom/" + crewId, message);
        return message;
    }
}
