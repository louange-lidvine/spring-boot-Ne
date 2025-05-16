package com.ne.domain.vehicle.dtos;


import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class VehicleRequest {

        @NotBlank(message = "Chassis number is required")
        private String chassisNumber;

        @NotBlank(message = "Manufacturer is required")
        private String manufacturer;

        @NotBlank(message = "Model name is required")
        private String modelName;

        @NotNull(message = "Manufacture year is required")
        private Integer year;

        @NotNull(message = "Price is required")
        private BigDecimal price;

        @NotNull(message = "Plate number ID is required")
        private UUID plateNumberId;

        @NotNull(message = "Owner ID is required")
        private UUID ownerId;
    }






