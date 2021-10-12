package com.irlix.irlixbook.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "title", nullable = false, length = 1500)
    @NotEmpty
    private String title;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<UserEntity> users;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

}
