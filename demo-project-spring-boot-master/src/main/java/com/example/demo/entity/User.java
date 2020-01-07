package com.example.demo.entity;

import com.example.demo.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
@SqlResultSetMappings(value = {
        @SqlResultSetMapping(
                name = "UserInfo",
                classes = @ConstructorResult(
                        targetClass = UserDto.class,
                        columns = {
                                @ColumnResult(name = "id"),
                                @ColumnResult(name = "username"),
                                @ColumnResult(name = "email")
                        }
                )
        )
})
@NamedNativeQuery(name = "findUserByEmailOrUsername", resultSetMapping = "UserInfo",
                    query = "SELECT id, username, email " +
                            "FROM users " +
                            "WHERE users.email = ?1 OR users.username = ?2")
@NamedNativeQuery(name = "findAllUser", resultSetMapping = "UserInfo",
                    query = "SELECT id, username, email " +
                            "FROM users ")
@NamedNativeQuery(name = "findUserById", resultSetMapping = "UserInfo",
                    query = "SELECT id, username, email FROM users WHERE users.id = ?1")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="email")
    private String email;

    @NotNull
    @Column(name="username", unique = true)
    private String username;

    @Column(name="address")
    private String address;
}
