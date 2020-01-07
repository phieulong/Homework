package com.example.demo.controller;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserRequest;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Api(value = "User APIs")
public class UserController {
    private static String UPLOAD_DIR = System.getProperty("user.home") + "/upload";

    @Autowired
    private UserService userService;

    @ApiOperation(value="Create a user", response = UserDto.class)
    @ApiResponses({
            @ApiResponse(code = 400, message="Da ton tai user nay"),
            @ApiResponse(code = 500, message="Server bi loi"),
    })
    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest createUserRequest){
        UserDto userDto = userService.createUser(createUserRequest);
        return ResponseEntity.ok(userDto);
    }

    @ApiOperation(value="Update info of a user", response = UserDto.class)
    @ApiResponses({
            @ApiResponse(code = 400, message="username da bi trung"),
            @ApiResponse(code = 404, message="Khong tim thay user"),
            @ApiResponse(code = 500, message="Server bi loi"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid CreateUserRequest createUserRequest, @PathVariable int id){
        UserDto userDto = userService.updateUser(createUserRequest, id);
        return ResponseEntity.ok(userDto);
    }

    @ApiOperation(value="Delete a user by id", response = UserDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message="Khong tim thay user"),
            @ApiResponse(code = 500, message="Server bi loi"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        UserDto userDto = userService.deleteUser(id);
        return ResponseEntity.ok(userDto);
    }

    @ApiOperation(value="Get a user by id", response = UserDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message="Khong tim thay user"),
            @ApiResponse(code = 500, message="Server bi loi"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value="Get a user by email or name", response = UserDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message="Khong tim thay user"),
            @ApiResponse(code = 500, message="Server bi loi"),
    })
    @GetMapping("/")
    public ResponseEntity<?> getUserByEmailOrName(String email, String username){
        List<UserDto> user = userService.getUserByEmailOrUsername(email, username);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Get a list of user", response = UserDto.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 404, message="Khong tim thay user"),
            @ApiResponse(code = 500, message="Server bi loi")
    })
    @GetMapping("")
    public ResponseEntity<?> getListUser(){
        List<UserDto> users = userService.getListUser();
        return ResponseEntity.ok(users);
    }
}
