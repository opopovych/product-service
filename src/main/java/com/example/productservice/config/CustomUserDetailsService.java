package com.example.productservice.config;

import com.example.productservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        com.example.productservice.model.User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача з номером " + phone + " не знайдено"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getPhone())
                .password(user.getPassword())
                .disabled(!user.isEnabled()) // Ключовий момент: статус аккаунта
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}