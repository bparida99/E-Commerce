package com.user.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UserDto(UUID id, String name, String email, List<AddressDto> addresses, String password) {
}
