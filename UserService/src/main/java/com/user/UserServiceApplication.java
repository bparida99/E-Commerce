package com.user;

import com.user.dto.AddressDto;
import com.user.entity.Address;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		AddressDto address =  AddressDto.builder().street("").build();
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
