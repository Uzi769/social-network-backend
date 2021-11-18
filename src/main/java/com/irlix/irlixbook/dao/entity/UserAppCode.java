package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_app_code")
@Builder
public class UserAppCode {

    @Id
    private String email;

    @Column
    private UUID userId;

    @ElementCollection
    private Set<String> codes;
}
