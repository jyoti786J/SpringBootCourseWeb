package hkmu.wadd.dao;


import hkmu.wadd.dto.UserRegistrationDto;
import hkmu.wadd.exception.UserAlreadyExistsException;
import hkmu.wadd.exception.UserNotFoundException;
import hkmu.wadd.model.User;
import hkmu.wadd.validator.UserValidator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PollVoteRepository pollVoteRepository;
    private final PollCommentRepository pollCommentRepository;
    private final CourseCommentRepository courseCommentRepository;

    public static final Set<String> VALID_ROLES = Set.of("ADMIN", "TEACHER", "STUDENT");

    @Autowired
    public UserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder, PollVoteRepository pollVoteRepository, PollCommentRepository pollCommentRepository, CourseCommentRepository courseCommentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pollVoteRepository = pollVoteRepository;
        this.pollCommentRepository = pollCommentRepository;
        this.courseCommentRepository = courseCommentRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(String username, String password, String fullName,
                           String email, String phone, String role) {
        if (userRepository.existsById(username)) {
            throw new UserAlreadyExistsException(username);
        }

        validateRole(role);

        User user = new User(
                username,
                passwordEncoder.encode(password),
                fullName,
                email,
                phone,
                role
        );
        userRepository.save(user);
    }

    public void save(User user) {
        validateUser(user);
        userRepository.save(user);
    }

    public void updateUser(User user) {
        if (!userRepository.existsById(user.getUsername())) {
            throw new UserNotFoundException(user.getUsername());
        }
        validateUser(user);
        userRepository.save(user);
    }

    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new UserNotFoundException(username);
        }
        // First delete all votes by this user
        pollVoteRepository.deleteByUsername(username);

        // Then delete all poll comments by this user
        pollCommentRepository.deleteByAuthorUsername(username);

        // Then delete all course comments by this user
        courseCommentRepository.deleteByAuthorUsername(username);

        // Finally delete the user
        userRepository.deleteById(username);
    }

    @PostConstruct
    public void initializeDefaultAdmin() {
        if (userRepository.count() == 0) {
            User admin = new User(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "Administrator",
                    "admin@school.edu",
                    "12345678",
                    "ADMIN"
            );
            userRepository.save(admin);
        }
    }

    private void validateUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        validateRole(user.getRole());
    }

    private void validateRole(String role) {
        if (role == null || !VALID_ROLES.contains(role.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Invalid role: " + role + ". Valid roles are: " + VALID_ROLES);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}