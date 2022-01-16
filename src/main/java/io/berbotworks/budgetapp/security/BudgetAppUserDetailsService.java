package io.berbotworks.budgetapp.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.berbotworks.budgetapp.models.User;
import io.berbotworks.budgetapp.services.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BudgetAppUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with " + email + " doesn't exist");
        }
        return new BudgetAppUserDetails(user);
    }

}
