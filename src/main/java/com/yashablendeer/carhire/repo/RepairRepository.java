package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.Order;
import com.yashablendeer.carhire.model.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    Repair findById(int id);
    List<Repair> findAll();

    void deleteById(int id);

}
