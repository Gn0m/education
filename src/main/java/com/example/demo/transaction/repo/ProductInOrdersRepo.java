package com.example.demo.transaction.repo;

import com.example.demo.transaction.model.ProductInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository
public interface ProductInOrdersRepo extends JpaRepository<ProductInOrder, Long> {
}
