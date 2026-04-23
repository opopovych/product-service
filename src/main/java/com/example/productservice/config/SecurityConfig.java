package com.example.productservice.config;

import com.example.productservice.model.User;
import com.example.productservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByPhone("380000000000")) { // Твій номер телефону
                User user = new User();
                user.setPhone("380000000000");
                user.setPassword(passwordEncoder.encode("adminpass")); // Твій пароль
                user.setFullName("Головний Адмін");
                user.setEnabled(true); // Адмін одразу активний
                user.setRole("ROLE_ADMIN");
                userRepository.save(user);
                System.out.println("✅ Адміна створено успішно!");
            }
        };
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("phone")
                        .password(passwordEncoder().encode("adminpass"))
                        .roles("ADMIN")
                        .build()
        );
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Вимкнення CSRF, якщо не потрібне
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/viewL", "/register", "/viewM", "/viewS","/pdf/view/L",
                                "/html/view/L","/excel/view/L","/photo/view/L","/pdf/view/M",
                                "/html/view/M","/excel/view/M","/photo/view/M","/pdf/view/S",
                                "/new-client","/new-client/form","/new-client/range","/new-client/request",
                                "/html/view/S","/excel/view/S","/photo/view/S","/excel/download/L",
                                "/excel/download/M","/excel/download/S","/zp.png","/video/coffee-bg.mp4",
                                "/css/**", "/js/**", "/images/**", "/evro_kava_logo.svg") // Додано нові шляхи
                        .permitAll() // Дозволяємо доступ до головної сторінки та нових шляхів без аутентифікації
                        .requestMatchers("/upload/**").hasRole("ADMIN") // Доступ до /upload тільки для адміністраторів
                        .anyRequest().authenticated() // Інші запити потребують аутентифікації
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("phone") // ДОДАТИ ЦЕ
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                /*.formLogin(form -> form
                        .loginPage("/login") // Налаштування сторінки для входу
                        .defaultSuccessUrl("/index", true) // Після входу перенаправлення на /index, якщо немає попередньої сторінки
                        .permitAll() // Доступ до сторінки логіну без аутентифікації
                )*/
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Після виходу перенаправлення на головну сторінку
                        .permitAll()
                );

        return http.build();
    }




}
