package com.medcheck.config;

import com.medcheck.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt để test cho dễ, sau này bật lại sau
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // Chỉ admin mới được vào
                        .requestMatchers("/report/submit", "/register").permitAll() // Cho phép đăng ký, báo cáo
                        .anyRequest().permitAll() // Còn lại cho xem hết (index, detail, alerts)
                )
                // Trong SecurityConfig.java
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Tất cả mọi người đăng nhập xong đều về trang chủ
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") // Đường dẫn kích hoạt đăng xuất
                        .logoutSuccessUrl("/login?logout") // Sau khi thoát xong thì đá về trang Login
                        .invalidateHttpSession(true) // Xóa sạch session
                        .deleteCookies("JSESSIONID") // Xóa cookie phiên làm việc
                        .permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repo) {
        return username -> repo.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .authorities(u.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user"));
    }
}