package com.ne.domain.vehicle;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

public class History {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID vehicleId;

    private UUID ownerId;

    private String plate;

    private double price;

    private LocalDateTime transferDate;

}
