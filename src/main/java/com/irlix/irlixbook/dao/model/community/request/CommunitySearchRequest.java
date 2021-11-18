package com.irlix.irlixbook.dao.model.community.request;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunitySearchRequest {
    private String name;
}
