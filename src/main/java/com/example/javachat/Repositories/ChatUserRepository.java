package com.example.javachat.Repositories;

import com.example.javachat.Chat.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    ChatUser findByUsername(String username);
}