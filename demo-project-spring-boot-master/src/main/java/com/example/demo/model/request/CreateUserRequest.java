package com.example.demo.model.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotNull(message = "Full name is required")
    @NotEmpty(message = "Full name is required")
    @ApiModelProperty(
            example = "Sam Smith",
            notes = "Full name cannot be empty",
            required = true
    )
    private String username;

    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email is required")
    @Email(message = "Please provide a valid email")
    @ApiModelProperty(
            example = "sam.smith@gmail.com",
            notes = "Email cannot be empty",
            required = true
    )
    private String email;

    @NotNull(message = "Address is required")
    @NotEmpty(message = "Address is required")
    @ApiModelProperty(
            example="Hanoi, VietNam",
            notes="Address cannot be empty",
            required=true
    )
    private String address;
}

