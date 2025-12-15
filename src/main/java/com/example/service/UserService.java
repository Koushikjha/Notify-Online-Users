package com.example.service;

import com.example.model.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class UserService {
    private final ConcurrentHashMap<String, UserStatus> users=new ConcurrentHashMap<>();

    public boolean addUser(String username) {

        return users.putIfAbsent(username, new UserStatus(username,true)) == null;
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public Collection<UserStatus> getAllUsers() {
        return users.values();
    }
}
