package com.example.demo.repository;

import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail() throws ParseException {
        User user = new User();
        user.setEmail("sam.smith@gmail.com");
//        user.setPhone("0916016987");
//        user.setPassword("123") ;
//        user.setFullname("Sam Smith");
//        user.setAvatar("https://techmaster.vn/media/image.jpg");
//        String date ="1998/12/31";
//        SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dddd");
//        user.setBirthday(formatter.parse(date));

        entityManager.persistAndFlush(user);

//        User found = userRepository.findByEmail(user.getEmail());

//        assertThat(found.getId()).isEqualTo(user.getId());
    }

    @Test
    public void findById() {
        User user = userRepository.findById(-1).orElse(null);
        assertThat(user).isNull();
    }

    @Test
    public void findAll() {
        User user1 = new User();
        user1.setEmail("sam.smith@gmail.com");
        User user2 = new User();
        user2.setEmail("joe@gmail.com");
        User user3 = new User();
        user3.setEmail("jame@gmail.com");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(3).extracting(User::getEmail).containsOnly(user1.getEmail(), user2.getEmail(), user3.getEmail());
    }
}
