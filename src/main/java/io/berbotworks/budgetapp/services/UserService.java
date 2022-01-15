package io.berbotworks.budgetapp.services;

import io.berbotworks.budgetapp.exceptions.UserAlreadyExistsException;
import io.berbotworks.budgetapp.models.User;
import io.berbotworks.budgetapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final DetailsService detailsService;
    private final PasswordEncoder passwordEncoder;

    public Long newUser(User user) {
        String email = user.getEmail();
        if (userExists(email)) {
            throw new UserAlreadyExistsException(
                    new StringBuilder()
                            .append("User with given mail id : ")
                            .append(email)
                            .append(" already exists")
                            .toString());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(detailsService.detailsForNewUser(user));

        return savedUser.getId();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
