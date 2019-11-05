package test.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import test.demo.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
