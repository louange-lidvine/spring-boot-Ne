package com.ne.domain.vehicle;

import com.ne.domain.plate.PlateRepository;
import com.ne.domain.users.UserRepository;
import com.ne.domain.vehicle.dtos.VehicleRequest;
import com.ne.domain.vehicle.dtos.VehicleResponse;
import com.ne.domain.vehicle.enums.PlateStatus;
import com.ne.domain.vehicle.vehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final com.ne.domain.vehicle.vehicleRepository.VehicleRepository vehicleRepository;
    private final PlateRepository.PlateNumberRepository plateNumberRepository;
    private final UserRepository userRepository;

    // Register vehicle
    public VehicleResponse registerVehicle(VehicleRequest request) {
        var owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner not found"));

        var plate = plateNumberRepository.findById(String.valueOf(request.getPlateNumberId()))
                .orElseThrow(() -> new EntityNotFoundException("Plate number not found"));

        if (plate.getStatus() == PlateStatus.IN_USE) {
            throw new IllegalStateException("Plate number already in use");
        }

        var vehicle = Vehicle.builder()
                .chassisNumber(request.getChassisNumber())
                .manufacturer(request.getManufacturer())
                .modelName(request.getModelName())
                .year(request.getYear())
                .price(request.getPrice())
                .owner(owner)
                .plateNumber(plate)
                .registeredAt(LocalDate.now())
                .build();

        plate.setStatus(PlateStatus.IN_USE);
        plateNumberRepository.save(plate);

        var savedVehicle = vehicleRepository.save(vehicle);
        return mapToResponse(savedVehicle);
    }

    // Search by chassis number
    public Optional<VehicleResponse> findByChassis(String chassis) {
        return vehicleRepository.findByChassisNumber(chassis).map(this::mapToResponse);
    }

    // Mapping method
    private VehicleResponse mapToResponse(Vehicle vehicle) {
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