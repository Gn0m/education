package com.example.demo.transaction.repo;

import com.example.demo.transaction.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Repository(value = "productRep")
public interface ProductRepo extends JpaRepository<Product, Long> {

    Set<Product> findByIdIn(Collection<Long> collection);
}
