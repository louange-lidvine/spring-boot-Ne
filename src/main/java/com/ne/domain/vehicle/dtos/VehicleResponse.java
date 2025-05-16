package com.ne.domain.vehicle.dtos;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {
    private UUID id;
    private String chassisNumber;
    private String manufacturer;
    private String modelName;
    private Integer year;
    private BigDecimal price;
    private UUID plateNumberId;
    private UUID ownerId;
}

