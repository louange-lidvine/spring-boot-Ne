package com.ne.domain.vehicle;
import com.ne.domain.plate.PlateNumber;
import com.ne.domain.users.User;
import com.ne.domain.vehicle.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Vehicle {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @Column(nullable = false, unique = true)
        private String chassisNumber;

        @Column(nullable = false)
        private String manufacturer;

        @Column(nullable = false)
        private String modelName;

        @Enumerated(EnumType.STRING)
        private VehicleType type;

        @Column(nullable = false)
        private int year;

        @Column(nullable = false)
        private BigDecimal price;



        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "plate_number_id")
        private PlateNumber plateNumber;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "owner_id")
        private User owner;

        private LocalDate registeredAt;
    }


