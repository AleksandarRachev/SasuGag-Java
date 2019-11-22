package gag.sasu.service;

import gag.sasu.config.JwtTokenUtil;
import gag.sasu.dto.RegisterUserRequest;
import gag.sasu.dto.UserLoginRequest;
import gag.sasu.dto.UserLoginResponse;
import gag.sasu.dto.UserResponse;
import gag.sasu.entity.User;
import gag.sasu.enums.Role;
import gag.sasu.exception.ElementExistsException;
import gag.sasu.exception.ElementMissingException;
import gag.sasu.exception.TokenExpiredException;
import gag.sasu.exception.WrongCredentialsException;
import gag.sasu.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                       JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ElementMissingException(USER_NOT_FOUND));
    }

    public User getById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ElementMissingException(USER_NOT_FOUND));
    }

    private void validateRegisterUser(RegisterUserRequest registerUserRequest) {
        if (userRepository.findByEmail(registerUserRequest.getEmail()).isPresent()) {
            throw new ElementExistsException("Email taken");
        }
        if (userRepository.findByUsername(registerUserRequest.getUsername()).isPresent()) {
            throw new ElementExistsException("Username taken");
        }
    }

    public UserResponse registerUser(RegisterUserRequest registerUserRequest) {
        validateRegisterUser(registerUserRequest);
        User user = modelMapper.map(registerUserRequest, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user = userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }

    private void authenticate(UserLoginRequest user, User user1) {
        if (!passwordEncoder.matches(user.getPassword(), user1.getPassword())) {
            throw new WrongCredentialsException();
        }
    }

    public UserLoginResponse loginUser(UserLoginRequest authenticationRequest) throws TokenExpiredException {
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        User user = getUserByEmail(authenticationRequest.getEmail());
        if (user == null) {
            throw new ElementMissingException(USER_NOT_FOUND);
        }

        try {
            authenticate(authenticationRequest, user);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token has expired");
        }

        UserResponse userLogin = new UserResponse(user.getUid(),
                user.getEmail(), user.getUsername(), user.getRole());
        return new UserLoginResponse(jwtTokenUtil.generateToken(userDetails), userLogin);
    }
}
