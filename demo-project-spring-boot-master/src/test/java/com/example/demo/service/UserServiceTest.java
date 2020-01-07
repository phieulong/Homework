package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeTestClass
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);

        User user = new User();
        user.setEmail("sam.smith@gmail.com");
        String date ="1998/12/31";
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dddd");



    }

    @Test
    public void getUserByEmail() {
        String email = "sam.smith@gmail.com";
        assertThat(userRepository).isNull();
    }
}
