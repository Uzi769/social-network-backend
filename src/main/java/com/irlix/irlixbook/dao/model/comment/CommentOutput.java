package com.irlix.irlixbook.dao.model.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentOutput {
    private String body;
    private LocalDate date;
    private Long userId;
    private Long postId;
}
