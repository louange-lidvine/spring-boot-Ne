package com.ne.domain.vehicle.mappers;
import com.ne.domain.plate.PlateNumber;
import com.ne.domain.vehicle.dtos.VehicleRequest;
import com.ne.domain.vehicle.dtos.VehicleResponse;
import com.ne.domain.vehicle.Vehicle;
 import com.ne.domain.users.User;

import java.util.UUID;

public class VehicleMapper {
        public static Vehicle toEntity(VehicleRequest dto, User owner, PlateNumber plateNumber) {
            return Vehicle.builder()
                    .chassisNumber(dto.getChassisNumber())
                    .manufacturer(dto.getManufacturer())
                    .modelName(dto.getModelName())
                    .year(dto.getYear())
                    .price(dto.getPrice())
                    .owner(owner)
                    .plateNumber(plateNumber)
                    .build();
        }

        public static VehicleResponse toResponse(Vehicle vehicle) {
            return VehicleResponse.builder()
                    .id(vehicle.getId())
                    .chassisNumber(vehicle.getChassisNumber())
                    .manufacturer(vehicle.getManufacturer())
                    .modelName(vehicle.getModelName())
                    .year(vehicle.getYear())
                    .price(vehicle.getPrice())
                    .ownerId(vehicle.getOwner().getId())
                    .plateNumberId(UUID.fromString(vehicle.getPlateNumber().getId()))
                    .build();
        }
    }


