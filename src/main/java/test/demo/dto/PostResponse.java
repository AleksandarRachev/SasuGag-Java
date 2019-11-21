package test.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private String uid;

    private String title;

    private Integer points;

    private String categoryName;

    private String userUsername;

    private List<CommentResponse> comments;

    public Integer getComments() {
        return comments.size();
    }

}
