package com.user.security;

import com.user.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

@Slf4j
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Authenticating {}", email);
        return userRepository.findByEmailIgnoreCase(email).map(user ->
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        newArrayList(new SimpleGrantedAuthority(user.getRole()))
                )
        ).orElseThrow(() -> {
            log.debug("User '{}' not found", email);
            return new UsernameNotFoundException(format("User with email=%s was not found", email));
        });
    }
}
