package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ErrorServerException;
import com.example.demo.exception.InputOutputException;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.CreateUserRequest;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public String createUser(CreateUserRequest createUserRequest)throws InputOutputException{
        if(!getUserByEmailOrUsername(null, createUserRequest.getUsername()).isEmpty()){
            throw new BadRequestException("Da ton tai user nay");
        }
        try{
            User user = new User();
            BeanUtils.copyProperties(createUserRequest, user);
            userRepository.saveAndFlush(user);
        }catch ( Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
        return "Thanh cong";
    }

    public String updateUser(CreateUserRequest createUserRequest, int id) throws InputOutputException {
        UserDto userDto = getUserById(id);
        if(userDto==null){
            throw new RecordNotFoundException("Khong tim thay user");
        }
        if(userRepository.findUserByEmailOrUsername(null,createUserRequest.getUsername()).
                get(0).getId() != userDto.getId())
            throw new BadRequestException("username da bi trung");
        try{
            User user = new User();
            BeanUtils.copyProperties(createUserRequest, user);
            user.setId(id);
            userRepository.saveAndFlush(user);
        }catch (Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
        return "Thanh cong";
    }

    public String deleteUser(int id) throws RecordNotFoundException{
        UserDto userDto = userRepository.findUserById(id);
        if(userDto == null)
            throw new RecordNotFoundException("Khong tim thay user");
        else{
            try {
                userRepository.deleteById(id);
            }catch (Exception ex){
                throw new ErrorServerException("Server bi loi");
            }return "Thanh cong";
        }
    }

    public UserDto getUserById(int id) throws InputOutputException {
        try{
            UserDto userDto = userRepository.findUserById(id);
            if(userDto == null ){
                throw new RecordNotFoundException("Khong tim thay user");
            }
            return userDto;
        }catch (Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
    }

    @Override
    public List<UserDto> getUserByEmailOrUsername(String Email, String Username) throws  InputOutputException {
        try {
            List<UserDto> userDtos = userRepository.findUserByEmailOrUsername(Email, Username);
            if(userDtos.isEmpty()){
                throw new RecordNotFoundException("Khong tim thay user");
            }
            return userDtos;
        }catch (Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
    }

    public List<UserDto> getListUser() throws InputOutputException {
        try {
            List<UserDto> userDto = userRepository.findAllUser();
            if(userDto.isEmpty()){
                throw new RecordNotFoundException("Khong tim thay user");
            }
            return userDto;
        }catch (Exception ex){
            throw new ErrorServerException("Server bi loi");
        }
    }
}
