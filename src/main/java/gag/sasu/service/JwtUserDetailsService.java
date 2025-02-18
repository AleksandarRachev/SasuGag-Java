package gag.sasu.service;

import gag.sasu.exception.WrongCredentialsException;
import gag.sasu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        gag.sasu.entity.User user = userRepository.findByEmail(username).orElseThrow(WrongCredentialsException::new);
        return new User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().name())));
    }
}