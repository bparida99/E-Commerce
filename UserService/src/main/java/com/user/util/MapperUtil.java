package com.user.util;
import com.user.dto.AddressDto;
import com.user.dto.UserDto;
import com.user.entity.Address;
import com.user.entity.Credential;
import com.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MapperUtil {


    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static Function<User, List<AddressDto>> mapToAddressesDto = (user) -> {
        return user.getAddresses().stream()
                .map(address ->
                        AddressDto.builder()
                                .id(address.getId())
                                .houseNo(address.getHouseNo())
                                .street(address.getStreet())
                                .city(address.getCity())
                                .state(address.getState())
                                .zipCode(address.getZipCode())
                                .country(address.getCountry())
                                .type(address.getType())
                                .build()
                ).toList();
    };

    public static Function<User, UserDto> mapToUserDto = (user) -> {
        return UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .addresses(mapToAddressesDto.apply(user)) // Correct usage
                .build();
    };


    public static BiFunction<UserDto, User, Credential> mapToCredential =
            (userDto, user) -> {
                if (userDto.password() == null) {
                    return null;
                }
                Credential credential = new Credential();
                credential.setPasswordHash(passwordEncoder.encode(userDto.password()));
                credential.setUser(user);
                return credential;
            };

    public static BiFunction<UserDto, User, List<Address>> mapToAddresses =
            (userDto, user) -> {
                if (userDto.addresses() == null) {
                    return new ArrayList<>();
                }
                return userDto.addresses().stream()
                        .map(addressDto -> Address.builder()
                                .user(user)
                                .houseNo(addressDto.houseNo())
                                .street(addressDto.street())
                                .city(addressDto.city())
                                .state(addressDto.state())
                                .zipCode(addressDto.zipCode())
                                .country(addressDto.country())
                                .type(addressDto.type())
                                .build())
                        .toList();
            };

    public static Function<UserDto, User> mapToUser =
            (userDto) -> {
                User user = new User();
                user.setName(userDto.name());
                user.setEmail(userDto.email());
                user.setCredential(mapToCredential.apply(userDto, user));
                user.getAddresses().addAll(mapToAddresses.apply(userDto, user));
                return user;
            };

}
