package gag.sasu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteForPostResponse {

    private String uid;

    private String title;

    private Integer points;

    private String categoryName;

    private String userUsername;

    private List<CommentResponse> comments;

    private VotedPostResponse voteOnPost;

    public Integer getComments() {
        return comments.size();
    }
}
