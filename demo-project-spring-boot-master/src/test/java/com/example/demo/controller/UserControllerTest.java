package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.text.SimpleDateFormat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Test
    public void getUserByEmail() throws Exception {
        UserDto user = new UserDto();
        user.setEmail("sam.smith@gmail.com");
//        user.setPhone("0916016987");
//        user.setFullname("Sam Smith");
//        user.setAvatar("https://techmaster.vn/media/image.jpg");
//        String date ="1998/12/31";
//        SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dddd");
//        user.setBirthday(formatter.parse(date));
//
//        given(service.getUserByEmail("sam.smith@gmail.com")).willReturn(user);

        mvc.perform(get("/api/user/email/sam.smith@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.fullname", is(user.getFullname())));
    }
}
