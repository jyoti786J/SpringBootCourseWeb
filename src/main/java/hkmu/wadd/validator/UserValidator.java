package hkmu.wadd.validator;

import hkmu.wadd.dto.UserRegistrationDto;
import hkmu.wadd.dao.UserRepository;
import hkmu.wadd.model.User;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationDto user = (UserRegistrationDto) target;

        // Password confirmation check
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }

        // Check if username already exists
        if (userRepository.existsById(user.getUsername())) {
            errors.rejectValue("username", "username.exists", "Username already taken");
        }

        // Additional validations can be added here
        if (user.getEmail() != null && !user.getEmail().isEmpty() &&
                !user.getEmail().matches(".+@.+\\..+")) {
            errors.rejectValue("email", "email.invalid", "Invalid email format");
        }

        if (user.getPhone() != null && !user.getPhone().isEmpty() &&
                !user.getPhone().matches("^[0-9]{8,15}$")) {
            errors.rejectValue("phone", "phone.invalid", "Phone must be 8-15 digits");
        }
    }
}