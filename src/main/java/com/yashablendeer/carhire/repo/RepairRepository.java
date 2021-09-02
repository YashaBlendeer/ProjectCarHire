package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.Order;
import com.yashablendeer.carhire.model.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to access database, contains basic CRUD operations
 *
 * @author yaroslava
 * @version 1.0
 * @see com.yashablendeer.carhire.service.RepairService
 */

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    Repair findById(int id);
    List<Repair> findAll();
    Repair findRepairByOrderId(int id);
    void deleteById(int id);
    void deleteByOrderId(int id);

}
