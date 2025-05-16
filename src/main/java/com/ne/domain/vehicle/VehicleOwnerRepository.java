package com.ne.domain.vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ne.domain.vehicle.History;


import java.util.List;
public class VehicleOwnerRepository {


    public interface VehicleOwnershipHistoryRepository extends JpaRepository<History, Long> {


        List<History> findByChassisNumberOrderByTransferredAtAsc(String chassisNumber);

        List<History> findByPlateNumberOrderByTransferredAtAsc(String plateNumber);
    }
}
