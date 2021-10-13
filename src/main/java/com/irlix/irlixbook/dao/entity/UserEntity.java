package com.irlix.irlixbook.dao.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_entity")
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @ToString.Include
    private UUID id;

    @NotEmpty
    @Column(name = "surname", length = 1500)
    private String surname;

    @NotEmpty
    @Column(name = "name", length = 1500)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    @Pattern(regexp = "(^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$)")
    private String phone;

    @Column(name = "description", length = 3000)
    private String description;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "vk")
    private String vk;

    @Column(name = "facebook")
    private String faceBook;

    @Column(name = "skype")
    private String skype;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "linked_in")
    private String linkedIn;

    @Column(name = "telegram")
    private String telegram;

    @Column(name = "blocked")
    private LocalDateTime blocked;

    @Column(name = "avatar", length = 1500)
    private String avatar;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.MERGE)
    private List<Content> contents;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.MERGE)
    private List<Community> communities;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ContentUser> contentUsers;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<RoleStatusUserCommunity> roleStatusUserCommunities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getName().name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
