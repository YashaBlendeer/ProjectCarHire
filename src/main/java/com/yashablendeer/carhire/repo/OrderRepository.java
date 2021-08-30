package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.Order;
import com.yashablendeer.carhire.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(int id);
    List<Order> findAll();
    Page<Order> findAll(Pageable pageable);
    Page<Order> findAllByUser(User user, Pageable pageable);

    List<Order> findAllByCarId(int id);

    void deleteById(int id);
    void deleteByCarId(int id);
}
