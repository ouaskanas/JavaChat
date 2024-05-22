package com.example.javachat.Service;

import com.example.javachat.Chat.ChatUser;
import com.example.javachat.Repositories.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatUserService {

    @Autowired
    private ChatUserRepository repository;

    public void findOrCreateUser(String username) {
        ChatUser user = repository.findByUsername(username);
        if (user == null) {
            user = new ChatUser();
            user.setUsername(username);
            repository.save(user);
        }
    }

    public List<String> findAllUsers() {
        return repository.findAll().stream()
                .map(ChatUser::getUsername)
                .collect(Collectors.toList());
    }

    public void removeUser(String username) {
        ChatUser user = repository.findByUsername(username);
        if (user != null) {
            repository.delete(user);
        }
    }
}
