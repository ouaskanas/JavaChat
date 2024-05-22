package com.example.javachat.Chat;

import com.example.javachat.Service.ChatMessageService;
import com.example.javachat.Service.ChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageService messageService;

    @Autowired
    private ChatUserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return messageService.saveMessage(chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        userService.findOrCreateUser(chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        updateUserList();
        return chatMessage;
    }

    @MessageMapping("/chat.getUsers")
    @SendTo("/topic/users")
    public List<String> getUsers() {
        return userService.findAllUsers();
    }

    @MessageMapping("/chat.removeUser")
    @SendTo("/topic/public")
    public ChatMessage removeUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        userService.removeUser(chatMessage.getSender());
        headerAccessor.getSessionAttributes().remove("username");
        updateUserList();
        return chatMessage;
    }

    private void updateUserList() {
        List<String> users = userService.findAllUsers();
        messagingTemplate.convertAndSend("/topic/users", users);
    }
}
