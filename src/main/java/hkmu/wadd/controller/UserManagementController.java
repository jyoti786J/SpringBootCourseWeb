package hkmu.wadd.controller;

import hkmu.wadd.dao.UserManagementService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import hkmu.wadd.dto.UserRegistrationDto;
import hkmu.wadd.model.User;
import hkmu.wadd.exception.UserAlreadyExistsException;
import hkmu.wadd.validator.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserManagementController {
    private static final Logger logger = LogManager.getLogger(UserManagementController.class);
    private final UserManagementService userManagementService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    private final List<String> availableRoles =
            new ArrayList<>(UserManagementService.VALID_ROLES);

    @Autowired
    public UserManagementController(UserManagementService userManagementService,
                                    UserValidator userValidator,
                                    PasswordEncoder passwordEncoder) {
        this.userManagementService = userManagementService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listUsers(Model model) {
        try {
            List<User> users = userManagementService.getAllUsers();
            model.addAttribute("users", users);
            return "listUser"; // Matches listUser.jsp
        } catch (Exception e) {
            logger.error("Error fetching users", e);
            model.addAttribute("error", "Failed to load users");
            return "listUser";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        model.addAttribute("availableRoles", availableRoles);
        return "addUser"; // Matches addUser.jsp
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                             BindingResult result,
                             Model model) {
        userValidator.validate(userDto, result);

        if (result.hasErrors()) {
            model.addAttribute("availableRoles", availableRoles);
            return "addUser";
        }

        try {
            User user = new User(
                    userDto.getUsername(),
                    passwordEncoder.encode(userDto.getPassword()),
                    userDto.getFullName(),
                    userDto.getEmail(),
                    userDto.getPhone(),
                    userDto.getRole()
            );

            userManagementService.save(user);
            return "redirect:/admin/users?updated";
        } catch (UserAlreadyExistsException e) {
            logger.warn("User creation failed - username exists: {}", userDto.getUsername());
            result.rejectValue("username", "user.exists", e.getMessage());
            model.addAttribute("availableRoles", availableRoles);
            return "addUser";
        } catch (Exception e) {
            logger.error("Error creating user", e);
            result.reject("creation.error", "Failed to create user");
            model.addAttribute("availableRoles", availableRoles);
            return "addUser";
        }
    }

    @GetMapping("/edit/{username}")
    public String showEditUserForm(@PathVariable String username,
                                   Model model) {


        try {
            User user = userManagementService.findByUsername(username);
            if (user == null) {
                logger.warn("User not found: {}", username);
                model.addAttribute("error", "user.not.found");
                return "redirect:/admin/users";
            }
            UserEditDto userEditDto = new UserEditDto();
            userEditDto.setFullName(user.getFullName());
            userEditDto.setEmail(user.getEmail());
            userEditDto.setPhone(user.getPhone());
            userEditDto.setRole(user.getRole());

            model.addAttribute("user", userEditDto);
            model.addAttribute("username", username);
            model.addAttribute("availableRoles", availableRoles);
            return "editUser";
        } catch (Exception e) {
            logger.error("Error loading user for editing", e);
            model.addAttribute("error", "Failed to load user");
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/edit/{username}")
    public String updateUser(@PathVariable String username,
                             @ModelAttribute("user") @Valid UserEditDto userEditDto,
                             BindingResult result,
                             Model model) {

        userEditDto.setUsername(username);

        if (result.hasErrors()) {
            model.addAttribute("username", username);
            model.addAttribute("availableRoles", availableRoles);
            return "editUser";
        }

        try {
            User existingUser = userManagementService.findByUsername(username);

            existingUser.setFullName(userEditDto.getFullName());
            existingUser.setEmail(userEditDto.getEmail());
            existingUser.setPhone(userEditDto.getPhone());
            existingUser.setRole(userEditDto.getRole());

            userManagementService.updateUser(existingUser);
            return "redirect:/admin/users?success=user.updated";
        } catch (Exception e) {
            logger.error("Error updating user", e);
            model.addAttribute("error", "Failed to update user");
            model.addAttribute("username", username);
            model.addAttribute("availableRoles", availableRoles);
            return "editUser";
        }
    }

    @PostMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username,
                             RedirectAttributes redirectAttributes) {
        try {
            // Prevent self-deletion
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (currentUser.equals(username)) {
                redirectAttributes.addFlashAttribute("error", "You cannot delete your own account");
                return "redirect:/admin/users";
            }

            userManagementService.deleteUser(username);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete user");
        }
        return "redirect:/admin/users";
    }

    public static class UserEditDto {
        private String username;

        @NotBlank
        private String fullName;

        @Email
        private String email;

        private String phone;

        @NotBlank
        private String role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}