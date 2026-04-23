package com.example.productservice.service;

import com.example.productservice.model.User;
import java.util.List;

public interface UserService {
    void register(User user);
    void updateStatus(Long userId, boolean status);
    List<User> findAllUsers();
}