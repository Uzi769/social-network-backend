package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "title", nullable = false, length = 1500)
    @NotEmpty
    private String title;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

}
