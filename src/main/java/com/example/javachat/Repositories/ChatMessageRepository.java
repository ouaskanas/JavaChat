package com.example.javachat.Repositories;

import com.example.javachat.Chat.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySender(String sender);
}

