package hkmu.wadd.controller;

import hkmu.wadd.dao.UserService;
import hkmu.wadd.dto.UserRegistrationDto;

import hkmu.wadd.exception.UserAlreadyExistsException;
import hkmu.wadd.model.User;
import hkmu.wadd.validator.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Autowired
    public RegistrationController(UserService userService,
                                  PasswordEncoder passwordEncoder,
                                  UserValidator userValidator) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                               BindingResult result,
                               Model model) {

        // Custom validation
        userValidator.validate(userDto, result);

        userDto.setRole("STUDENT");


        try {
            User user = new User(
                    userDto.getUsername(),
                    passwordEncoder.encode(userDto.getPassword()),
                    userDto.getFullName(),
                    userDto.getEmail(),
                    userDto.getPhone(),
                    userDto.getRole()
            );

            userService.registerUser(user);
            return "redirect:/login?registered";
        } catch (UserAlreadyExistsException e) {
            result.rejectValue("username", "user.exists", "Username already exists");
            //model.addAttribute("availableRoles", List.of("STUDENT"));
            return "register";
        } catch (Exception e) {
            result.reject("registration.error", "Registration failed. Please try again.");
            //model.addAttribute("availableRoles", List.of("STUDENT"));
            return "register";
        }
    }
}