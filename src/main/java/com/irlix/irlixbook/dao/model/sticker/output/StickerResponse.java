package com.irlix.irlixbook.dao.model.sticker.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StickerResponse {

    private Long id;
    private String name;

}
