package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.model.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    @Query(nativeQuery = true, name = "findUserByEmailOrUsername")
    public List<UserDto> findUserByEmailOrUsername( String email, String username);
    @Query(nativeQuery = true, name = "findAllUser")
    public List<UserDto> findAllUser();
    @Query(nativeQuery = true, name = "findUserById")
    public UserDto findUserById(@Param("id") int id);
    @Query(nativeQuery = true, value = "SELECT max(id) FROM users")
    public int selectMaxId();
}
