package com.stark.webbanhang.api.user.configuration;


import com.stark.webbanhang.api.user.entity.Role;
import com.stark.webbanhang.api.user.entity.User;
import com.stark.webbanhang.api.user.repository.RoleRepository;
import com.stark.webbanhang.api.user.repository.UserRepository;

import com.stark.webbanhang.helper.exception.AppException;
import com.stark.webbanhang.helper.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Configuration
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ApplicationInitConfig {
     PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

                User user = User.builder()
                        .firstName("admin")
                        .lastName("admin")
                        .email("admin@gmail.com")
                        .status(1)
                        .password(passwordEncoder.encode("admin"))
                        .role(adminRole)  // Liên kết role đã tồn tại
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin. Please change the password.");
            }
        };
    }


}
