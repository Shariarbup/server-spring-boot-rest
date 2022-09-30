package io.gateways.server.repo;

import io.gateways.server.enumeration.Status;
import io.gateways.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import io.gateways.server.enumeration.Status;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
    Server findByIpAddress(String ipAddress);
    @Query("SELECT s FROM Server s WHERE s.status = io.gateways.server.enumeration.Status.SERVER_DOWN")
    List<Server> findByServerStatus();
    List<Server> findByStatus(Status status);

    List<Server> findByBillContainsIgnoreCase(String bill);
    @Query("SELECT s FROM Server s WHERE s.bill =?1")
    List<Server> findByBill(String bill);

}
