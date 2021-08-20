package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(int id);
    List<Order> findAll();

    void deleteById(int id);


}
