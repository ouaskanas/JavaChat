package com.example.javachat.Service;


import com.example.javachat.Chat.ChatMessage;
import com.example.javachat.Repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository repository;

    public List<ChatMessage> findAll() {
        return repository.findAll();
    }

    public List<ChatMessage> getMessagesBySender(String sender) {
        return repository.findBySender(sender);
    }

    public ChatMessage saveMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return repository.save(message);
    }
}

