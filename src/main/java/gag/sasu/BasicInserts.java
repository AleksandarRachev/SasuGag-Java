package gag.sasu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import gag.sasu.entity.User;
import gag.sasu.enums.Role;
import gag.sasu.repository.UserRepository;

import javax.annotation.PostConstruct;

@Component
public class BasicInserts {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        User admin = new User();
        admin.setEmail("admin@abv.bg");
        admin.setPassword("admin");
        admin.setUsername("admin");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }

}
