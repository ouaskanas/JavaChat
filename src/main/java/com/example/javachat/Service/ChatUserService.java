package com.example.javachat.Service;


import com.example.javachat.Chat.ChatUser;
import com.example.javachat.Repositories.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatUserService {
    @Autowired
    private ChatUserRepository repository;

    public void findOrCreateUser(String username) {
        ChatUser user = repository.findByUsername(username);
        if (user == null) {
            user = new ChatUser();
            user.setUsername(username);
            user = repository.save(user);
        }
    }
}
