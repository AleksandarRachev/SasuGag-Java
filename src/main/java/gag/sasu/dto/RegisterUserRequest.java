package gag.sasu.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "Email must not be empty")
    @Email
    private String email;

    @NotBlank(message = "Password must not be empty")
    private String password;

    @Size(max = 30, message = "Username too long")
    private String username;

}
