package com.irlix.irlixbook.dao.model.sticker.input;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StickerUpdateRequest {

    private Long id;

    private String name;
}
