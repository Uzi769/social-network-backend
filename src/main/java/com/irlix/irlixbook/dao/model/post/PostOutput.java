package com.irlix.irlixbook.dao.model.post;

import com.irlix.irlixbook.dao.model.tag.TagOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostOutput {

    private Long id;
    private LocalDate date;
    private String topic;
    private String content;
    private Long userId;
    private Integer commentCount;
    private List<TagOutput> tagOutput;
}
