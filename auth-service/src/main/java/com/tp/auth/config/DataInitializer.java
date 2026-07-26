package com.tp.auth.config;

import com.tp.auth.entity.AppUser;
import com.tp.auth.entity.Role;
import com.tp.auth.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a default admin/admin account on first boot so the app is usable out of
 * the box. No-op once the user exists.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new AppUser(
                    "admin",
                    passwordEncoder.encode("admin"),
                    Role.ADMIN,
                    null
            ));
            log.info("Seeded default admin user (admin/admin) — change the password in production");
        }
    }
}
