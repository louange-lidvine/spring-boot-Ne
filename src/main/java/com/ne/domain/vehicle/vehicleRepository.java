package com.ne.domain.vehicle;
import com.ne.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public class vehicleRepository {


    public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

        Optional<Vehicle> findByChassisNumber(String chassisNumber);
    }

//    List<Vehicle> findByOwnerId(UUID ownerId);
//    Optional<Vehicle> findByPlateNumber_PlateNumber(String plateNumber);


}
