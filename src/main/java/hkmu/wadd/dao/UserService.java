package hkmu.wadd.dao;


import hkmu.wadd.exception.UserAlreadyExistsException;
import hkmu.wadd.model.User;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;


import java.util.List;


@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public void registerUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        userRepository.save(user);
    }

    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsById(username);
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public void updateUser(User user) {
        userRepository.save(user); // Save the updated user details
    }

}