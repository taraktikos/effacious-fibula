package com.user.service;

import com.user.persistence.entity.User;
import com.user.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(email).map(user ->
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        getAuthorities(user.getRoles())
                )
        ).orElseThrow(() -> new UsernameNotFoundException(format("User with email=%s was not found", email)));
    }

    private List<GrantedAuthority> getAuthorities(List<User.Roles> roles) {
        return roles.stream().map(e -> new SimpleGrantedAuthority(e.name())).collect(Collectors.toList());
    }
}
