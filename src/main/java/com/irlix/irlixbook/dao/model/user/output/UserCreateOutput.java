package com.irlix.irlixbook.dao.model.user.output;

import com.irlix.irlixbook.dao.model.direction.DirectionOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateOutput {

    private Long id;
    private String fullName;
    private List<DirectionOutput> directionList;
    private List<String> photos;
    private String token;
}
