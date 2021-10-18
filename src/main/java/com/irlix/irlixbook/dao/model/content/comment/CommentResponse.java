package com.irlix.irlixbook.dao.model.content.comment;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String text;
    private LocalDateTime dateCreated;
    private String author;
    private Long contentId;
    private Long parentCommentId;
    private List<Long> repliesId;
}
