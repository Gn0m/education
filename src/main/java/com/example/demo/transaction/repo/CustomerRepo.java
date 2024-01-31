package com.example.demo.transaction.repo;

import com.example.demo.transaction.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
