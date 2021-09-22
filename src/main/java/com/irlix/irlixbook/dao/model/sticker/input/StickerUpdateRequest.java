package com.irlix.irlixbook.dao.model.sticker.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StickerUpdateRequest {

    private Long id;
    private String name;

}
