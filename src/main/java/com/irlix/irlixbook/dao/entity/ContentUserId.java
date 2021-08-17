package com.irlix.irlixbook.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentUserId implements Serializable {

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "user_id")
    private UUID userId;

}
