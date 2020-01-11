package com.example.demo.controller;

import com.example.demo.DemoProjectApplication;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.web.servlet.MockMvc;


import java.text.ParseException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoProjectApplication.class)
@AutoConfigureMockMvc
public class UserControllerIntergrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private int port = 8089;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);

    @AfterTestClass
    public void resetDb() {
        userRepository.deleteAll();
    }
    @Test
    public void createUser() throws Exception {
        resetDb();
        createTestUser("sam",null,null);
        String user ="{\n" +
                    "  \"address\": \"Hanoi\",\n" +
                    "  \"email\": \"abc@gmail.com\",\n" +
                    "  \"username\": \"tuan\"\n" +
                    "}";
        int id = userRepository.selectMaxId()+1;
        mvc.perform(post("/api/user/")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("abc@gmail.com")))
                .andExpect(jsonPath("$.username", is("tuan")))
                .andExpect(jsonPath("$.id", is(id)));
    }

    @Test
    public void createUserReturnErrorMessage() throws Exception {
        resetDb();
        createTestUser("sam",null,null);
        String user ="{\n" +
                "  \"address\": \"Hanoi\",\n" +
                "  \"email\": \"abc@gmail.com\",\n" +
                "  \"username\": \"sam\"\n" +
                "}";
        int id = userRepository.selectMaxId()+1;
        mvc.perform(post("/api/user/")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("username da bi trung")))
                .andExpect(jsonPath("$.statusCode", is("BAD_REQUEST")));
    }

    @Test
    public void updateUser() throws Exception {
        createTestUser("sam",null, null);

        String id = Integer.toString(userRepository.findByUsername("sam").getId());

        String user ="{\n" +
                        "  \"address\": \"Ha Long\",\n" +
                        "  \"email\": \"abc@gmail.com\",\n" +
                        "  \"username\": \"tuan\"\n" +
                        "}";

        mvc.perform(put("/api/user/"+id)
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("tuan")));
    }

    @Test
    public void updateUserReturnErrorMessage() throws Exception {
        resetDb();
        createTestUser("sam",null, null);

        createTestUser("tuan",null, null);

        String id = Integer.toString(userRepository.findByUsername("sam").getId());

        String user ="{\n" +
                        "  \"address\": \"Ha Long\",\n" +
                        "  \"email\": \"abc@gmail.com\",\n" +
                        "  \"username\": \"tuan\"\n" +
                        "}";

        mvc.perform(put("/api/user/"+id)
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("username da bi trung")))
                .andExpect(jsonPath("$.statusCode", is("BAD_REQUEST")));
    }

    @Test
    public void deleteUser() throws Exception {
        createTestUser("sam",null, null);
        String id = Integer.toString(userRepository.findByUsername("sam").getId());
        mvc.perform(delete("/api/user/"+id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("sam")));
    }

    @Test
    public void deleteUserReturnNullMessage() throws Exception {
        resetDb();

        mvc.perform(delete("/api/user/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Khong tim thay user")))
                .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
    }

    @Test
    public void getUserById() throws Exception {
        createTestUser("sam",null, null);
        String id = Integer.toString(userRepository.findByUsername("sam").getId());
        mvc.perform(get("/api/user/"+id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("sam")));
    }

    @Test
    public void getUserByIdReturnNullMessage() throws Exception {
        resetDb();

        mvc.perform(get("/api/user/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Khong tim thay user")))
                .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
    }

    @Test
    public void getUserByEmailOrNameTest() throws Exception {
        resetDb();
        createTestUser("sam", null, null);

        mvc.perform(get("/api/user/?username=sam").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("sam")))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getUserByEmailOrNameReturn2ItemTest() throws Exception {
        resetDb();
        createTestUser("sam", "sam.smith@gmail.com", null);
        createTestUser("sam 2", "sam.smith@gmail.com", null);

        mvc.perform(get("/api/user/?email=sam.smith@gmail.com").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", is("sam")))
                .andExpect(jsonPath("$[1].username", is("sam 2")));
    }

    @Test
    public void getUserByEmailOrNameReturnNullMessageTest() throws Exception {
        resetDb();

        mvc.perform(get("/api/user/?username=sam").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Khong tim thay user")))
                .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
    }


    @Test
    public void getListUserTest() throws Exception {
        resetDb();
        createTestUser("sam",null, null);

        mvc.perform(get("/api/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", is("sam")))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getListUserReturnNullMessageTest() throws Exception {
        resetDb();

        mvc.perform(get("/api/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Khong tim thay user")))
                .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
    }

    private void createTestUser(String name, String email, String address) throws ParseException {
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setAddress(address);
        userRepository.saveAndFlush(user);
    }
}
