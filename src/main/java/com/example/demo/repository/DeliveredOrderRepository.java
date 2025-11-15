package com.example.demo.repository;

import com.example.demo.model.DeliveredOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface DeliveredOrderRepository extends JpaRepository<DeliveredOrder, Long> {
    List<DeliveredOrder> findByDeliveredDateBetween(Date startDate, Date endDate);
}