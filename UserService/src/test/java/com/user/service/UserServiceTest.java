package com.user.service;

import com.user.dao.UserRepository;
import com.user.dto.UserDto;
import com.user.entity.User;
import com.user.exception.DuplicateResourceException;
import com.user.exception.ResourceNotFoundException;
import com.user.service.impl.UserCrudService;
import com.user.util.MapperUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCrudService userCrudService;

    private UserDto getUserDto(){
        return UserDto.builder()
                .name("Biswojit")
                .email("biswojitp11@gmail.com")
                .build();
    }

    @Test
    public void save_ValidEntity_SuccessfullySaves(){
        UserDto userDto = getUserDto();
        User user = MapperUtil.mapToUser.apply(userDto);
        user.setUserId(UUID.randomUUID());
        Mockito.when(userRepository.saveAndFlush(Mockito.any(User.class))).thenReturn(user);
        UserDto result = userCrudService.save(userDto);
        assertEquals(user.getName(), result.name());
        assertNotNull(result.id());
    }

    @Test
    void save_DuplicateEntry_ThrowsException() {
        Mockito.when(userRepository.saveAndFlush(Mockito.any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Email already exist"));
        Exception ex = Assertions.assertThrows(DuplicateResourceException.class,
                () -> userCrudService.save(getUserDto()));
        assertEquals("Email already exist", ex.getMessage());
        verify(userRepository, times(1))
                .saveAndFlush(Mockito.any(User.class));
    }


    @Test
    void findByEmail_successful_Fetch() throws ResourceNotFoundException {
        UserDto userDto = getUserDto();
        User user = MapperUtil.mapToUser.apply(userDto);
        var email = "biswojitp11@gmail.com";
        when(userRepository.findByEmail(email))
                .thenReturn(user);
        UserDto result = userCrudService.findByEmail(email);
        assertNotNull(result);
        verify(userRepository,times(1))
                .findByEmail(email);
    }

    @Test
    void findByEmail_Throw_ResourceNotFoundException() throws ResourceNotFoundException {
        var email = "biswojitp11@gmail.com";
        when(userRepository.findByEmail(email))
                .thenReturn(null);
        Exception ex = assertThrows(ResourceNotFoundException.class,
                ()->userCrudService.findByEmail(email));
        assertEquals("User not found",ex.getMessage());
        verify(userRepository,times(1))
                .findByEmail(email);
    }

}
