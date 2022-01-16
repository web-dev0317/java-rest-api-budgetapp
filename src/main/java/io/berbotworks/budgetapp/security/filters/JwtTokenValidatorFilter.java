package io.berbotworks.budgetapp.security.filters;

import static io.berbotworks.budgetapp.security.filters.SecurityConstants.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login") || request.getServletPath().equals("/signup");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(JWT_HEADER);
        if (jwt != null) {
            try {
                SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt.split(" ")[1])
                        .getBody();
                log.debug("uid in JWTTokenValidator : {}", claims.get("uid"));
                request.setAttribute("uid", claims.get("uid"));
                String email = (String) claims.get("email");
                String authorities = (String) claims.get("authorities");
                log.debug("authorities {}", authorities);
                Authentication auth = new UsernamePasswordAuthenticationToken(email, null,
                        commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                log.debug("exception message {}", e.getMessage());
                log.debug("Invalid token");
                response.setStatus(401);
                return;
                // throw new BadCredentialsException("Invalid token!");
            }
        }
        filterChain.doFilter(request, response);
    }

    private Collection<? extends GrantedAuthority> commaSeparatedStringToAuthorityList(String str) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : str.split(",")) {
            authorities.add(new SimpleGrantedAuthority(authority.trim()));
        }
        return authorities;
    }

}
