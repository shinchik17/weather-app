package com.alexshin.weatherapp.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessions", schema = "public", indexes = {
        @Index(name = "session_user_id_index", columnList = "user_id")
})
public class UserSession implements BaseEntity<String> {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

}
