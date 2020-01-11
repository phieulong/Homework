package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ErrorServerException;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.model.dto.LoyaltyUserDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.mapper.LoyaltyUserMapper;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.CreateUserRequest;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    RestTemplate restTemplate = new RestTemplate();

    public UserDto createUser(CreateUserRequest createUserRequest){
        User user;
        try{
            user = userRepository.findByUsername(createUserRequest.getUsername());
        }catch ( Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
        if(user != null){
            throw new BadRequestException("username da bi trung");
        }
        user = new User();
        try{
            BeanUtils.copyProperties(createUserRequest, user);
            userRepository.saveAndFlush(user);
        }catch (Exception ex){
            throw ex;
        }
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(CreateUserRequest createUserRequest, int id){
        LoyaltyUserDto loyaltyUserDto = getUserById(id);
        if(loyaltyUserDto == null){
            throw new RecordNotFoundException("Khong tim thay user");
        }
        if(!userRepository.findUserByEmailOrUsername(null,createUserRequest.getUsername()).isEmpty())
            throw new BadRequestException("username da bi trung");
        User user = new User();
        try{
            BeanUtils.copyProperties(createUserRequest, user);
            user.setId(id);
            userRepository.saveAndFlush(user);
        }catch (Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
        return UserMapper.toUserDto(user);
    }

    public UserDto deleteUser(int id){
        UserDto userDto = userRepository.findUserById(id);
        if(userDto == null)
            throw new RecordNotFoundException("Khong tim thay user");
        else{
            try {
                userRepository.deleteById(id);
            }catch (Exception ex){
                throw new ErrorServerException("Server bi loi");
            }
        return userDto;
        }
    }

    public LoyaltyUserDto getUserById(int id){
        UserDto userDto;
        try{
            userDto = userRepository.findUserById(id);
        }catch (Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
        if(userDto == null )
            throw new RecordNotFoundException("Khong tim thay user");
        String fooResourceUrl = "http://localhost:8181/api/loyaltyUser/"+userDto.getUsername();
        ResponseEntity<Integer> response = restTemplate.getForEntity(fooResourceUrl, Integer.class);
        return LoyaltyUserMapper.toLoyaltyUserDto(userDto,response.getBody());
    }

    @Override
    public List<UserDto> getUserByEmailOrUsername(String Email, String Username){
        if(Email == null && Username == null)
            return getListUser();
        List<UserDto> userDtos;
        try {
            userDtos = userRepository.findUserByEmailOrUsername(Email, Username);
        }catch (Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
        if(userDtos.isEmpty())
            throw new RecordNotFoundException("Khong tim thay user");
        return userDtos;
    }

    public List<UserDto> getListUser(){
        List<UserDto> userDto = new ArrayList<>();
        try{
            userDto = userRepository.findAllUser();
        }catch (Exception ex) {
            throw new ErrorServerException("Server bi loi");
        }
        if(userDto.isEmpty())
            throw new RecordNotFoundException("Khong tim thay user");
        return userDto;
    }
}
