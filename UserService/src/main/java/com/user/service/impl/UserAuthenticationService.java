package com.user.service.impl;

import com.user.dto.CredentialDto;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthenticationService implements UserDetailsService {
    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CredentialDto credentialDto =
                userService.fetchUserCredentials(username);
        if (credentialDto == null) {
            throw new UsernameNotFoundException("User Name Not Found");
        }
        return new User(credentialDto.email(), credentialDto.passWord(), List.of(new SimpleGrantedAuthority("USER")));
    }
}
