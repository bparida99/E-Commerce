package com.user.service;


import com.user.dto.CredentialDto;
import com.user.dto.UserDto;
import com.user.exception.DuplicateResourceException;
import com.user.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface UserService {

    public UserDto save(UserDto userDto) throws DuplicateResourceException;

    public UserDto get(String id) throws ResourceNotFoundException;

    public UserDto findByEmail(String email) throws ResourceNotFoundException;

    public UserDto update(UserDto userDto) throws ResourceNotFoundException;

    public void delete(String userId) throws ResourceNotFoundException;

    public List<UserDto> fetchAll();

    public CredentialDto fetchUserCredentials(String email);
}
