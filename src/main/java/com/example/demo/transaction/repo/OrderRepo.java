package com.example.demo.transaction.repo;

import com.example.demo.transaction.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
@Repository(value = "orderRep")
public interface OrderRepo extends JpaRepository<Order, Long> {
}
