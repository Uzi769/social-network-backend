package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ContentCommunityId implements Serializable {

    @Column(name = "community_id")
    private UUID communityId;

    @Column(name = "content_id")
    private Long contentId;
}
