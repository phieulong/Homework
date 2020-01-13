package com.example.demo.controller;

import com.example.demo.DemoProjectApplication;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;


import java.text.ParseException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
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
    private RestTemplate restTemplate;
    private WireMockServer wireMockServer;
    @BeforeEach
    void configureSystemUnderTest() {
        this.restTemplate = new RestTemplate();
        this.wireMockServer = new WireMockServer(options()
                .dynamicPort()
        );
        this.wireMockServer.start();
        configureFor("localhost", this.wireMockServer.port());
    }

    @Test
    @DisplayName("Should ensure that WireMock server was started")
    void shouldEnsureThatServerWasStarted() {
        givenThat(WireMock.get(urlEqualTo("/")).willReturn(aResponse()
                .withStatus(200)
        ));

        String serverUrl = buildServerUrl();
        ResponseEntity<String> response = restTemplate.getForEntity(serverUrl, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    private String buildServerUrl() {
        return String.format("http://localhost:%d", this.wireMockServer.port());
    }
    @AfterEach
    void stopWireMockServer() {
        this.wireMockServer.stop();
    }
    @Test
    public void testWireMockServer() {
        String body = "10";
        stubFor(WireMock.get(urlMatching("/api/loyaltyUser/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8181/api/loyaltyUser/Sam Smith", String.class);
        String value = response.getBody().toString();
        Integer rs = Integer.parseInt(value);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

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
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/")
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
        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/")
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
        createTestUser("Sam Smith",null, null);

        String id = Integer.toString(userRepository.findByUsername("Sam Smith").getId());

        String user ="{\n" +
                        "  \"address\": \"Ha Long\",\n" +
                        "  \"email\": \"abc@gmail.com\",\n" +
                        "  \"username\": \"tuan\"\n" +
                        "}";

        mvc.perform(MockMvcRequestBuilders
                .put("/api/user/"+id)
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
        createTestUser("Sam Smith",null, null);

        createTestUser("tuan",null, null);

        String id = Integer.toString(userRepository.findByUsername("Sam Smith").getId());

        String user ="{\n" +
                        "  \"address\": \"Ha Long\",\n" +
                        "  \"email\": \"abc@gmail.com\",\n" +
                        "  \"username\": \"tuan\"\n" +
                        "}";

        mvc.perform(MockMvcRequestBuilders
                .put("/api/user/"+id)
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
        mvc.perform(MockMvcRequestBuilders
                .delete("/api/user/"+id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("sam")));
    }

    @Test
    public void deleteUserReturnNullMessage() throws Exception {
        resetDb();

        mvc.perform(MockMvcRequestBuilders
                .delete("/api/user/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Khong tim thay user")))
                .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
    }

    @Test
    public void getUserById() throws Exception {
        createTestUser("Sam Smith",null, null);
        String id = Integer.toString(userRepository.findByUsername("Sam Smith").getId());
        mvc.perform(MockMvcRequestBuilders
                .get("/api/user/"+id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("Sam Smith")));
    }

    @Test
    public void getUserByIdReturnNullMessage() throws Exception {
        resetDb();

        mvc.perform(MockMvcRequestBuilders
                .get("/api/user/1").contentType(MediaType.APPLICATION_JSON))
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

        mvc.perform(MockMvcRequestBuilders
                .get("/api/user/?username=sam").contentType(MediaType.APPLICATION_JSON))
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

        mvc.perform(MockMvcRequestBuilders
                .get("/api/user/?email=sam.smith@gmail.com").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", is("sam")))
                .andExpect(jsonPath("$[1].username", is("sam 2")));
    }

    @Test
    public void getUserByEmailOrNameReturnNullMessageTest() throws Exception {
        resetDb();

        mvc.perform(MockMvcRequestBuilders
                .get("/api/user/?username=sam").contentType(MediaType.APPLICATION_JSON))
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

        mvc.perform(MockMvcRequestBuilders
                .get("/api/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", is("sam")))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getListUserReturnNullMessageTest() throws Exception {
        resetDb();

        mvc.perform(MockMvcRequestBuilders
                .get("/api/user").contentType(MediaType.APPLICATION_JSON))
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
