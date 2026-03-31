package com.medcheck.service;

import com.medcheck.model.User;
import com.medcheck.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa password
        user.setRole("USER"); // Mặc định là người dùng thường
        userRepository.save(user);
    }

    // Hàm này để ông tạo sẵn Admin ban đầu
    public void createAdminIfNotExist() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Password là admin123
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }
}