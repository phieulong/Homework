package com.example.demo.service;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {
    public UserDto createUser(CreateUserRequest createUserRequest);

    public UserDto updateUser(CreateUserRequest createUserRequest, int id);

    public UserDto deleteUser(int id);

    public UserDto getUserById(int id);

    public List<UserDto> getUserByEmailOrUsername(String Email, String Username);

    public List<UserDto> getListUser();
}
