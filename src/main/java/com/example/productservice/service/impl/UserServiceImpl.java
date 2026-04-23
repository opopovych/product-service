package com.example.productservice.service.impl;

import com.example.productservice.model.User;
import com.example.productservice.repository.UserRepository;
import com.example.productservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(User user) {
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("Цей номер телефону вже зареєстрований");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false); // Завжди false при реєстрації
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateStatus(Long userId, boolean status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));
        user.setEnabled(status);
        userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}