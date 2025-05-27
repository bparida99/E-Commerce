package com.user.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AddressDto(UUID id, String houseNo, String street, String city, String state, String zipCode, String country, String type, String userId) {
}
