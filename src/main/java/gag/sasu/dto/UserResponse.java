package gag.sasu.dto;

import gag.sasu.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String uid;

    private String email;

    private String username;

    private Role role;

}
