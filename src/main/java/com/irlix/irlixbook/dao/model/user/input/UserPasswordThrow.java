package com.irlix.irlixbook.dao.model.user.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordThrow extends UserPasswordInput{

    @NotNull
    private Long userId;
}

