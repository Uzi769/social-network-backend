package com.irlix.irlixbook.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_recovery")
@Builder
public class PasswordRecoveryEntity {

    @Id
    private String id;
    @Column
    private String email;
    @Column
    private LocalDateTime createDate;
    @Column
    private UUID userId;
}
