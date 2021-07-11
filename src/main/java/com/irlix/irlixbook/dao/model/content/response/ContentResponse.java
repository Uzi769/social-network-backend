package com.irlix.irlixbook.dao.model.content.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponse {

    private Long id;
    private String name;
    private String type;
    private String shortDescription;
    private String description;
    private String registrationLink;
    private String author;
    private List<String> users;
    private String stickerName;
    private List<String> pictures;
}