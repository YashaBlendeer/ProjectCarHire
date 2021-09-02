package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.Car;
import com.yashablendeer.carhire.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to access database, contains basic CRUD operations
 *
 * @author yaroslava
 * @version 1.0
 * @see com.yashablendeer.carhire.service.CarService
 */

@Repository
public interface CarRepository  extends JpaRepository<Car, Long> {
    Car findById(int id);
    List<Car> findAllByCarMark(String carMark);
    List<Car> findAllByCarQuality(String carQuality);

    void deleteById(int id);
    List<Car> findAll();
    Page<Car> findAll(Pageable pageable);
}
