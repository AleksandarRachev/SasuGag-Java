package gag.sasu.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private String postUid;

    @NotBlank(message = "Cannot add empty comment")
    @Size(max = 200, message = "Comment too long")
    private String text;

}
