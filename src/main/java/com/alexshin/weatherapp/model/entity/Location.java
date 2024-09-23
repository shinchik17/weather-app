package com.alexshin.weatherapp.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations", schema = "public", indexes = {
        @Index(name = "loc_user_id_index", columnList = "user_id"),
        @Index(name = "loc_coords_index", columnList = "latitude, longitude")
})
public class Location implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal latitude;

    private BigDecimal longitude;

}
