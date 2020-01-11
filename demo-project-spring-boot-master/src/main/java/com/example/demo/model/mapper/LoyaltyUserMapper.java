package com.example.demo.model.mapper;

import com.example.demo.model.dto.LoyaltyUserDto;
import com.example.demo.model.dto.UserDto;

public class LoyaltyUserMapper {
    public static LoyaltyUserDto toLoyaltyUserDto(UserDto userDto, int loyaltyPoint) {
        LoyaltyUserDto loyaltyUserDto = new LoyaltyUserDto();
        loyaltyUserDto.setId(userDto.getId());
        loyaltyUserDto.setUsername(userDto.getUsername());
        loyaltyUserDto.setEmail(userDto.getEmail());
        loyaltyUserDto.setLoyaltyPoint(loyaltyPoint);
        return loyaltyUserDto;
    }
}
