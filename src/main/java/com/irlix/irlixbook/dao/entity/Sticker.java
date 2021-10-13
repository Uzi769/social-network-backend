package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "sticker")
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 1500)
    private String name;

    @OneToMany(mappedBy = "sticker", cascade = CascadeType.MERGE)
    private List<Content> contents;

}
