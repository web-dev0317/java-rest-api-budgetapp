package io.berbotworks.budgetapp.security.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.berbotworks.budgetapp.exceptions.UserAlreadyExistsException;
import io.berbotworks.budgetapp.models.User;
import io.berbotworks.budgetapp.services.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignUpFilter extends OncePerRequestFilter {

    // cannot autowire beans from a filter since its outside context ??
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (userService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils
                    .getWebApplicationContext(servletContext);
            userService = webApplicationContext.getBean(UserService.class);
        }
        String base64Credentials = request.getHeader(SecurityConstants.JWT_HEADER).split(" ")[1];
        log.debug("encoded cred : {}", base64Credentials);
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        log.debug("decoded cred : {}", credentials);
        final String[] values = credentials.split(":");
        // can check whether the values are well formed
        User newUser = new User(values[0], values[1], Arrays.asList("ROLE_USER"));
        log.debug("newUser : {}", newUser);
        try {
            userService.newUser(newUser);
        } catch (UserAlreadyExistsException e) {
            response.setStatus(400);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/signup");
    }

}
