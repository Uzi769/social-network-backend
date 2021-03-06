package com.irlix.irlixbook.dao.model.content.comment;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotBlank
    private String text;

    private Long parentCommentId;
}
