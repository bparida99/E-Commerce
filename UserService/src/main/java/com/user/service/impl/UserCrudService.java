package com.user.service.impl;

import com.user.dao.UserRepository;
import com.user.dto.CredentialDto;
import com.user.dto.UserDto;
import com.user.entity.User;
import com.user.exception.DuplicateResourceException;
import com.user.exception.ResourceNotFoundException;
import com.user.service.UserService;
import com.user.util.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserCrudService implements UserService {

    private final UserRepository userRepository;

    public UserCrudService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto save(UserDto userDto)  throws DuplicateResourceException{
        User user = MapperUtil.mapToUser.apply(userDto);
        try {
            return MapperUtil.mapToUserDto.
                    apply(userRepository.saveAndFlush(user));
        }catch (DataIntegrityViolationException ex){
            throw new DuplicateResourceException("Email already exist");
        }
    }

    @Override
    public UserDto get(String id) throws ResourceNotFoundException {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return MapperUtil.mapToUserDto.
                apply(user);
    }

    @Override
    public UserDto findByEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return MapperUtil.mapToUserDto.
                apply(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) throws ResourceNotFoundException {
        User user = userRepository.findById(userDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (userDto.name() != null && !userDto.name().equals(user.getName())) {
            user.setName(userDto.name());
        }
        if (userDto.email() != null && !userDto.email().equals(user.getEmail())) {
            user.setEmail(userDto.email());
        }
        return MapperUtil.mapToUserDto.apply(userRepository.save(user));
    }

    @Override
    public void delete(String userId) throws ResourceNotFoundException {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> fetchAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user->
                        MapperUtil.mapToUserDto.apply(user))
                .toList();
    }

    @Override
    public CredentialDto fetchUserCredentials(String email) {
        return userRepository.findCredentials(email);
    }

}
