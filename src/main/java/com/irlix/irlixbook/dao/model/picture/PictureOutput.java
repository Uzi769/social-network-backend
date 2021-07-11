package com.irlix.irlixbook.dao.model.picture;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PictureOutput {

    private UUID pictureId;
    private String url;
    private UUID contentId;
}
