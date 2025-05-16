package com.ne.domain.plate;
import com.ne.domain.vehicle.enums.PlateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public class PlateRepository {


    public interface PlateNumberRepository extends JpaRepository<PlateNumber, String> {

        Optional<PlateNumber> findByPlateNumber(String plateNumber);

        List<PlateNumber> findByStatus(PlateStatus status);
    }
}
