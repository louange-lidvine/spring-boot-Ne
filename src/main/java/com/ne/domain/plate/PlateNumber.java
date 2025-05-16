package com.ne.domain.plate;

import com.ne.domain.users.User;
import com.ne.domain.vehicle.enums.PlateStatus;
import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

    @Entity
    @Table(name = "plate_numbers")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public class PlateNumber {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        @Column(name = "plate_number", unique = true, nullable = false)
        private String plateNumber;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private PlateStatus status;

        @ManyToOne
        @JoinColumn(name = "owner_id")
        private User owner;

        private LocalDate issuedDate;
}