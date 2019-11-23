package gag.sasu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotedPostResponse {

    private String userUid;

    private String postUid;

    private Boolean up;

    private Boolean down;

}
