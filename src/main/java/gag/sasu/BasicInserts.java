package gag.sasu;

import gag.sasu.entity.User;
import gag.sasu.enums.Role;
import gag.sasu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BasicInserts {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User admin = new User();
        admin.setEmail("admin@abv.bg");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setUsername("admin");
        admin.setRole(Role.ADMIN);
        if (userRepository.findByEmail(admin.getEmail()).isEmpty()) {
            userRepository.save(admin);
        }
    }

}
