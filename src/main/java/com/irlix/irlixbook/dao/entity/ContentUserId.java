package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentUserId implements Serializable {

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "user_id")
    private UUID userId;
}
