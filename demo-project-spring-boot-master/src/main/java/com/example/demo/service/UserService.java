package com.example.demo.service;

import com.example.demo.exception.InputOutputException;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {
    public String createUser(CreateUserRequest createUserRequest)throws InputOutputException;

    public String updateUser(CreateUserRequest createUserRequest, int id)throws InputOutputException;

    public String deleteUser(int id)throws InputOutputException;

    public UserDto getUserById(int id)throws InputOutputException;

    public List<UserDto> getUserByEmailOrUsername(String Email, String Username)throws InputOutputException;

    public List<UserDto> getListUser()throws InputOutputException;
}
