package test.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.demo.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String uid;

    private String email;

    private String username;

    private Role role;

}
